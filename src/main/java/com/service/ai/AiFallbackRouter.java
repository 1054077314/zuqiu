package com.service.ai;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 当模型未触发工具调用时，按关键词规则兜底路由到真实数据库查询。
 */
@Component
public class AiFallbackRouter {

    @Autowired
    private AiToolExecutor toolExecutor;

    @Autowired
    private AiReplyFormatter replyFormatter;

    @Autowired
    private AiExecutionRecorder executionRecorder;

    public boolean looksLikeSystemDataQuestion(String message) {
        if (StringUtils.isBlank(message)) {
            return false;
        }
        return message.contains("球员") || message.contains("公告") || message.contains("赛事")
                || message.contains("比赛") || message.contains("训练") || message.contains("合同")
                || message.contains("数据") || message.contains("档案") || message.contains("系统内");
    }

    public JSONArray fallbackToolResults(String userMessage) {
        JSONArray results = new JSONArray();
        JSONObject arguments = new JSONObject();
        arguments.put("keyword", extractKeyword(userMessage));
        arguments.put("playerName", extractContractKeyword(userMessage));
        if (replyFormatter.asksForLatestOne(userMessage)) {
            arguments.put("limit", 1);
        }

        if (userMessage.contains("合同")) {
            results.add(recordFallback("queryContracts", arguments, toolExecutor.queryContracts(arguments)));
        } else if (userMessage.contains("公告") || userMessage.contains("通知")) {
            results.add(recordFallback("queryAnnouncements", arguments, toolExecutor.queryAnnouncements(arguments)));
        } else if (userMessage.contains("赛事") || userMessage.contains("比赛") || userMessage.contains("赛程")) {
            results.add(recordFallback("queryMatches", arguments, toolExecutor.queryMatches(arguments)));
        } else if (userMessage.contains("训练")) {
            results.add(recordFallback("queryTrainingPlans", arguments, toolExecutor.queryTrainingPlans(arguments)));
        } else if (userMessage.contains("数据")) {
            results.add(recordFallback("queryPlayerData", arguments, toolExecutor.queryPlayerData(arguments)));
        } else if (userMessage.contains("球员") || userMessage.contains("队员") || userMessage.contains("档案")) {
            results.add(recordFallback("queryPlayers", arguments, toolExecutor.queryPlayers(arguments)));
        }
        return results;
    }

    private JSONObject recordFallback(String toolName, JSONObject arguments, JSONObject result) {
        return executionRecorder.record(toolName, arguments, result, 0L, true);
    }

    public String extractContractKeyword(String message) {
        if (StringUtils.isBlank(message)) {
            return "";
        }
        String value = message;
        int contractIndex = value.indexOf("合同");
        if (contractIndex > 0) {
            value = value.substring(0, contractIndex);
        }
        return stripQueryWords(value);
    }

    public String extractKeyword(String message) {
        if (StringUtils.isBlank(message)) {
            return "";
        }
        return stripQueryWords(message);
    }

    private String stripQueryWords(String message) {
        String value = message.replaceAll("[\\s，。！？!?,；;：:、]+", "");
        String[] words = new String[]{
                "帮我", "请问", "请", "查询一下", "查一下", "查询", "查看", "看看", "查",
                "系统内", "系统", "最新", "最近", "第一条", "刚发布", "刚录入",
                "什么时候到期", "到什么时候", "到期时间", "什么时候", "到期",
                "球员档案", "球员资料", "球员信息", "队员档案", "队员资料", "档案",
                "赛事信息", "比赛信息", "赛程信息", "公告信息", "通知信息",
                "训练计划", "训练安排", "球员数据", "数据记录",
                "合同信息", "合同资料", "合同", "球员", "队员", "赛事", "比赛", "赛程",
                "公告", "通知", "训练", "数据", "信息", "资料", "的"
        };
        for (String word : words) {
            value = value.replace(word, "");
        }
        return value.trim();
    }
}

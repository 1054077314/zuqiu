package com.service.ai;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 将工具查询结果格式化为后端确定性纯文本回复。
 */
@Component
public class AiReplyFormatter {

    public String formatToolReply(String userMessage, JSONArray toolResults) {
        if (toolResults == null || toolResults.isEmpty()) {
            return "系统没有返回可用查询结果。";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < toolResults.size(); i++) {
            JSONObject payload = toolResults.getJSONObject(i);
            if (i > 0) {
                builder.append("\n\n");
            }
            builder.append(formatSingleToolPayload(userMessage, payload));
        }
        return builder.toString();
    }

    public String formatSingleToolPayload(String userMessage, JSONObject payload) {
        String module = payload.getString("module");
        String keyword = payload.getString("keyword");
        JSONArray records = payload.getJSONArray("records");
        int count = records == null ? 0 : records.size();

        if (count == 0) {
            if ("合同信息".equals(module)) {
                return formatEmptyContractReply(userMessage, payload, keyword);
            }
            return "系统内未查到匹配的" + defaultText(module, "数据")
                    + ("无".equals(keyword) ? "。" : "，关键词：" + keyword + "。");
        }

        if (count > 1 && !"无".equals(keyword)) {
            return "系统内匹配到多条" + module + "，请根据名称或编号进一步指定：\n" + formatRecords(module, records, payload, userMessage);
        }

        if (asksForLatestOne(userMessage) && count == 1) {
            return "最新" + module + "：\n" + formatRecords(module, records, payload, userMessage);
        }

        return "系统查询到" + count + "条" + module + "：\n" + formatRecords(module, records, payload, userMessage);
    }

    public boolean asksForLatestOne(String message) {
        if (StringUtils.isBlank(message)) {
            return false;
        }
        return message.contains("最新") || message.contains("最近") || message.contains("第一条")
                || message.contains("刚发布") || message.contains("刚录入");
    }

    public boolean asksContractUnavailableField(String message) {
        if (StringUtils.isBlank(message)) {
            return false;
        }
        return message.contains("到期") || message.contains("薪资") || message.contains("工资")
                || message.contains("期限") || message.contains("多少钱");
    }

    private String formatEmptyContractReply(String userMessage, JSONObject payload, String keyword) {
        if (payload.getBooleanValue("playerMissing") && !"无".equals(keyword)) {
            return "系统里没有找到名为“" + keyword + "”的球员，无法查询他的合同"
                    + (asksContractUnavailableField(userMessage) ? "到期时间" : "信息")
                    + "。请先确认球员姓名，或先用“查看球员档案”查系统里的球员。";
        }

        JSONArray candidates = payload.getJSONArray("playerCandidates");
        if (payload.getBooleanValue("playerAmbiguous") && candidates != null && !candidates.isEmpty()) {
            return "系统里匹配到多个可能的球员，请按姓名或编号再问一次：\n"
                    + formatPlayerCandidates(candidates);
        }

        if (asksContractUnavailableField(userMessage)) {
            return "系统内未查到匹配的合同信息"
                    + ("无".equals(keyword) ? "。" : "，关键词：" + keyword + "。")
                    + "另外当前合同表未维护合同到期时间、薪资或工资字段，不能从系统数据库返回这些信息。";
        }

        return "系统内未查到匹配的合同信息"
                + ("无".equals(keyword) ? "。" : "，关键词：" + keyword + "。");
    }

    private String formatPlayerCandidates(JSONArray candidates) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < candidates.size(); i++) {
            JSONObject row = candidates.getJSONObject(i);
            if (i > 0) {
                builder.append("\n");
            }
            builder.append(i + 1)
                    .append(". ")
                    .append(defaultText(row.getString("name"), "未命名球员"))
                    .append("，编号：")
                    .append(defaultText(row.getString("number"), "暂无"))
                    .append("，账号：")
                    .append(defaultText(row.getString("account"), "暂无"));
        }
        return builder.toString();
    }

    private String formatRecords(String module, JSONArray records, JSONObject payload, String userMessage) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < records.size(); i++) {
            JSONObject row = records.getJSONObject(i);
            if (i > 0) {
                builder.append("\n");
            }
            builder.append(i + 1).append(". ");
            if ("球员档案".equals(module)) {
                builder.append(defaultText(row.getString("name"), "未命名球员"))
                        .append("，编号：").append(defaultText(row.getString("number"), "暂无"))
                        .append("，账号：").append(defaultText(row.getString("account"), "暂无"))
                        .append("，性别：").append(defaultText(row.getString("sex"), "暂无"))
                        .append("，手机：").append(defaultText(row.getString("phone"), "暂无"))
                        .append("，邮箱：").append(defaultText(row.getString("email"), "暂无"))
                        .append("，详情：").append(row.getString("detailUrl"));
            } else if ("公告信息".equals(module)) {
                builder.append(defaultText(row.getString("title"), "未命名公告"))
                        .append("，发布时间：").append(defaultText(row.getString("publishTime"), "暂无"))
                        .append("，摘要：").append(defaultText(row.getString("summary"), "暂无"))
                        .append("，详情：").append(row.getString("detailUrl"));
            } else if ("赛事信息".equals(module)) {
                builder.append(defaultText(row.getString("title"), "未命名赛事"))
                        .append("，地点：").append(defaultText(row.getString("address"), "暂无"))
                        .append("，录入时间：").append(defaultText(row.getString("publishTime"), "暂无"))
                        .append("，介绍：").append(defaultText(row.getString("summary"), "暂无"))
                        .append("，详情：").append(row.getString("detailUrl"));
            } else if ("训练计划".equals(module)) {
                builder.append(defaultText(row.getString("title"), "未命名训练计划"))
                        .append("，编号：").append(defaultText(row.getString("number"), "暂无"))
                        .append("，科目：").append(defaultText(row.getString("subject"), "暂无"))
                        .append("，训练日期：").append(defaultText(row.getString("trainingDate"), "暂无"))
                        .append("，说明：").append(defaultText(row.getString("summary"), "暂无"))
                        .append("，详情：").append(row.getString("detailUrl"));
            } else if ("合同信息".equals(module)) {
                builder.append(defaultText(row.getString("title"), "未命名合同"))
                        .append("，球员ID：").append(defaultText(row.getString("playerId"), "暂无"))
                        .append("，附件：").append(defaultText(row.getString("file"), "暂无"))
                        .append("，备注：").append(defaultText(row.getString("remark"), "暂无"))
                        .append("，创建时间：").append(defaultText(row.getString("createTime"), "暂无"))
                        .append("，详情：").append(row.getString("detailUrl"));
            } else if ("球员数据".equals(module)) {
                builder.append(defaultText(row.getString("title"), "未命名球员数据"))
                        .append("，编号：").append(defaultText(row.getString("number"), "暂无"))
                        .append("，记录日期：").append(defaultText(row.getString("recordDate"), "暂无"))
                        .append("，说明：").append(defaultText(row.getString("summary"), "暂无"))
                        .append("，详情：").append(row.getString("detailUrl"));
            } else {
                builder.append(row.toJSONString());
            }
        }

        if ("合同信息".equals(module) && asksContractUnavailableField(userMessage)) {
            builder.append("\n说明：当前合同表未维护合同到期时间、薪资或工资字段，不能从系统数据库返回这些信息。");
        } else if ("合同信息".equals(module) && StringUtils.isNotBlank(payload.getString("schemaNote"))) {
            builder.append("\n说明：").append(payload.getString("schemaNote"));
        }
        return builder.toString();
    }

    private String defaultText(String value, String fallback) {
        return StringUtils.isBlank(value) ? fallback : value;
    }
}

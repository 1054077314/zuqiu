package com.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.service.ai.AiFallbackRouter;
import com.service.ai.AiReplyFormatter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AiReplyFormatterTest {

    private final AiReplyFormatter formatter = new AiReplyFormatter();

    @Test
    void latestMatchUsesSinglePlainTextResult() {
        JSONArray results = new JSONArray();
        JSONObject payload = payload("赛事信息", "无");
        payload.getJSONArray("records").add(record(
                "title", "2026年热身赛 vs 浙江队",
                "address", "虹口足球场",
                "publishTime", "2026-07-12 18:53",
                "summary", "真实赛事介绍",
                "detailUrl", "/zuqiujulebguanli/front/pages/saishi/detail.html?id=335"));
        results.add(payload);

        String reply = formatter.formatToolReply("查最新赛事", results);

        assertTrue(reply.startsWith("最新赛事信息：\n1. 2026年热身赛 vs 浙江队"));
        assertFalse(reply.contains("##"));
        assertFalse(reply.contains("**"));
        assertFalse(reply.contains("\n2."));
    }

    @Test
    void missingPlayerShowsNoResultInsteadOfInventingProfile() {
        JSONArray results = new JSONArray();
        results.add(payload("球员档案", "张三丰"));

        String reply = formatter.formatToolReply("查不存在球员张三丰", results);

        assertEquals("系统内未查到匹配的球员档案，关键词：张三丰。", reply);
    }

    @Test
    void missingContractPlayerExplainsPlayerNotFoundFirst() {
        JSONArray results = new JSONArray();
        JSONObject payload = payload("合同信息", "张三");
        payload.put("playerMissing", true);
        results.add(payload);

        String reply = formatter.formatToolReply("查张三的合同什么时候到期", results);

        assertTrue(reply.contains("系统里没有找到名为“张三”的球员"));
        assertTrue(reply.contains("无法查询他的合同到期时间"));
    }

    @Test
    void existingContractExpiryQuestionExplainsUnavailableSchemaField() {
        JSONArray results = new JSONArray();
        JSONObject payload = payload("合同信息", "宋彬建");
        payload.getJSONArray("records").add(record(
                "title", "宋彬建合同",
                "playerId", "206",
                "file", "contract.pdf",
                "remark", "一线队合同",
                "createTime", "2026-07-01 10:00",
                "detailUrl", "/zuqiujulebguanli/front/pages/hetong/detail.html?id=1"));
        results.add(payload);

        String reply = formatter.formatToolReply("查宋彬建的合同什么时候到期", results);

        assertTrue(reply.contains("宋彬建合同"));
        assertTrue(reply.contains("当前合同表未维护合同到期时间、薪资或工资字段"));
    }

    @Test
    void ambiguousKeywordAsksUserToSpecify() {
        JSONArray results = new JSONArray();
        JSONObject payload = payload("球员档案", "宋");
        payload.getJSONArray("records").add(record(
                "name", "宋彬建",
                "number", "YH2024200",
                "account", "player_206",
                "sex", "男",
                "phone", "185****6188",
                "email", "player_206@club.com",
                "detailUrl", "/zuqiujulebguanli/front/pages/yonghu/detail.html?id=206"));
        payload.getJSONArray("records").add(record(
                "name", "宋涛",
                "number", "YH2024100",
                "account", "player_100",
                "sex", "男",
                "phone", "138****0000",
                "email", "player_100@club.com",
                "detailUrl", "/zuqiujulebguanli/front/pages/yonghu/detail.html?id=100"));
        results.add(payload);

        String reply = formatter.formatToolReply("查宋的球员档案", results);

        assertTrue(reply.startsWith("系统内匹配到多条球员档案，请根据名称或编号进一步指定："));
        assertTrue(reply.contains("1. 宋彬建，编号：YH2024200"));
        assertTrue(reply.contains("2. 宋涛，编号：YH2024100"));
    }

    private JSONObject payload(String module, String keyword) {
        JSONObject payload = new JSONObject();
        payload.put("module", module);
        payload.put("keyword", keyword);
        payload.put("count", 0);
        payload.put("records", new JSONArray());
        return payload;
    }

    private JSONObject record(String... values) {
        JSONObject row = new JSONObject();
        for (int i = 0; i < values.length; i += 2) {
            row.put(values[i], values[i + 1]);
        }
        return row;
    }
}

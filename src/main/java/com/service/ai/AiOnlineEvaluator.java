package com.service.ai;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.service.AiChatService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 在线模型评测执行器。需要配置 DEEPSEEK_API_KEY 后手动触发。
 */
@Component
@ConditionalOnProperty(name = "ai.eval.online-enabled", havingValue = "true")
public class AiOnlineEvaluator {

    private static final int REPEAT_COUNT = 3;

    @Autowired
    private AiChatService aiChatService;

    public Map<String, Object> runOnlineEvaluation() throws Exception {
        List<JSONObject> cases = loadDataset();
        int total = cases.size() * REPEAT_COUNT;
        int stableReplies = 0;
        int factConsistent = 0;
        int fallbackTriggered = 0;
        List<Long> durations = new ArrayList<>();

        for (JSONObject testCase : cases) {
            List<String> replies = new ArrayList<>();
            for (int i = 0; i < REPEAT_COUNT; i++) {
                long start = System.currentTimeMillis();
                String reply = aiChatService.chat(testCase.getString("question"));
                durations.add(System.currentTimeMillis() - start);
                replies.add(reply);
            }

            if (replies.stream().distinct().count() == 1) {
                stableReplies++;
            }

            JSONArray mustContain = testCase.getJSONArray("replyMustContain");
            if (mustContain != null && replies.stream().allMatch(reply -> containsAll(reply, mustContain))) {
                factConsistent++;
            }

            if (replies.stream().anyMatch(reply -> reply.contains("模型没有触发工具调用")
                    || reply.contains("系统查询到")
                    || reply.contains("系统内未查到")
                    || reply.contains("最新"))) {
                fallbackTriggered++;
            }
        }

        Map<String, Object> report = new LinkedHashMap<>();
        report.put("totalRuns", total);
        report.put("cases", cases.size());
        report.put("repeatPerCase", REPEAT_COUNT);
        report.put("stabilityRate", rate(stableReplies, cases.size()));
        report.put("factConsistencyRate", rate(factConsistent, cases.size()));
        report.put("fallbackOrToolReplyRate", rate(fallbackTriggered, cases.size()));
        report.put("avgDurationMs", durations.stream().mapToLong(Long::longValue).average().orElse(0));
        return report;
    }

    private boolean containsAll(String reply, JSONArray mustContain) {
        for (int i = 0; i < mustContain.size(); i++) {
            if (!reply.contains(mustContain.getString(i))) {
                return false;
            }
        }
        return true;
    }

    private String rate(int numerator, int denominator) {
        if (denominator == 0) {
            return "0%";
        }
        return String.format("%.1f%%", numerator * 100.0 / denominator);
    }

    private List<JSONObject> loadDataset() throws Exception {
        try (InputStream inputStream = new ClassPathResource("ai/eval-dataset.json").getInputStream()) {
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            JSONArray array = JSON.parseArray(content);
            List<JSONObject> cases = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                cases.add(array.getJSONObject(i));
            }
            return cases;
        }
    }
}

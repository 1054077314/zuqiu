package com.service.ai;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 离线评测：不调用真实模型，验证确定性回复与边界场景处理。
 */
class AiOfflineEvaluatorTest {

    private final AiReplyFormatter formatter = new AiReplyFormatter();
    private final AiFallbackRouter fallbackRouter = new AiFallbackRouter();

    @Test
    void offlineDatasetShouldCoverCoreScenarios() throws Exception {
        List<JSONObject> cases = loadDataset();
        assertTrue(cases.size() >= 10, "评测集至少应包含 10 条用例");

        int passed = 0;
        List<String> failures = new ArrayList<>();
        for (JSONObject testCase : cases) {
            String id = testCase.getString("id");
            String question = testCase.getString("question");
            JSONObject payload = testCase.getJSONObject("offlinePayload");

            if (payload != null) {
                JSONArray results = new JSONArray();
                results.add(payload);
                String reply = formatter.formatToolReply(question, results);
                if (!assertReply(testCase, reply)) {
                    failures.add(id + " -> " + reply);
                    continue;
                }
            }

            if (testCase.getString("expectedFallbackTool") != null) {
                assertTrue(fallbackRouter.looksLikeSystemDataQuestion(question), id + " should look like system question");
            }

            passed++;
        }

        assertTrue(failures.isEmpty(), "失败用例: " + failures);
        assertTrue(passed >= cases.size() * 0.9, "通过率应 >= 90%，实际通过 " + passed + "/" + cases.size());
    }

    @Test
    void fallbackKeywordExtractionCases() {
        assertTrue(fallbackRouter.looksLikeSystemDataQuestion("查最新赛事"));
        assertFalse(fallbackRouter.looksLikeSystemDataQuestion("什么是越位"));
    }

    private boolean assertReply(JSONObject testCase, String reply) {
        JSONArray mustContain = testCase.getJSONArray("replyMustContain");
        if (mustContain != null) {
            for (int i = 0; i < mustContain.size(); i++) {
                if (!reply.contains(mustContain.getString(i))) {
                    return false;
                }
            }
        }
        JSONArray mustNotContain = testCase.getJSONArray("replyMustNotContain");
        if (mustNotContain != null) {
            for (int i = 0; i < mustNotContain.size(); i++) {
                if (reply.contains(mustNotContain.getString(i))) {
                    return false;
                }
            }
        }
        return true;
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

package com.service.ai;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 封装 OpenAI 兼容 Chat Completions API 调用。
 */
@Component
public class AiProviderClient {

    private static final Logger logger = LoggerFactory.getLogger(AiProviderClient.class);

    @Value("${ai.api-key:}")
    private String apiKey;

    @Value("${ai.api-url:https://api.deepseek.com/chat/completions}")
    private String apiUrl;

    @Value("${ai.model:deepseek-chat}")
    private String model;

    @Value("${ai.timeout:30000}")
    private int timeout;

    @Value("${ai.http-referer:http://localhost:8080}")
    private String httpReferer;

    public boolean isConfigured() {
        return StringUtils.isNotBlank(apiKey);
    }

    public JSONObject chatWithTools(JSONArray messages, JSONArray tools) {
        JSONObject requestBody = baseRequest(messages);
        requestBody.put("tools", tools);
        requestBody.put("tool_choice", "auto");
        return postChat(requestBody);
    }

    public JSONObject baseRequest(JSONArray messages) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model);
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.2);
        return requestBody;
    }

    public JSONObject postChat(JSONObject requestBody) {
        HttpResponse response = HttpRequest.post(apiUrl)
                .header(Header.AUTHORIZATION, "Bearer " + apiKey)
                .header(Header.CONTENT_TYPE, "application/json; charset=utf-8")
                .header("HTTP-Referer", httpReferer)
                .header("X-Title", "zuqiu-club-management")
                .timeout(timeout)
                .body(requestBody.toJSONString())
                .execute();

        String body = response.body();
        if (!response.isOk()) {
            logger.warn("AI provider returned status {}, body: {}", response.getStatus(), body);
            throw new IllegalStateException("AI provider returned status " + response.getStatus());
        }
        return JSON.parseObject(body);
    }

    public JSONObject firstAssistantMessage(JSONObject response) {
        JSONObject error = response.getJSONObject("error");
        if (error != null) {
            logger.warn("AI provider error: {}", error.toJSONString());
            throw new IllegalStateException("AI provider error");
        }

        JSONArray choices = response.getJSONArray("choices");
        if (choices == null || choices.isEmpty()) {
            return null;
        }
        return choices.getJSONObject(0).getJSONObject("message");
    }

    public String contentFromMessage(JSONObject message) {
        if (message == null || StringUtils.isBlank(message.getString("content"))) {
            return "AI 服务暂时没有返回内容。";
        }
        return message.getString("content").trim();
    }

    public JSONObject message(String role, String content) {
        JSONObject item = new JSONObject();
        item.put("role", role);
        item.put("content", content);
        return item;
    }
}

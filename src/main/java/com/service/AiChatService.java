package com.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.service.ai.AiExecutionRecorder;
import com.service.ai.AiFallbackRouter;
import com.service.ai.AiProviderClient;
import com.service.ai.AiReplyFormatter;
import com.service.ai.AiToolDefinitionService;
import com.service.ai.AiToolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AI 问答编排服务：模型负责判断是否调用工具，Java 负责执行真实业务查询与确定性回复。
 */
@Service
public class AiChatService {

    private static final Logger logger = LoggerFactory.getLogger(AiChatService.class);

    @Autowired
    private AiProviderClient providerClient;
    @Autowired
    private AiToolDefinitionService toolDefinitionService;
    @Autowired
    private AiToolExecutor toolExecutor;
    @Autowired
    private AiFallbackRouter fallbackRouter;
    @Autowired
    private AiReplyFormatter replyFormatter;
    @Autowired
    private AiExecutionRecorder executionRecorder;

    public String chat(String userMessage) {
        if (!providerClient.isConfigured()) {
            return "AI 功能还没有配置 API Key。请在环境变量 DEEPSEEK_API_KEY 中配置后重启服务。";
        }

        executionRecorder.startSession();
        try {
            JSONArray messages = new JSONArray();
            messages.add(providerClient.message("system",
                    "你是足球俱乐部管理系统的智能助手。用户询问系统内的球员、公告、赛事、训练、合同、球员数据时，必须调用提供的工具查询真实数据库；不要编造系统数据。用户说“最新、最近、第一条、刚发布”时，只查1条。最终回复使用简洁纯文本，不要使用 Markdown 标题符号、星号加粗或 emoji。普通足球知识问题可以直接回答。"));
            messages.add(providerClient.message("user", userMessage));

            JSONObject firstResponse = providerClient.chatWithTools(messages, toolDefinitionService.buildTools());
            JSONObject assistantMessage = providerClient.firstAssistantMessage(firstResponse);
            JSONArray toolCalls = assistantMessage == null ? null : assistantMessage.getJSONArray("tool_calls");

            if (toolCalls == null || toolCalls.isEmpty()) {
                if (fallbackRouter.looksLikeSystemDataQuestion(userMessage)) {
                    JSONArray fallbackResults = fallbackRouter.fallbackToolResults(userMessage);
                    if (!fallbackResults.isEmpty()) {
                        return replyFormatter.formatToolReply(userMessage, fallbackResults);
                    }
                    return "这是系统业务数据问题，但模型没有触发工具调用。你可以换成更明确的问题，例如：\"查询最新赛事\"、\"查看公告\"、\"查看球员张三档案\"。";
                }
                return providerClient.contentFromMessage(assistantMessage);
            }

            JSONArray toolResults = new JSONArray();
            for (int i = 0; i < toolCalls.size(); i++) {
                JSONObject toolCall = toolCalls.getJSONObject(i);
                toolResults.add(toolExecutor.executeToolCall(toolCall, userMessage));
            }
            return replyFormatter.formatToolReply(userMessage, toolResults);
        } catch (Exception e) {
            logger.warn("AI chat request failed: {}", e.getMessage(), e);
            return "AI 服务请求失败，请检查网络、API Key 或模型工具调用配置。";
        } finally {
            executionRecorder.clearSession();
        }
    }
}

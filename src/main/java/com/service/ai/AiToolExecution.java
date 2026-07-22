package com.service.ai;

import com.alibaba.fastjson2.JSONObject;

/**
 * 单次 AI 工具调用执行记录，供评测与可观测性使用。
 */
public class AiToolExecution {

    private final String toolName;
    private final JSONObject arguments;
    private final JSONObject result;
    private final long durationMs;
    private final boolean fallback;

    public AiToolExecution(String toolName, JSONObject arguments, JSONObject result, long durationMs, boolean fallback) {
        this.toolName = toolName;
        this.arguments = arguments;
        this.result = result;
        this.durationMs = durationMs;
        this.fallback = fallback;
    }

    public String getToolName() {
        return toolName;
    }

    public JSONObject getArguments() {
        return arguments;
    }

    public JSONObject getResult() {
        return result;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public boolean isFallback() {
        return fallback;
    }
}

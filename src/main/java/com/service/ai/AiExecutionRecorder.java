package com.service.ai;

import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 记录 AI 工具调用名称、参数、耗时与结果，供评测与排障使用。
 */
@Component
public class AiExecutionRecorder {

    private static final Logger logger = LoggerFactory.getLogger(AiExecutionRecorder.class);

    private final ThreadLocal<List<AiToolExecution>> currentExecutions = ThreadLocal.withInitial(ArrayList::new);

    public void startSession() {
        currentExecutions.set(new ArrayList<>());
    }

    public JSONObject record(String toolName, JSONObject arguments, JSONObject result, long durationMs, boolean fallback) {
        AiToolExecution execution = new AiToolExecution(toolName, arguments, result, durationMs, fallback);
        currentExecutions.get().add(execution);
        logger.info("AI tool executed: name={}, fallback={}, durationMs={}, count={}",
                toolName, fallback, durationMs, result == null ? 0 : result.getIntValue("count"));
        return result;
    }

    public List<AiToolExecution> getExecutions() {
        return Collections.unmodifiableList(new ArrayList<>(currentExecutions.get()));
    }

    public void clearSession() {
        currentExecutions.remove();
    }
}

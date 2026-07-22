package com.interceptor;

import com.common.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对公开 AI 接口做简单 IP 级频率限制，防止滥用 API 额度。
 */
@Component
public class AiRateLimitInterceptor implements HandlerInterceptor {

    private final Map<String, Deque<Long>> requestHistory = new ConcurrentHashMap<>();

    @Value("${ai.rate-limit.max-requests:20}")
    private int maxRequests;

    @Value("${ai.rate-limit.window-seconds:60}")
    private int windowSeconds;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String clientKey = resolveClientKey(request);
        long now = System.currentTimeMillis();
        long windowStart = now - windowSeconds * 1000L;

        Deque<Long> history = requestHistory.computeIfAbsent(clientKey, key -> new ArrayDeque<>());
        synchronized (history) {
            while (!history.isEmpty() && history.peekFirst() < windowStart) {
                history.pollFirst();
            }
            if (history.size() >= maxRequests) {
                throw new BusinessException("AI 请求过于频繁，请稍后再试", 429);
            }
            history.addLast(now);
        }
        return true;
    }

    private String resolveClientKey(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}

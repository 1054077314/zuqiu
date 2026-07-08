package com.service;

import com.entity.DictionaryEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 字典数据 Redis 缓存服务
 *
 * 缓存策略：
 * - key 格式: zuqiu:dict:{dic_code}（Redis Hash 结构）
 * - field: codeIndex(Integer 转 String)
 * - value: indexName
 *
 * 降级策略：
 * - 每个 Redis 操作用 try-catch 包裹
 * - Redis 不可用时自动降级到 ServletContext（原方案）
 * - 30 秒内不重试，避免频繁抛异常
 *
 * 注意：Redis 宕机期间的字典变更只写入 ServletContext，不会同步到 Redis。
 * Redis 恢复后需重启应用或执行一次字典变更来刷新缓存，否则可能读到旧数据。
 */
@Service
public class DictionaryCacheService {

    private static final Logger logger = LoggerFactory.getLogger(DictionaryCacheService.class);
    private static final String KEY_PREFIX = "zuqiu:dict:";

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    private volatile boolean redisAvailable = true;
    private volatile long lastFailTime = 0;

    /**
     * 写入单个字典条目的缓存
     */
    public void put(String dicCode, Map<Integer, String> valueMap) {
        if (!checkAvailable()) return;
        try {
            Map<String, String> hash = new HashMap<>();
            for (Map.Entry<Integer, String> e : valueMap.entrySet()) {
                hash.put(String.valueOf(e.getKey()), e.getValue());
            }
            redisTemplate.opsForHash().putAll(KEY_PREFIX + dicCode, hash);
        } catch (Exception e) {
            markUnavailable("put", e);
        }
    }

    /**
     * 读取某个字典类型的全部映射
     * 返回 null 表示 Redis 不可用或未命中
     */
    @SuppressWarnings("unchecked")
    public Map<Integer, String> get(String dicCode) {
        if (!checkAvailable()) return null;
        try {
            Map<Object, Object> hash = redisTemplate.opsForHash().entries(KEY_PREFIX + dicCode);
            if (hash.isEmpty()) return null;
            Map<Integer, String> result = new HashMap<>();
            for (Map.Entry<Object, Object> e : hash.entrySet()) {
                result.put(Integer.valueOf(String.valueOf(e.getKey())), String.valueOf(e.getValue()));
            }
            return result;
        } catch (Exception e) {
            markUnavailable("get", e);
            return null;
        }
    }

    /**
     * 获取完整字典映射（兼容 ServletContext 的 Map&lt;String, Map&lt;Integer, String&gt;&gt; 结构）
     * 用于 dictionaryConvert 中替代 ServletContext 读取
     */
    @SuppressWarnings("unchecked")
    public Map<String, Map<Integer, String>> getAllAsMap() {
        if (!checkAvailable()) return null;
        try {
            Set<String> keys = redisTemplate.keys(KEY_PREFIX + "*");
            if (keys == null || keys.isEmpty()) return null;
            Map<String, Map<Integer, String>> result = new HashMap<>();
            for (String key : keys) {
                Map<Object, Object> hash = redisTemplate.opsForHash().entries(key);
                if (!hash.isEmpty()) {
                    Map<Integer, String> subMap = new HashMap<>();
                    for (Map.Entry<Object, Object> e : hash.entrySet()) {
                        subMap.put(Integer.valueOf(String.valueOf(e.getKey())),
                                   String.valueOf(e.getValue()));
                    }
                    result.put(key.substring(KEY_PREFIX.length()), subMap);
                }
            }
            return result.isEmpty() ? null : result;
        } catch (Exception e) {
            markUnavailable("getAllAsMap", e);
            return null;
        }
    }

    /**
     * 清除所有字典缓存并从数据库重新加载
     */
    public void refresh(List<DictionaryEntity> entities) {
        if (!checkAvailable()) return;
        try {
            Set<String> keys = redisTemplate.keys(KEY_PREFIX + "*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
            Map<String, Map<Integer, String>> map = new HashMap<>();
            for (DictionaryEntity d : entities) {
                Map<Integer, String> m = map.get(d.getDicCode());
                if (m == null) {
                    m = new HashMap<>();
                }
                m.put(d.getCodeIndex(), d.getIndexName());
                map.put(d.getDicCode(), m);
            }
            for (Map.Entry<String, Map<Integer, String>> entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
            redisAvailable = true;
            logger.info("Redis 字典缓存刷新完成，共 {} 个类型", map.size());
        } catch (Exception e) {
            markUnavailable("refresh", e);
        }
    }

    private boolean checkAvailable() {
        if (redisTemplate == null) return false;
        if (redisAvailable) return true;
        if (System.currentTimeMillis() - lastFailTime > 30_000) {
            redisAvailable = true;
            logger.info("Redis 重试标记已重置");
        }
        return redisAvailable;
    }

    private void markUnavailable(String op, Exception e) {
        redisAvailable = false;
        lastFailTime = System.currentTimeMillis();
        logger.warn("Redis {} 失败，降级到 ServletContext: {}", op, e.getMessage());
    }
}

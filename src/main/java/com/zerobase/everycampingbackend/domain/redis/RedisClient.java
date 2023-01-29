package com.zerobase.everycampingbackend.domain.redis;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisClient {
    private final RedisTemplate<String, Object> redisTemplate;

    public String getValue(String index, String key) {
        return (String) redisTemplate.opsForValue().get(index + ":" + key);
    }

    public void putValue(String index, String key, String value, long ttl) {
        redisTemplate.opsForValue()
            .set(index + ":" + key, value, ttl, TimeUnit.MILLISECONDS);
    }

    public void deleteValue(String index, String key) {
        redisTemplate.delete(index + ":" + key);
    }
}

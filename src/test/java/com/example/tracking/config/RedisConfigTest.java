package com.example.tracking.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(RedisConfig.class)
class RedisConfigTest {

    @Autowired
    private ReactiveStringRedisTemplate redisTemplate;

    @Test
    void testRedisTemplateBeanLoaded() {
        assertNotNull(redisTemplate);
    }
}

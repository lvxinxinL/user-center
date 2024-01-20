package com.example.usercenter.service;

import com.example.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

/**
 * Redis 测试
 * @author 乐小鑫
 * @version 1.0
 */
@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    void test() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 增
        valueOperations.set("ghostString", "dog");
        valueOperations.set("ghostInt", 1);
        valueOperations.set("ghostDouble", 2.0);
        User user = new User();
        user.setId(1L);
        user.setUsername("ghost");
        valueOperations.set("ghostUser", user);
        // 查
        Object ghost = valueOperations.get("ghostString");
        Assertions.assertTrue("dog".equals((String) ghost));
        ghost = valueOperations.get("ghostInt");
        Assertions.assertTrue(1 == (Integer) ghost);
        ghost = valueOperations.get("ghostDouble");
        Assertions.assertTrue(2.0 == (Double) ghost);
        System.out.println(valueOperations.get("ghostUser"));
        valueOperations.set("ghostString", "dog");
        redisTemplate.delete("ghostString");
    }
}

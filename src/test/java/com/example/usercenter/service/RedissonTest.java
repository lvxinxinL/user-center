package com.example.usercenter.service;

import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 乐小鑫
 * @version 1.0
 * @Date 2024-01-21-15:53
 */
@SpringBootTest
public class RedissonTest {

    @Resource
    private RedissonClient redissonClient;

    @Test
    void test() {
        // list
        List<String> list = new ArrayList<>();
        list.add("ghost");
        System.out.println("List:" + list.get(0));
        RList<Object> rList = redissonClient.getList("test-list");
        rList.add("ghost");
        System.out.println("rList:" + rList.get(0));
        // map

        // set
    }
}

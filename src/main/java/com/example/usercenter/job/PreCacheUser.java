package com.example.usercenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usercenter.model.domain.User;
import com.example.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存预热定时任务
 * @author 乐小鑫
 * @version 1.0
 */
@Component
@Slf4j
public class PreCacheUser {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserService userService;

    List<Long> mainUserList = Arrays.asList(3L);// 重要用户列表，为该列表的用户开启缓存预热

    @Scheduled(cron = "0 16 21 ? * * ")// 每天 21:14 执行定时任务进行用户数据缓存预热
    public void doPreCacheUser() {
        // 查出用户存到 Redis 中
        for (Long userId : mainUserList) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);// 查询所有用户
            String key = String.format("langhua:user:recommend:%s", userId);
            ValueOperations valueOperations = redisTemplate.opsForValue();
            // 将查询出来的数据写入缓存
            try {
                valueOperations.set(key,userPage,24, TimeUnit.HOURS);
            } catch (Exception e) {
                log.error("redis key set error", e);
            }
        }
    }
}

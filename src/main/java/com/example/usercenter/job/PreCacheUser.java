package com.example.usercenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usercenter.model.domain.User;
import com.example.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.usercenter.constant.enums.RedisConstant.PRE_CACHE_USER_LOCK;
import static com.example.usercenter.constant.enums.RedisConstant.USER_RECOMMEND_KEY_PREFIX;

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

    @Resource
    private RedissonClient redissonClient;

    List<Long> mainUserList = Arrays.asList(3L);// 重要用户列表，为该列表的用户开启缓存预热

    @Scheduled(cron = "0 59 21 ? * * ")// 每天 21:57 执行定时任务进行用户数据缓存预热
    public void doPreCacheUser() {
        RLock lock = redissonClient.getLock(PRE_CACHE_USER_LOCK);
        try {
            if (lock.tryLock(0,-1,TimeUnit.MILLISECONDS)) {
                log.info("get redisson lock" + Thread.currentThread().getId());
                // 查出用户存到 Redis 中
                for (Long userId : mainUserList) {
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);// 查询所有用户
                    String key = String.format(USER_RECOMMEND_KEY_PREFIX, userId);
                    ValueOperations valueOperations = redisTemplate.opsForValue();
                    // 将查询出来的数据写入缓存
                    try {
                        valueOperations.set(key,userPage,24, TimeUnit.HOURS);
                    } catch (Exception e) {
                        log.error("redis key set error", e);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("redisson precache user error", e);
        } finally {
            log.info("redisson unlock" + Thread.currentThread().getId());
            lock.unlock();
        }

    }
}

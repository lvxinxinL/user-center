package com.example.usercenter.constant.enums;

/**
 * redis key
 * @author 乐小鑫
 * @version 1.0
 * @Date 2024-01-27-15:13
 */
public class RedisConstant {

    public static final String PRE_CACHE_USER_LOCK = "langhua:precachejob:doprecache:lock";
    public static final String USER_RECOMMEND_KEY_PREFIX = "langhua:user:recommend:%s";
    public static final String JOIN_TEAM_USER_LOCK = "langhua:join_team:lock";
}

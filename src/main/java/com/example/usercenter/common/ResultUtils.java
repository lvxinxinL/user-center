package com.example.usercenter.common;

/**
 * 工具类，返回统一返回类
 * @author Ghost
 * @version 1.0
 */
public class ResultUtils {
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse(0, data, "ok");
    }
}

package com.example.usercenter.common;

import lombok.Data;

/**
 * 通用返回类
 * @author Ghost
 * @version 1.0
 */
@Data
public class BaseResponse<T> {
    private int code;// 业务状态码 0-正常

    private T data;// 数据

    private String message;// 业务处理信息

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

   public BaseResponse(int code, T data) {
        this(code, data, "");
   }
}

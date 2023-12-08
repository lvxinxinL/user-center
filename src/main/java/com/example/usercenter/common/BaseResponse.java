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


    private String description;// 详细信息

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }
    public BaseResponse(int code, T data, String msg) {
        this(code, data, msg, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMsg(), errorCode.getDescription());
    }
}
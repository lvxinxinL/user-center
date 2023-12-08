package com.example.usercenter.exception;

import com.example.usercenter.common.ErrorCode;

import javax.lang.model.type.ErrorType;

/**
 * @author Ghost
 * @version 1.0
 */
public class BusinessException extends RuntimeException{

    private final int code;
    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);// 把捕获到的异常信息 message 传给父类？
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
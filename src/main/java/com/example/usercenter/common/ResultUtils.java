package com.example.usercenter.common;


/**
 * 工具类，返回统一返回类
 * @author Ghost
 * @version 1.0
 */
public class ResultUtils {
    /**
     * 成功
     *
     * @param data 请求的数据
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse(0, data, "ok");
    }

    /**
     * 失败：支持传入 ErrorCode
     *
     * @param errorCode 错误码
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse(errorCode);
    }

    /**
     * 失败：支持自定义 code、message、description
     *
     * @param code
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse(code, null, message, description);
    }

    /**
     * 失败：支持自定义 description
     *
     * @param errorCode
     * @param description
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode, String description) {
        return new BaseResponse(errorCode.getCode(), null, errorCode.getMsg(), description);
    }

    /**
     * 失败：支持自定义 message、description
     *
     * @param errorCode
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse(errorCode.getCode(), null, message, description);
    }
}
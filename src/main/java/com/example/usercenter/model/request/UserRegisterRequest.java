package com.example.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求
 * @author Ghost
 * @version 1.0
 */
@Data
public class UserRegisterRequest implements Serializable{

    private static final long serialVersionUID = -150010351605082849L;// 序列化 id

    private String userAccount;

    private String userPassword;

    private String checkPassword;

}

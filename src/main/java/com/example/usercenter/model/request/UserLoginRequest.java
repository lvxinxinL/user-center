package com.example.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求
 * @author Ghost
 * @version 1.0
 */
@Data
public class UserLoginRequest implements Serializable{

    private static final long serialVersionUID = 6993746803531411917L;

    private String userAccount;

    private String userPassword;

}

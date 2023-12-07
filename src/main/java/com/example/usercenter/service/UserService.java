package com.example.usercenter.service;

import com.example.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author Ghost
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2023-12-02 09:29:43
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册校验
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 确认密码
     * @return 用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 登录用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 用户信息脱敏
     * @param user 未脱敏用户
     * @return 脱敏用户
     */
    User getSafetyUser(User user);

    /**
     * 用户退出登录
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);
}

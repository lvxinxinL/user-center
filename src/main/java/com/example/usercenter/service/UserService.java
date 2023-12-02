package com.example.usercenter.service;

import com.example.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 20890
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2023-12-02 09:29:43
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册校验
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 确认密码
     * @return 用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

}

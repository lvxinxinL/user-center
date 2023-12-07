package com.example.usercenter.service;

import com.example.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


/**
 * 用户服务测试
 * @author Ghost
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    /**
     * 测试添加用户功能
     */
    @Test
    void testAddUser() {
        User user = new User();

        // 设置属性
        user.setUsername("admin");
        user.setUserAccount("admin");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("18274837847");
        user.setEmail("28937485738@qq.com");

        boolean result = userService.save(user);// 返回插入结果
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    /**
     * 测试用户注册方法
     */
    @Test
    void userRegister() {
        // 密码不能为空
        String userAccount = "lucky";
        String userPassword = "";
        String checkPassword = "12345678";
        String planetCode = "1";
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        // 账户长度不能小于 4 位
        userAccount = "lu";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        // 密码不能小于 8 位
        userAccount = "lucky";
        userPassword = "123456";
        checkPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        // 账户不能重复
        userAccount = "ghost";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        // 账户不能包含特殊字符
        userAccount = "lu cky";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        // 密码和校验密码要相同
        userAccount = "lucky";
        userPassword = "12345678";
        checkPassword = "25163531";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        // 正常情况
        userAccount = "Jack";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertTrue(result > 0);
    }
}
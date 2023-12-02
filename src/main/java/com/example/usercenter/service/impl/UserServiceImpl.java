package com.example.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenter.model.domain.User;
import com.example.usercenter.service.UserService;
import com.example.usercenter.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author 20890
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2023-12-02 09:29:43
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;
    private static final String DEFAULT_SALT = "GHOST";

    /**
     * 用户注册校验
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 确认密码
     * @return 用户 id
     */
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1、校验
        // 用户的账户、密码、确认密码非空
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }
        // 账户长度不小于 4 位
        if(userAccount.length() < 4) {
            return -1;
        }
        // 密码就不小于 8 位
        if(userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }

        // 账户不包含特殊字符
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccount);
        if(matcher.find()) {// 如果匹配到了特殊字符就直接返回 -1
            return -1;
        }

        // 密码和校验密码相同
        if(!userPassword.equals(checkPassword)) {
            return -1;
        }

        // 账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
//        this.selectCount(queryWrapper);// this 是 UserServiceImpl，继承了ServiceImpl，可以使用里面的方法
        userMapper.selectCount(queryWrapper);

        // 2、对用户密码进行加密
        String digestPassword = DigestUtils.md5DigestAsHex((DEFAULT_SALT + userPassword).getBytes());

        // 3、将用户数据插入数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(digestPassword);
        boolean saveResult = this.save(user);
        if(!saveResult) {
            return -1;
        }
        return user.getId();
    }
}





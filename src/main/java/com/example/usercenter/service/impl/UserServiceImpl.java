package com.example.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenter.model.domain.User;
import com.example.usercenter.service.UserService;
import com.example.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author Ghost
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2023-12-02 09:29:43
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 加密的盐值
     */
    private static final String DEFAULT_SALT = "GHOST";

    /**
     * 用户登录态键
     */
    private static final String User_LOGIN_STATE = "userLoginState";

    /**
     * 用户注册校验
     * @param userAccount 用户账户
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
        Long result = userMapper.selectCount(queryWrapper);
        if(result > 0) {
            log.info("注册失败");
            return -1;
        }

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

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 登录用户信息
     */
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1、校验
        // 用户的账户、密码、确认密码非空
        if(StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        // 账户长度不小于 4 位
        if(userAccount.length() < 4) {
            return null;
        }
        // 密码就不小于 8 位
        if(userPassword.length() < 8) {
            return null;
        }

        // 账户不包含特殊字符
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccount);
        if(matcher.find()) {// 如果匹配到了特殊字符就直接返回 -1
            return null;
        }

        // 对用户密码进行加密
        String digestPassword = DigestUtils.md5DigestAsHex((DEFAULT_SALT + userPassword).getBytes());

        // 2、查询数据库中的密文密码进行校验
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password", digestPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在或账户密码不匹配
        if(user == null) {
            log.info("登录失败");
            return null;
        }

        // 3、返回用户信息（脱敏）
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setIsDelete(user.getIsDelete());

        // 4、记录用户的登录态
        request.getSession().setAttribute(User_LOGIN_STATE, safetyUser);

        return safetyUser;// 返回脱敏后的用户
    }
}





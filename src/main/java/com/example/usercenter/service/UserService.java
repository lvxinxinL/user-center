package com.example.usercenter.service;

import com.example.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.example.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.example.usercenter.constant.UserConstant.USER_LOGIN_STATE;

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
     * @param planetCode 星球编号
     * @return 用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);

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

    /**
     * 根据标签搜索用户
     * @param tagNameList 用户的标签列表
     * @return 匹配该标签列表的用户列表
     */
    List<User> searchUsersByTags(List<String> tagNameList);

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 修改用户信息
     * @param user
     * @param loginUser
     * @return
     */
    int updateUser(User user, User loginUser);


    /**
     * 判断用户是否为管理员
     *
     * @param request 登录态
     * @return 是否为管理员
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 判断用户是否为管理员
     *
     * @param loginUser 当前登录用户
     * @return 是否为管理员
     */
    boolean isAdmin(User loginUser);
}

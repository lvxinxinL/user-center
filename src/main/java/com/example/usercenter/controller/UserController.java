package com.example.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.usercenter.model.domain.User;
import com.example.usercenter.model.request.UserLoginRequest;
import com.example.usercenter.model.request.UserRegisterRequest;
import com.example.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.example.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户 Controller
 * @author Ghost
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest 用户注册请求体
     * @return 用户 id
     */
    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if(userRegisterRequest == null) {
            return null;
        }

        log.info("用户请求注册：{}", userRegisterRequest.getUserAccount());

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();

        // 校验登录信息是否为空（Controller 也要进行校验，单纯的请求参数本身的校验）
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            return null;
        }

        return userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
    }


    /**
     * 用户登录
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if(userLoginRequest == null) {
            return null;
        }

        log.info("用户请求登录：{}", userLoginRequest.getUserAccount());

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        // 校验登录信息是否为空（Controller 也要进行校验，单纯的请求参数本身的校验）
        if(StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        return userService.userLogin(userAccount, userPassword, request);
    }


    /**
     * 用户注销
     * @param request
     * @return 退出登录成功返回 1
     */
    @PostMapping("/logout")
    public Integer userLogout(HttpServletRequest request) {
         if(request == null) {
             return null;
         }
         return userService.userLogout(request);
    }

    /**
     * 获取当前用户信息
     * @param request HttpServletRequest
     * @return 当前登录用户信息
     */
    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);// 获取用户登录态
        User currentUser = (User) userObj;
        Long userId = currentUser.getId();

        log.info("获取当前登录用户信息：{}", userId);

        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        return userService.getSafetyUser(user);
    }

    /**
     * 用户管理：查询用户（模糊匹配）
     * @param username 用户名
     * @return 查询到的用户列表
     */
    @GetMapping("/search")
    public List<User> searchUser(String username, HttpServletRequest request) {
        log.info("根据用户名查找用户 / 加载用户列表");
        if(!isAdmin(request)) {// 如果不是管理员，不能查询用户
            return new ArrayList<>();
        }

        // 查询构造器
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);// 根据用户名进行模糊查询
        }

        List<User> userList = userService.list(queryWrapper);
        // 返回脱敏后的用户数据
        return userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
    }

    /**
     * 用户管理：根据 id 删除用户
     * @param id 用户 id
     * @return 是否删除成功
     */
    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request) {
        log.info("根据用户 id 删除用户：{}", id);
        if(!isAdmin(request)) {// 如果不是管理员，不能删除用户
            return false;
        }

        if(id <= 0) {
            return false;
        }

        return userService.removeById(id);// 根据 id 逻辑删除
    }

    /**
     * 判断用户是否为管理员
     * @param request 登录态
     * @return 是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        log.info("判断用户是否为管理员");
        // 仅管理员可查询用户——鉴权
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}

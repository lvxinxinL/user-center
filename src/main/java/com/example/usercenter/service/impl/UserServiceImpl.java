package com.example.usercenter.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenter.common.ErrorCode;
import com.example.usercenter.exception.BusinessException;
import com.example.usercenter.model.domain.User;
import com.example.usercenter.service.UserService;
import com.example.usercenter.mapper.UserMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.ehcache.impl.internal.resilience.RobustResilienceStrategy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.BufferUnderflowException;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.example.usercenter.constant.UserConstant.USER_LOGIN_STATE;

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
     * 用户注册校验
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 确认密码
     * @param planetCode 星球编号
     * @return 用户 id
     */
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        // 1、校验
        // 用户的账户、密码、确认密码非空
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 账户长度不小于 4 位
        if(userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度过短");
        }
        // 密码不小于 8 位
        if(userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度过短");
        }

        // 星球编号不大于 5 位
        if(planetCode.length() > 5) {
            log.info("星球编号过长");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号过长");
        }

        // 账户不包含特殊字符
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccount);
        if(matcher.find()) {// 如果匹配到了特殊字符就直接返回 -1
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号包含特殊字符");
        }

        // 密码和校验密码相同
        if(!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入密码不一致");
        }

        // 账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
//        this.selectCount(queryWrapper);// this 是 UserServiceImpl，继承了ServiceImpl，可以使用里面的方法
        Long result = userMapper.selectCount(queryWrapper);
        if(result > 0) {
            log.info("账号重复，注册失败");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }

        // 星球编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planet_code", planetCode);
        result = userMapper.selectCount(queryWrapper);
        if(result > 0) {
            log.info("星球编号重复，注册失败");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号重复");
        }

        // 2、对用户密码进行加密
        String digestPassword = DigestUtils.md5DigestAsHex((DEFAULT_SALT + userPassword).getBytes());

        // 3、将用户数据插入数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(digestPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);
        if(!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统异常，注册失败");
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
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 账号长度不小于 4 位
        if(userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度过短");
        }
        // 密码不小于 8 位
        if(userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度过短");
        }

        // 账户不包含特殊字符
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccount);
        if(matcher.find()) {// 如果匹配到了特殊字符就直接返回 -1
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号包含特殊字符");
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
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或账号密码不匹配");
        }

        // 3、返回用户信息（脱敏）
        User safetyUser = getSafetyUser(user);

        // 4、记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);

        return safetyUser;// 返回脱敏后的用户
    }

    /**
     * 用户信息脱敏
     * @param user 未脱敏用户
     * @return 脱敏用户
     */
    public User getSafetyUser(User user) {
        if(user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
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
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setPlanetCode(user.getPlanetCode());
        safetyUser.setTags(user.getTags());
        return safetyUser;
    }

    /**
     * 用户退出登录
     * @param request
     * @return
     */
    public int userLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 根据标签搜索用户（内存过滤）
     * @param tagNameList 用户的标签列表
     * @return 匹配该标签列表的用户列表
     */
    public List<User> searchUsersByTags(List<String> tagNameList) {
        // 1. 判断参数是否为空
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 1. 查询所有用户
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        List<User> userList = userMapper.selectList(queryWrapper);

        Gson gson = new Gson();
        return userList.stream().filter(user -> {
            // 2. 遍历用户取出标签信息
            String tagsStr = user.getTags();
            if (StringUtils.isEmpty(tagsStr)) {
                return false;
            }
            // 3. 在内存中判断是否包含要求标签
            Set<String> tempTagsNameSet = gson.fromJson(tagsStr, new TypeToken<Set<String>>(){}.getType());
            for (String tagName : tagNameList) {
                if (!tempTagsNameSet.contains(tagName)) {// 用户标签不包含所要求的标签
                    return false;
                }
            }
            return true;
        }).map(this::getSafetyUser).collect(Collectors.toList());
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断用户登录态
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return (User) userObj;
    }

    /**
     * 更新用户信息
     * @param user 要更新的用户
     * @param loginUser 当前登录用户
     * @return
     */
    @Override
    public int updateUser(User user, User loginUser) {
        // 判断权限：只有管理员和自己可以修改用户信息
        if (user == null || loginUser == null || !isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        long userId = user.getId();
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 管理员：更新任意用户的信息
        // 普通用户：只允许更新自己的信息
        if (!isAdmin(loginUser) && userId == loginUser.getId()) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        User oldUser = userMapper.selectById(userId);
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userMapper.updateById(user);
    }

    /**
     * 判断用户是否为管理员
     *
     * @param loginUser 当前登录用户
     * @return 是否为管理员
     */
    public boolean isAdmin(User loginUser) {
        return loginUser != null && loginUser.getUserRole() == ADMIN_ROLE;
    }

    /**
     * 判断用户是否为管理员
     *
     * @param request 登录态
     * @return 是否为管理员
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        log.info("判断用户是否为管理员");
        // 仅管理员可查询用户——鉴权
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }


    /**
     * 根据标签搜索用户（SQL 查询）
     * @param tagNameList 用户的标签列表
     * @return 匹配该标签列表的用户列表
     */
    @Deprecated
    private List<User> searchUsersByTagsBySQL(List<String> tagNameList) {
        // 1. 判断参数是否为空
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2. 构造查询器
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        for (String tagName : tagNameList) {
            queryWrapper = queryWrapper.like("tags", tagName);
        }

        // 3. 根据标签列表查找用户
        List<User> userList = userMapper.selectList(queryWrapper);

        // 4. 返回脱敏后的用户列表
        return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());
    }
}





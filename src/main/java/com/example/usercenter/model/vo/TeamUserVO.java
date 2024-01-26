package com.example.usercenter.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 队伍和用户信息返回封装类（脱敏）
 * @author 乐小鑫
 * @version 1.0
 * @Date 2024-01-23-15:23
 */
@Data
public class TeamUserVO implements Serializable {

    private static final long serialVersionUID = -8855840933732067014L;

    /**
     * id
     */
    private Long id;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 队伍创建用户列表
     */
    UserVO createUser;

    /**
     * 用户是否已加入该队伍
     */
    private boolean hasJoin = false;
}

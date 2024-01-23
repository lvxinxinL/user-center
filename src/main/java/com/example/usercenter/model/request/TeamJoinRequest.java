package com.example.usercenter.model.request;

import lombok.Data;
import java.io.Serializable;

/**
 * 加入队伍请求参数封装类
 */
@Data
public class TeamJoinRequest implements Serializable {
    private static final long serialVersionUID = 4442151141560122843L;

    /**
     * 队伍 id
     */
    private Long teamId;

    /**
     * 密码
     */
    private String password;
}
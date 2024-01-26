package com.example.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 退出队伍请求参数封装类
 */
@Data
public class TeamQuitRequest implements Serializable {
    private static final long serialVersionUID = 4442151141560122843L;

    /**
     * 队伍 id
     */
    private Long id;
}
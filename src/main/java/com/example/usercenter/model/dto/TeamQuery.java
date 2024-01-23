package com.example.usercenter.model.dto;

import com.example.usercenter.common.PageRequest;
import lombok.Data;

import java.util.List;


/**
 * @author 乐小鑫
 * @version 1.0
 * @Date 2024-01-22-20:14
 */
@Data
public class TeamQuery extends PageRequest {
    private static final long serialVersionUID = -8434935321943948180L;

    /**
     * id
     */
    private Long id;

    /**
     * idList
     */
    private List<Long> idList;

    /**
     * 查询关键词（同时查询 name 和 description）
     */
    private String searchText;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 队伍描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;
}

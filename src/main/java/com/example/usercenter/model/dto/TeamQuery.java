package com.example.usercenter.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.usercenter.common.PageRequest;
import lombok.Data;


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
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 队伍名称
     */
    private String name;

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

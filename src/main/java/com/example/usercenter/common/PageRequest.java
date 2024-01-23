package com.example.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用分页查询参数
 * @author 乐小鑫
 * @version 1.0
 * @Date 2024-01-22-20:16
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 1395844225639844641L;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 当前页数
     */
    private int pageNum = 1;

}

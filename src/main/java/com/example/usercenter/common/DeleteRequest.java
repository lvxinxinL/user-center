package com.example.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用删除 id
 * @author 乐小鑫
 * @version 1.0
 * @Date 2024-01-22-20:16
 */
@Data
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = 1395844225639844641L;

    private long id;

}

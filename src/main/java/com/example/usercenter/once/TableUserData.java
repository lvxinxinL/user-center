package com.example.usercenter.once;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 用户信息实体对象：与 Excel 字段对应
 * @author Ghost
 * @version 1.0
 */
@Data
public class TableUserData {

    @ExcelProperty("用户名")
    private String username;

    @ExcelProperty("星球编号")
    private String planetCode;

}

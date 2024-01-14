package com.example.usercenter.once;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 导入用户数据到数据库
 * @author Ghost
 * @version 1.0
 */
@Slf4j
public class ImportUser2DB {
    public static void main(String[] args) {
        String fileName = "D:\\code\\user-center\\src\\main\\resources\\testUser.xls";// 先写一个绝对路径
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<TableUserData> userInfoList =
                EasyExcel.read(fileName).head(TableUserData.class).sheet().doReadSync();

        // 判重
        System.out.println("用户总数：" + userInfoList.size());
        // TODO 清洗数据，导入数据库
    }
}

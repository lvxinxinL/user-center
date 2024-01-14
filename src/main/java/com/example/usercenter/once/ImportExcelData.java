package com.example.usercenter.once;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 读取 Excel 表格数据
 * @author Ghost
 * @version 1.0
 */
@Slf4j
public class ImportExcelData {
    public static void main(String[] args) {
        // 写法 1
        String fileName = "D:\\code\\user-center\\src\\main\\resources\\testUser.xls";// 先写一个绝对路径
//        readByListener(fileName);
        synchronousRead(fileName);
    }

    /**
     * 读取方式一：使用监听器读取
     * @param fileName 文件路径
     */
    public static void readByListener(String fileName) {
        // 这里默认每次会读取100条数据 然后返回过来 直接调用使用数据就行
        EasyExcel.read(fileName, TableUserData.class, new DemoDataListener()).sheet().doRead();
    }

    /**
     * 读取方式二：同步读取（同步的返回，不推荐使用，如果数据量大会把数据放到内存里面）
     * @param fileName 文件路径
     */
    public static void synchronousRead(String fileName) {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<TableUserData> list = EasyExcel.read(fileName).head(TableUserData.class).sheet().doReadSync();
        for (TableUserData data : list) {
            log.info("读取到数据:{}", data);
        }
    }
}

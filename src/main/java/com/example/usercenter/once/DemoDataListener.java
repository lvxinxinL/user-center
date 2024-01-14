package com.example.usercenter.once;

import com.alibaba.excel.context.AnalysisContext;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.excel.read.listener.ReadListener;

@Slf4j
public class DemoDataListener implements ReadListener<TableUserData> {

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(TableUserData data, AnalysisContext context) {
        System.out.println(data);// 输出每次解析到的数据
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("所有数据解析完成！");
    }
}
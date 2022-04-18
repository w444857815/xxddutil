package com.dxhy.order.thread;

import cn.hutool.core.date.DateUtil;
import com.dxhy.order.service.ApiWankeService;
import com.dxhy.order.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @ClassName 要传多少参数就在这里建多少属性
 * @Description TODO
 * @Author wangruwei
 * @Date 2022/3/30 16:49
 * @Version 1.0
 */
@Slf4j
public class PrintDateThread implements Runnable {


    private String jsonStr ;


    public PrintDateThread(String jsonStr
    ) {
        this.jsonStr = jsonStr;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("获取主线程传来的参数:{},线程的日期:{}",jsonStr, DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));

    }

}


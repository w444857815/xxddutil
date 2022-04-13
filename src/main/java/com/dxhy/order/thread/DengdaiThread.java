package com.dxhy.order.thread;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @ClassName 要传多少参数就在这里建多少属性
 * @Description TODO
 * @Author wangruwei
 * @Date 2022/3/30 16:49
 * @Version 1.0
 */
@Slf4j
public class DengdaiThread implements Runnable {


    private String jsonStr ;

    private CyclicBarrier cyclicBarrier;


    public DengdaiThread(String jsonStr,
                         CyclicBarrier cyclicBarrier
    ) {
        this.jsonStr = jsonStr;
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        log.info("准备线程:{}",jsonStr);
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        log.info("获取主线程传来的参数:{},线程的日期:{}",jsonStr, DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
    }

}


package com.dxhy.order.thread;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * @ClassName 要传多少参数就在这里建多少属性
 * @Description TODO
 * @Author wangruwei
 * @Date 2022/3/30 16:49
 * @Version 1.0
 */
@Slf4j
public class WangzheReadyThread implements Runnable {
    private int wanjiaNum;

    private CountDownLatch countDownLatch;

    private Set<String> set;

    public WangzheReadyThread(
            int wanjiaNum,
            CountDownLatch countDownLatch,
            Set<String> set
    ) {
        this.wanjiaNum = wanjiaNum;
        this.countDownLatch = countDownLatch;
        this.set = set;
    }

    @Override
    public void run() {
        log.info("玩家{}正在准备",wanjiaNum);
        long i = new Random().nextInt(5)*1000;
        try {
            Thread.sleep(i);
            log.info("玩家{}准备完成，耗时{}秒",wanjiaNum,i/1000);
            set.add("玩家"+wanjiaNum+"完成");
            countDownLatch.countDown();
        } catch (InterruptedException e) {
        //} catch (Exception e) {
            e.printStackTrace();
        }

    }

}


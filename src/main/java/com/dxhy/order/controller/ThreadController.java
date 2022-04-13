package com.dxhy.order.controller;

import com.dxhy.order.model.OrderInfo;
import com.dxhy.order.service.ApiWankeService;
import com.dxhy.order.thread.DengdaiThread;
import com.dxhy.order.thread.PrintDateThread;
import com.dxhy.order.thread.QueryArticleThread;
import com.dxhy.order.thread.WangzheReadyThread;
import com.dxhy.order.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName RedisKucunController
 * @Description TODO
 * @Author wangruwei
 * @Date 2022/2/25 16:05
 * @Version 1.0
 */
@RestController
@RequestMapping("/thread")
@Slf4j
public class ThreadController extends BaseController{

    @Autowired
    private ApiWankeService wankeService;


    /**
    * @Description 执行业务逻辑后新建线程发起其他请求
    * @Return java.util.Map<java.lang.String,java.lang.Object>
    * @Author wangruwei
    * @Date 2022/4/8 14:05
    **/
    @RequestMapping(value = "zhuanJson" )
    public Map<String,Object> zhuanJson() {
        OrderInfo oi = new OrderInfo();
        oi.setDdh("订单号");
        oi.setSldMc("开票点名称");
        oi.setFpqqlsh("流水号");
        oi.setDdrq(new Date());
        oi.setXhfMc("销货方名称");
        oi.setXhfNsrsbh("销方识别号");
        String s = JsonUtils.getInstance().toJsonString(oi);
        log.info("转化出来的数据：{}",s);

        /**
        * @Description 需要耗时或与主业务无关的业务放线程里
        **/
        log.info("新建线程开始");
        QueryArticleThread eleKpPostInvoiceThread = new QueryArticleThread(s,wankeService);
        Thread thread = new Thread(eleKpPostInvoiceThread);
        thread.start();
        log.info("新建线程结束");

        return getSussRtn(s, "执行成功");
    }

    /**
    * @Description 简单线程池
    * @Author wangruwei
    * @Date 2022/4/12 17:30
    **/
    @RequestMapping(value = "simpleThreadPool" )
    public Map<String,Object> simpleThreadPool() {
        //定义线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        String str = "主线程传参";
        //执行线程
        log.info("放入线程池执行");
        for (int i = 0; i < 40; i++) {
            PrintDateThread printDateThread = new PrintDateThread(str);
            executorService.submit(printDateThread);
        }
        log.info("放入线程池完成");
        executorService.shutdown();
        return getSussRtn("", "放入线程池执行成功");
    }


    /**
    * @Description 等待创建完特定数量20个线程后再执行
    * @param
    * @Return java.util.Map<java.lang.String,java.lang.Object>
    * @Author wangruwei
    * @Date 2022/4/12 19:22
    **/
    @RequestMapping(value = "dengdaiThread" )
    public Map<String,Object> dengdaiThread() {
        //定义CyclicBarrier 的屏障，需要等多少个线程到了才发起请求
        CyclicBarrier cyclicBarrier = new CyclicBarrier(20);

        String str = "主线程传参";
        //执行线程
        log.info("新建线程开始");
        //这里i的数量大于20的时候，有些线程会先于等待准备的线程先执行。
        //数量一样的时候，就会都准备好，然后统一执行，在线程里要先await后执行线程逻辑代码
        for (int i = 0; i < 20; i++) {
            DengdaiThread printDateThread = new DengdaiThread(i+"",cyclicBarrier);
            Thread t = new Thread(printDateThread);
            t.start();
        }
        log.info("新建线程结束");

        return getSussRtn("", "放入线程池执行成功");
    }


    /**
     * @Description 等待创建完特定数量20个线程后再执行
     * @param
     * @Return java.util.Map<java.lang.String,java.lang.Object>
     * @Author wangruwei
     * @Date 2022/4/12 19:22
     **/
    @RequestMapping(value = "dengdaiThreadPool" )
    public Map<String,Object> dengdaiThreadPool() {
        //定义CyclicBarrier 的屏障，需要等多少个线程到了才发起请求
        CyclicBarrier cyclicBarrier = new CyclicBarrier(20);

        //定义线程池
        ExecutorService executorService = Executors.newFixedThreadPool(30);

        String str = "主线程传参";
        //执行线程
        log.info("放入线程池开始");
        //这里i的数量大于20的时候，有些线程会先于等待准备的线程先执行。
        //数量一样的时候，就会都准备好，然后统一执行，在线程里要先await后执行线程逻辑代码
        for (int i = 0; i < 20; i++) {
            DengdaiThread printDateThread = new DengdaiThread(i+"",cyclicBarrier);
            executorService.submit(printDateThread);
        }
        log.info("放入线程池结束");
        executorService.shutdown();

        return getSussRtn("", "放入线程池执行成功");
    }


    /**
    * @Description 等待新建线程都执行完后，再执行主线程。就像开王者一样，所有人都准备了，主线程才开始游戏
    * @param
    * @Return java.util.Map<java.lang.String,java.lang.Object>
    * @Author wangruwei
    * @Date 2022/4/13 10:23
    **/
    @RequestMapping(value = "countDownThread" )
    public Map<String,Object> countDownThread() {

        CountDownLatch countDownLatch = new CountDownLatch(10);

        //线程池一样的写法
        //ExecutorService executorService = Executors.newFixedThreadPool(30);

        Set<String> set = new HashSet<>();
        log.info("进入准备界面");
        for (int i = 0; i < 10; i++) {
            WangzheReadyThread wz = new WangzheReadyThread(i,countDownLatch,set);
            //线程池一样的写法
            //executorService.submit(wz);
            Thread t = new Thread(wz);
            t.start();
        }
        try {
            //等待所有线程都执行完再执行主线程
            countDownLatch.await();
            log.info("所有玩家准备成功，开始游戏。\n所有线程处理完返回的总数据:{}",JsonUtils.getInstance().toJsonString(set));
        } catch (InterruptedException e) {
            e.printStackTrace();
            return getFailRtn("执行失败");
        }
        //线程池一样的写法
        //executorService.shutdown();

        return getSussRtn("", "放入线程池执行成功");
    }



}

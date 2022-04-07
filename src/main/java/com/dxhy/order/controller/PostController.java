package com.dxhy.order.controller;

import com.dxhy.order.model.*;
import com.dxhy.order.service.*;
import com.dxhy.order.util.HttpUtils;
import com.dxhy.order.util.JsonUtils;
import com.dxhy.order.util.sql;
import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName OrderController
 * @Description TODO
 * @Author wangruwei
 * @Date 2020-10-13 11:36
 * @Version 1.0
 */
@Slf4j
@Controller
@RequestMapping("/dopost")
public class PostController {

    //定义CyclicBarrier 的屏障，需要等多少个线程到了才发起请求
    CyclicBarrier cyclicBarrier = new CyclicBarrier(200);

    private final static Executor executor = Executors.newCachedThreadPool();//启用多线程

    @GetMapping("/dopost")
    public String dopost(String num){
        int cishu = Integer.parseInt(num);
        /*for (int i=0;i<cishu;i++){
            String url = "http://dzfpfw-pre.ele-cloud.com/accept/invoice/V2.0/invoiceIssue";
            String requestParam = "{\"checkCode\":\"B010\",\"data\":\"H4sIAAAAAAAAAAFwA4/8xX54AwQtqCBIFdgTashS4pYpJBrjxb3gH68Jm9ZvRnLwk1Nl0vLzWt6SoOHHfn/ozO9EYMd6G4rZ7LJzWrSuhhZ51Pv+RUqDvqhkbVP82U3YfGSv6HCCzaxu7m8ft96jooWa42zX2nSCS3v9wBvuFtMl/L6mQt2WkpbQlgInL+5X9ezdO45mVBgPergeUNmHaH3xfEuDqEAamJcmLRiF6VrtDwafLsbUtaZ+bIPlPDM6vODVMRaj7EuX03W3YVF3rk2EXuiVO4/NlYf8YRX0Ht4diVpvqPRJJkvxwrB2eXFzVxdJtGzfl1oQB0WHXFBKJkvxwrB2eXEYxdlkGNMIO9hOgzkaOxWZ41BggbtHKHqKs85TScSdLuqHnwdKEDB4PNqEEmP8tndjjNxs4bfuwhRdRfwZHNchqBH1kdOwdLGdE7eWu+O6aGHhkYg7buVhNwGq9CY30uesFwOJs4AFAC1WByA/ypOuVTPOoCDqUBAO5UV+71cpgxMho3nKy90DZVpZyVCXvNVeE/UUeSFj3bNra/E3Tv1cMbr51RvezlcbhWuNcWhabKMhiqMyG5BC41+y5gpp93OL6fGzsshlCezRa5Z9tvUfK7FJEOehFwB9jmzULS9x1oQ+6k/28twUzP1+DNtvJK7GF0jRYRJBlFm1uSZR1OdRUlayC0ukRlRuBNV0c9C2tVENgJh9+Qb8sPOfTY0pP3Ft2AGbz4x+PgIfybZM5UUqKceqdYEt8L/L0U2sZTkEQ8yo+3zuvcGbOIxWMpuTD0ax92Z2o1vkBq8dlZKoLFDzSxZjxtG4tP2wc2lBgeuT3hpcTgZqlZrKEcwlq6jyi/3edEw9RWl1THC3RJw9hJpT7OCnmi+9OFzekqDhx35/6KGekQsYvqRpsDqAMeXAuLau1Ebm6A8RBV48et351umd3ktWwcIa1MXUtQrnAvSX1MGikgXEsxPllkw8lscE1exsN2iPzHxKQwTWOnNqRvTcerqwygSSw8E2NnXFKs4CiUod5eTZkYZObt3rlw7KjT2kH+dtW+0eZ/fOer/+LK7ICr4wqX29aLIFaYNkjfwFUewJg4D6Y8IX9ke/WvoQPQhLGwFe++kTQirUOBYebE75dmpEsUeKm0j6/loHceQDZGPugLtgZ8RGLezqfqc+E0TmjrbDtxNzR7ib7aNwAwAA\",\"dataExchangeId\":\"668985576993734656001\",\"encryCode\":\"2\",\"key\":\"1660363994meWgwxyaBzFTbXXbuuM5mg==\",\"machineNo\":\"\",\"machineType\":\"C48\",\"returnCode\":\"0000\",\"returnMsg\":\"\",\"taxpayerNo\":\"110101MYJ2GPQQ4\",\"terminalCode\":\"0\",\"version\":\"1.3\"}";
            log.info("请求信息:{}",requestParam);
            String result = HttpUtils.doPost(url, requestParam);
            log.info("结果是:{}",result);
        }*/

        for(int i=0;i<=cishu;i++){
            final int j=i;  //关键是这一句代码，将 i 转化为  j，这样j 还是final类型的参与线程
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        String url = "http://dzfpfw-pre.ele-cloud.com/accept/invoice/V2.0/invoiceIssue";
                        String requestParam = "{\"checkCode\":\"B010\",\"data\":\"H4sIAAAAAAAAAAFwA4/8xX54AwQtqCBIFdgTashS4pYpJBrjxb3gH68Jm9ZvRnLwk1Nl0vLzWt6SoOHHfn/ozO9EYMd6G4rZ7LJzWrSuhhZ51Pv+RUqDvqhkbVP82U3YfGSv6HCCzaxu7m8ft96jooWa42zX2nSCS3v9wBvuFtMl/L6mQt2WkpbQlgInL+5X9ezdO45mVBgPergeUNmHaH3xfEuDqEAamJcmLRiF6VrtDwafLsbUtaZ+bIPlPDM6vODVMRaj7EuX03W3YVF3rk2EXuiVO4/NlYf8YRX0Ht4diVpvqPRJJkvxwrB2eXFzVxdJtGzfl1oQB0WHXFBKJkvxwrB2eXEYxdlkGNMIO9hOgzkaOxWZ41BggbtHKHqKs85TScSdLuqHnwdKEDB4PNqEEmP8tndjjNxs4bfuwhRdRfwZHNchqBH1kdOwdLGdE7eWu+O6aGHhkYg7buVhNwGq9CY30uesFwOJs4AFAC1WByA/ypOuVTPOoCDqUBAO5UV+71cpgxMho3nKy90DZVpZyVCXvNVeE/UUeSFj3bNra/E3Tv1cMbr51RvezlcbhWuNcWhabKMhiqMyG5BC41+y5gpp93OL6fGzsshlCezRa5Z9tvUfK7FJEOehFwB9jmzULS9x1oQ+6k/28twUzP1+DNtvJK7GF0jRYRJBlFm1uSZR1OdRUlayC0ukRlRuBNV0c9C2tVENgJh9+Qb8sPOfTY0pP3Ft2AGbz4x+PgIfybZM5UUqKceqdYEt8L/L0U2sZTkEQ8yo+3zuvcGbOIxWMpuTD0ax92Z2o1vkBq8dlZKoLFDzSxZjxtG4tP2wc2lBgeuT3hpcTgZqlZrKEcwlq6jyi/3edEw9RWl1THC3RJw9hJpT7OCnmi+9OFzekqDhx35/6KGekQsYvqRpsDqAMeXAuLau1Ebm6A8RBV48et351umd3ktWwcIa1MXUtQrnAvSX1MGikgXEsxPllkw8lscE1exsN2iPzHxKQwTWOnNqRvTcerqwygSSw8E2NnXFKs4CiUod5eTZkYZObt3rlw7KjT2kH+dtW+0eZ/fOer/+LK7ICr4wqX29aLIFaYNkjfwFUewJg4D6Y8IX9ke/WvoQPQhLGwFe++kTQirUOBYebE75dmpEsUeKm0j6/loHceQDZGPugLtgZ8RGLezqfqc+E0TmjrbDtxNzR7ib7aNwAwAA\",\"dataExchangeId\":\"668985576993734656001\",\"encryCode\":\"2\",\"key\":\"1660363994meWgwxyaBzFTbXXbuuM5mg==\",\"machineNo\":\"\",\"machineType\":\"C48\",\"returnCode\":\"0000\",\"returnMsg\":\"\",\"taxpayerNo\":\"110101MYJ2GPQQ4\",\"terminalCode\":\"0\",\"version\":\"1.3\"}";
                        log.info("请求信息:{}",requestParam);
                        String result = HttpUtils.doPost(url, requestParam);
                        log.info("结果是:{}",result);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }



        return "完成";
    }


    @GetMapping("/dothreadpool")
    public String dothreadpool(String num){
        runThread(num);
        return "请求完成";
    }

    private void runThread(String num) {
        int n = Integer.parseInt(num);
        //定义线程池
        ExecutorService executorService = Executors.newFixedThreadPool(n);
        //执行线程
        for (int i = 0; i < n; i++) {
            executorService.submit(buildThread(i));
        }
        executorService.shutdown();
    }

    private Thread buildThread(int i) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info("Thread：" + Thread.currentThread().getName() + "准备...");
                    //cyclicBarrier一定要等到满200个线程到了才往后执行
                    cyclicBarrier.await();
                    log.info("Thread：" + Thread.currentThread().getName() + "开始执行");
                    //do something
                    String url = "http://dzfpfw-pre.ele-cloud.com/accept/invoice/V2.0/invoiceIssue";
                    String requestParam = "{\"checkCode\":\"B010\",\"data\":\"H4sIAAAAAAAAAAFwA4/8xX54AwQtqCBIFdgTashS4pYpJBrjxb3gH68Jm9ZvRnLwk1Nl0vLzWt6SoOHHfn/ozO9EYMd6G4rZ7LJzWrSuhhZ51Pv+RUqDvqhkbVP82U3YfGSv6HCCzaxu7m8ft96jooWa42zX2nSCS3v9wBvuFtMl/L6mQt2WkpbQlgInL+5X9ezdO45mVBgPergeUNmHaH3xfEuDqEAamJcmLRiF6VrtDwafLsbUtaZ+bIPlPDM6vODVMRaj7EuX03W3YVF3rk2EXuiVO4/NlYf8YRX0Ht4diVpvqPRJJkvxwrB2eXFzVxdJtGzfl1oQB0WHXFBKJkvxwrB2eXEYxdlkGNMIO9hOgzkaOxWZ41BggbtHKHqKs85TScSdLuqHnwdKEDB4PNqEEmP8tndjjNxs4bfuwhRdRfwZHNchqBH1kdOwdLGdE7eWu+O6aGHhkYg7buVhNwGq9CY30uesFwOJs4AFAC1WByA/ypOuVTPOoCDqUBAO5UV+71cpgxMho3nKy90DZVpZyVCXvNVeE/UUeSFj3bNra/E3Tv1cMbr51RvezlcbhWuNcWhabKMhiqMyG5BC41+y5gpp93OL6fGzsshlCezRa5Z9tvUfK7FJEOehFwB9jmzULS9x1oQ+6k/28twUzP1+DNtvJK7GF0jRYRJBlFm1uSZR1OdRUlayC0ukRlRuBNV0c9C2tVENgJh9+Qb8sPOfTY0pP3Ft2AGbz4x+PgIfybZM5UUqKceqdYEt8L/L0U2sZTkEQ8yo+3zuvcGbOIxWMpuTD0ax92Z2o1vkBq8dlZKoLFDzSxZjxtG4tP2wc2lBgeuT3hpcTgZqlZrKEcwlq6jyi/3edEw9RWl1THC3RJw9hJpT7OCnmi+9OFzekqDhx35/6KGekQsYvqRpsDqAMeXAuLau1Ebm6A8RBV48et351umd3ktWwcIa1MXUtQrnAvSX1MGikgXEsxPllkw8lscE1exsN2iPzHxKQwTWOnNqRvTcerqwygSSw8E2NnXFKs4CiUod5eTZkYZObt3rlw7KjT2kH+dtW+0eZ/fOer/+LK7ICr4wqX29aLIFaYNkjfwFUewJg4D6Y8IX9ke/WvoQPQhLGwFe++kTQirUOBYebE75dmpEsUeKm0j6/loHceQDZGPugLtgZ8RGLezqfqc+E0TmjrbDtxNzR7ib7aNwAwAA\",\"dataExchangeId\":\"668985576993734656001\",\"encryCode\":\"2\",\"key\":\"1660363994meWgwxyaBzFTbXXbuuM5mg==\",\"machineNo\":\"\",\"machineType\":\"C48\",\"returnCode\":\"0000\",\"returnMsg\":\"\",\"taxpayerNo\":\"110101MYJ2GPQQ4\",\"terminalCode\":\"0\",\"version\":\"1.3\"}";
                    log.info("请求信息:{}",requestParam);
                    String result = HttpUtils.doPost(url, requestParam);
                    log.info("结果是:{}",result);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setName("name:" + i);
        return thread;
    }

}

package com.dxhy.order.thread;

import com.dxhy.order.service.ApiWankeService;
import com.dxhy.order.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName 要传多少参数就在这里建多少属性
 * @Description TODO
 * @Author wangruwei
 * @Date 2022/3/30 16:49
 * @Version 1.0
 */
@Slf4j
public class QueryArticleThread implements Runnable {


    private String jsonStr ;

    //想传什么都可以，impl类直接传来用就行
    //private MakeOutAnInvoiceServiceImpl makeOutAnInvoiceServiceImpl;

    // service和 impl均可
    private ApiWankeService wankeService;



    public QueryArticleThread(final String jsonStr,
                              final ApiWankeService wankeService
    ) {
        this.jsonStr = jsonStr;
        this.wankeService = wankeService;
    }

    @Override
    public void run() {

        try {
            log.info("打印获取数据库线程开始");
            log.info("获取到的字符传参:{}。从wankeService获取到的数据:{}",jsonStr, JsonUtils.getInstance().toJsonString(wankeService.selectArtilesList("", "5", "1"))
            );
            log.info("打印获取数据库线程结束");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}


package com.dxhy.order.controller;

import com.dxhy.order.service.RedisService;
import com.dxhy.order.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName RedisKucunController
 * @Description TODO
 * @Author wangruwei
 * @Date 2022/2/25 16:05
 * @Version 1.0
 */
@RestController
public class RedisKucunController extends BaseController{
    @Autowired
    private StockService stockService;

    @Autowired
    private RedisService redisService;

    @RequestMapping(value = "jian", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String,Object> jian() {
        Map<String,Object> hh = stockService.jian(1);
        return hh;
    }

    @RequestMapping(value = "chushi", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String,Object> chushi() {
        boolean initNum = redisService.set("kucun", "10");
        if(initNum){
            return getSussRtn("", "初始化库存为10成功");
        }else {
            return getFailRtn("初始化失败");
        }

    }




}

package com.dxhy.order.service;

import cn.hutool.core.util.ObjectUtil;
import com.dxhy.order.service.impl.BaseServiceImpl;
import com.dxhy.order.util.RedisLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName StockService
 * @Description TODO
 * @Author wangruwei
 * @Date 2022/2/25 16:08
 * @Version 1.0
 */

/**
 * 扣库存
 *
 * @author yuhao.wang
 */
@Service
public class StockService extends BaseServiceImpl {
    Logger logger = LoggerFactory.getLogger(StockService.class);

    /**
     * 不限库存
     */
    public static final long UNINITIALIZED_STOCK = -3L;

    /**
     * Redis 客户端
     */
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedisService redisService;

    /**
     * 执行扣库存的脚本
     */
    public static final String STOCK_LUA;

    static {
        /**
         *
         * @desc 扣减库存Lua脚本
         * 库存（stock）-1：表示不限库存
         * 库存（stock）0：表示没有库存
         * 库存（stock）大于0：表示剩余库存
         *
         * @params 库存key
         * @return
         *   -3:库存未初始化
         *   -2:库存不足
         *   -1:不限库存
         *   大于等于0:剩余库存（扣减之后剩余的库存）
         *      redis缓存的库存(value)是-1表示不限库存，直接返回1
         */
        StringBuilder sb = new StringBuilder();
        sb.append("if (redis.call('exists', KEYS[1]) == 1) then");
        sb.append("    local stock = tonumber(redis.call('get', KEYS[1]));");
        sb.append("    local num = tonumber(ARGV[1]);");
        sb.append("    if (stock == -1) then");
        sb.append("        return -1;");
        sb.append("    end;");
        sb.append("    if (stock >= num) then");
        sb.append("        return redis.call('incrby', KEYS[1], 0 - num);");
        sb.append("    end;");
        sb.append("    return -2;");
        sb.append("end;");
        sb.append("return -3;");
        STOCK_LUA = sb.toString();
    }


    String key = "kucun";
    //减库存
    public Map<String,Object> jian(int num) {
        Long result = null;
        // 脚本里的KEYS参数
        List<String> keys = new ArrayList<>();
        keys.add(key);
        // 脚本里的ARGV参数 num
        try {
            //调用lua脚本并执行
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
            redisScript.setResultType(Long.class);//返回类型是Long

            //redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/redis_lock4.lua")));

            redisScript.setScriptText(STOCK_LUA);
            logger.info("执行lua脚本减库存开始");
            //还有参数往后继续加
            result = redisTemplate.execute(redisScript, keys, num+"");
            logger.info("执行lua脚本减库存结束:剩余{}",result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return getSussRtn(result, "-3:库存未初始化,-2:库存不足,-1:不限库存,大于等于0:剩余库存");
    }


}

package com.dxhy.order.service;

import cn.hutool.core.util.ObjectUtil;
import com.dxhy.order.config.redis.RedisUtil;
import com.dxhy.order.service.impl.BaseServiceImpl;
import com.dxhy.order.util.RedisLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    //public static final String STOCK_LUA;

    /*static {
        *//**
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
         *//*
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

    *//**
     * @param key           库存key
     * @param expire        库存有效时间,单位秒
     * @param num           扣减数量
     * @param stockCallback 初始化库存回调函数
     * @return -2:库存不足; -1:不限库存; 大于等于0:扣减库存之后的剩余库存
     *//*
    public long stock(String key, long expire, int num, IStockCallback stockCallback) {
        long stock = stock(key, num);
        // 初始化库存
        if (stock == UNINITIALIZED_STOCK) {
            RedisLock redisLock = new RedisLock(redisTemplate, key);
            try {
                // 获取锁
                if (redisLock.lock()) {
                    // 双重验证，避免并发时重复回源到数据库
                    stock = stock(key, num);
                    if (stock == UNINITIALIZED_STOCK) {
                        // 获取初始化库存
                        final int initStock = stockCallback.getStock();
                        // 将库存设置到redis
                        redisTemplate.opsForValue().set(key, initStock, expire, TimeUnit.SECONDS);
                        // 调一次扣库存的操作
                        stock = stock(key, num);
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                redisLock.unlock();
            }

        }
        return stock;
    }

    *//**
     * 加库存(还原库存)
     *
     * @param key 库存key
     * @param num 库存数量
     * @return
     *//*
    public long addStock(String key, int num) {

        return addStock(key, null, num);
    }

    *//**
     * 加库存
     *
     * @param key    库存key
     * @param expire 过期时间（秒）
     * @param num    库存数量
     * @return
     *//*
    public long addStock(String key, Long expire, int num) {
        boolean hasKey = redisTemplate.hasKey(key);
        // 判断key是否存在，存在就直接更新
        if (hasKey) {
            return redisTemplate.opsForValue().increment(key, num);
        }

        Assert.notNull(expire, "初始化库存失败，库存过期时间不能为null");
        RedisLock redisLock = new RedisLock(redisTemplate, key);
        try {
            if (redisLock.lock()) {
                // 获取到锁后再次判断一下是否有key
                hasKey = redisTemplate.hasKey(key);
                if (!hasKey) {
                    // 初始化库存
                    redisTemplate.opsForValue().set(key, num, expire, TimeUnit.SECONDS);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            redisLock.unlock();
        }

        return num;
    }

    *//**
     * 获取库存
     *
     * @param key 库存key
     * @return -1:不限库存; 大于等于0:剩余库存
     *//*
    public int getStock(String key) {
        Integer stock = (Integer) redisTemplate.opsForValue().get(key);
        return stock == null ? -1 : stock;
    }

    *//**
     * 扣库存
     *
     * @param key 库存key
     * @param num 扣减库存数量
     * @return 扣减之后剩余的库存【-3:库存未初始化; -2:库存不足; -1:不限库存; 大于等于0:扣减库存之后的剩余库存】
     *//*
    private Long stock(String key, int num) {
        // 脚本里的KEYS参数
        List<String> keys = new ArrayList<>();
        keys.add(key);
        // 脚本里的ARGV参数
        List<String> args = new ArrayList<>();
        args.add(Integer.toString(num));

        long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                // 集群模式
                if (nativeConnection instanceof JedisCluster) {
                    return (Long) ((JedisCluster) nativeConnection).eval(STOCK_LUA, keys, args);
                }

                // 单机模式
                //else if (nativeConnection instanceof Jedis) {
                //    return (Long) ((Jedis) nativeConnection).eval(STOCK_LUA, keys, args);
                //}
                else{
                    return (Long) ((Jedis) nativeConnection).eval(STOCK_LUA, keys, args);
                }
                //return UNINITIALIZED_STOCK;
            }
        });
        return result;
    }*/

    String key = "kucun";
    //减库存
    public Map<String,Object> jian(int num) {
        RedisLock redisLock = new RedisLock(redisTemplate, key);
        try {
            //如果上锁成功，去减库存
            if(redisService.setNx("key", "锁")){
                logger.info("上锁成功，去执行减库存");
                // 获取到锁后再次判断一下是否有key
                trueJian(num);
            }else{
                //如果上锁失败，说明正在减库存，然后递归执行此方法
                logger.info("已经有锁，递归执行减库存方法");
                this.jian(num);
            }


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            redisService.unlock(key);
            //redisLock.unlock();
        }
        return getSussRtn("","");
    }

    private Map<String,Object> trueJian(int num) {
        if(Integer.parseInt(redisService.get(key).toString())<=0){
            logger.info("已经卖完");
            return getFailRtn("已经卖完");
        }else{
            //库存减一
            long decr = redisService.decr(key, 1);
            logger.info("{}库存减1成功,剩余:{}",key,decr);
            return getSussRtn(decr, "库存减一成功，剩余"+decr);
        }
    }
}

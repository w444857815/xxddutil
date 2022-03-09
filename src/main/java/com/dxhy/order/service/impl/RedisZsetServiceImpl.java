package com.dxhy.order.service.impl;

import com.dxhy.order.service.RedisZsetService;
import com.dxhy.order.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis业务实现类
 *
 * @author ZSC-DXHY
 * @date 创建时间: 2020-08-14 18:19
 */
@Service
@Component
@Slf4j
public class RedisZsetServiceImpl implements RedisZsetService {

    private static final String LOCK = "_LOCK";

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    
    private static final String LOGGER_MSG = "(redis 服务实现类)";
    
    @Override
    public Boolean expire(final String key, final int seconds) {
        return redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }
    
    @Override
    public boolean set(final String key, final String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("{},异常，异常信息：{}", LOGGER_MSG, e);
            return false;
        }
    }
    
    @Override
    public boolean set(final String key, final Object value, final int seconds) {
        String objectJson = JsonUtils.getInstance().toJsonString(value);
        return set(key, objectJson, seconds);
    }
    
    @Override
    public boolean setNx(final String key, final String value) {
    
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {
            log.error("{},异常，异常信息：{}", LOGGER_MSG, e);
            return false;
        }
    }
    
    @Override
    public boolean set(String key, String value, int seconds) {
        try {
            redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
            if (seconds > 0) {
                expire(key, seconds);
            }
            return true;
        } catch (Exception e) {
            log.error("{},异常，异常信息：{}", LOGGER_MSG, e);
            return false;
        }
    }
    
    
    @Override
    public boolean del(final String key) {
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Exception e) {
            log.error("{},异常，异常信息：{}", LOGGER_MSG, e);
            return false;
        }
    }
    
    @Override
    public <T> T get(final String key, Class<T> clazz) {
        //String value = get(key);
        String value = "";
        return StringUtils.isBlank(value) ? null : JsonUtils.getInstance().parseObject(value, clazz);
    }
    
    @Override
    public long append(String key, String value) {
        try {
            Integer append = redisTemplate.opsForValue().append(key, value);
            if (append != null) {
                return append;
            }
        
        } catch (Exception e) {
            log.error("{},异常，异常信息：{}", LOGGER_MSG, e.getMessage(), e);
        }
        return 0L;
    }
    
    @Override
    public Set<String> get(final String key) {
        if (key == null) {
            return null;
        }
        //zrevrank
        return redisTemplate.opsForZSet().rangeByScore(key, 0, 100);


    }
    
    /**
     * 放数据
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public Long lPush(String key, String... value) {
        Long aLong = redisTemplate.opsForList().leftPushAll(key, value);
        if (aLong == null) {
            return 0L;
        }
        return aLong;
    }
    
    /**
     * 取数据
     *
     * @param key
     * @return
     */
    @Override
    public String rPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }
    
    
    /**
     * 删除List中某一个数据
     *
     * @param key
     * @return
     */
    @Override
    public Long listRemove(String key, String value) {
        return redisTemplate.opsForList().remove(key, 1, value);
    }
    
    @Override
    public Long rPush(String key, String... value) {
        Long aLong = redisTemplate.opsForList().rightPushAll(key, value);
        if (aLong == null) {
            return 0L;
        }
        return aLong;
    }
    
    @Override
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }
    
    @Override
    public List<String> lRange(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }
    
    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 锁住资源，如果资源已经被锁住了，返回false，如果成功锁住资源，返回true，60秒钟后自动解锁
     *
     * @param key
     * @return
     */
    public boolean lock(String key) {
        String newKey = key + LOCK;
        if (exists(newKey)) {
            return false;
        }

        return set(newKey, "1", 60);
    }

    /**
     * 解锁资源
     *
     * @param key
     * @return
     */
    public boolean unlock(String key) {
        String newKey = key + LOCK;
        del(newKey);
        return true;
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String ... key){
        if(key!=null&&key.length>0){
            if(key.length==1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 移除key
     * @param keys
     */
    public void remove(String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * key是否存在
     * @param key
     * @return
     */
    public boolean exists(String key) {
        return this.redisTemplate.hasKey(key).booleanValue();
    }

    /**
     * 递减
     * @param key 键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<String>> rangeWithScores(String key, long startIndex, long lastIndex) {
        return redisTemplate.opsForZSet().rangeWithScores(key, startIndex, lastIndex);
    }

    @Override
    public Set<String> reverseRange(String key, long startIndex, long lastIndex) {
        return redisTemplate.opsForZSet().reverseRange(key, startIndex, lastIndex);
    }


    @Override
    public Set<ZSetOperations.TypedTuple<String>> reverseRangeWithScores(String key, long startIndex, long lastIndex) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, startIndex, lastIndex);
        //return redisTemplate.opsForZSet().reverseRange(key, startIndex, lastIndex);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<String>> rangeByScoreWithScores(String key, long lowScore, long highScore) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key,lowScore,highScore);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<String>> reverseRangeByScoreWithScores(String key, long lowScore, long highScore) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key,lowScore,highScore);
    }

    @Override
    public boolean add(String key, String member, double score) {
        return redisTemplate.opsForZSet().add(key, member, score);
    }

    @Override
    public Long zCard(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }

    @Override
    public double incrementScore(String key, String member, double score) {
        return redisTemplate.opsForZSet().incrementScore(key,member,score);
    }


    public static void main(String[] args) {
        //此活动参与人数共有多少
        //redisTemplate.opsForZSet().zCard()
        //redisTemplate.opsForZSet().incrementScore()

        //redisTemplate.opsForZSet().add

        //Set<ZSetOperations.TypedTuple<String>> s = new s
    }

}

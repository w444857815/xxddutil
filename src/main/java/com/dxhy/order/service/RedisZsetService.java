package com.dxhy.order.service;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Set;

/**
 * redis缓存服务
 *
 * @author ZSC-DXHY
 */
public interface RedisZsetService {
    
    /**
     * 给一个key值设置过期时间
     *
     * @param key
     * @param seconds
     * @return
     */
    Boolean expire(String key, int seconds);
    
    /**
     * 删除缓存中得对象，根据key
     *
     * @param key
     * @return
     */
    boolean del(String key);
    
    /**
     * 根据key 获取内容
     *
     * @param key
     * @return
     */
    Set<String> get(String key);
    
    /**
     * 根据key 获取对象
     *
     * @param key
     * @param clazz
     * @return
     */
    <T> T get(String key, Class<T> clazz);
    
    /**
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将 指定value 追加到改 key 原来的值（value）的末尾。
     *
     * @param key
     * @param value
     * @return
     */
    long append(String key, String value);
    
    /**
     * setNX
     *
     * @param key
     * @param value
     * @return
     */
    boolean setNx(String key, String value);
    
    /**
     * 向缓存中设置字符串内容
     *
     * @param key
     * @param value
     * @return
     */
    boolean set(String key, String value);
    
    /**
     * 保存缓存
     *
     * @param key
     * @param value
     * @param seconds 有效时间/秒
     * @return
     */
    boolean set(String key, String value, int seconds);
    
    /**
     * 保存缓存
     *
     * @param key
     * @param value   对象，转为json字符串后存入redis
     * @param seconds 有效时间，单位为秒
     * @return
     */
    boolean set(String key, Object value, int seconds);
    
    /**
     * 将一个或多个值插入到列表头部
     *
     * @param key
     * @param value
     * @return
     */
    Long lPush(String key, String... value);
    
    /**
     * 移除并获取列表最后一个元素
     *
     * @param key
     * @return
     */
    String rPop(String key);
    
    /**
     * 移除列表某一个元素
     *
     * @param key
     * @param value
     * @return
     */
    Long listRemove(String key, String value);
    
    /**
     * 在列表中添加一个或多个值
     *
     * @param key
     * @param value
     * @return
     */
    Long rPush(String key, String... value);
    
    /**
     * 模糊查询key
     *
     * @param pattern
     * @return
     */
    Set<String> keys(String pattern);
    
    /**
     * 模糊查询key
     *
     * @param key
     * @return
     */
    List<String> lRange(String key);
    
    /**
     * 获取失效时间
     *
     * @param key
     * @return
     */
    Long getExpire(String key);

    /**
     * 锁住资源，如果资源已经被锁住了，返回false，如果成功锁住资源，返回true，60秒钟后自动解锁
     *
     * @param key
     * @return
     */
    public boolean lock(String key) ;

    /**
     * 解锁资源
     *
     * @param key
     * @return
     */
    public boolean unlock(String key);

    /**
     * 递减
     * @param key 键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta);

    Set<ZSetOperations.TypedTuple<String>> rangeWithScores(String key, long startIndex, long lastIndex);

    Set<String> reverseRange(String key, long startIndex, long lastIndex);

    Set<ZSetOperations.TypedTuple<String>> rangeByScoreWithScores(String key, long lowScore, long highScore);

    Set<ZSetOperations.TypedTuple<String>> reverseRangeByScoreWithScores(String key, long lowScore, long highScore);

    boolean add(String key, String member, double score);

    Long zCard(String key);

    double incrementScore(String key, String member, double score);

    Set<ZSetOperations.TypedTuple<String>> reverseRangeWithScores(String key, long startIndex, long lastIndex);
}

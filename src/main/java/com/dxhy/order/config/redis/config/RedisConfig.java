package com.dxhy.order.config.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis重新定义template
 *
 * @author ZSC-DXHY
 * @date 创建时间: 2021-03-10 11:38
 */
@Configuration
@Slf4j
public class RedisConfig {
    
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        // 设置redis连接
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        
        // 将redisTemplate的序列化方式更改为StringRedisSerializer
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}

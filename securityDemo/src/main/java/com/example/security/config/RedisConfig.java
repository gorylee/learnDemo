package com.example.security.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author GoryLee
 * @Date 2022/12/5 00:02
 * @Version 1.0
 */
@Configuration
@EnableCaching
public class RedisConfig {

//    @Bean
//    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory connectionFactory){
//        RedisTemplate<Object,Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);
//
//        //使用StringRedisSerializer 来序列化和反序列化redis的key值
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(serializer);
//
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(serializer);
//
//        template.afterPropertiesSet();
//        return template;
//    }

    @Value("${spring.cache.redis.time-to-live}")
    private Long expireTime;
    private Map<String,Long> keys;

    @Bean(name = "redisTemplate")
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(keySerializer());
        redisTemplate.setHashKeySerializer(keySerializer());
        redisTemplate.setValueSerializer(valueSerializer());
        redisTemplate.setHashValueSerializer(valueSerializer());
        return redisTemplate;
    }
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory){
        //默认缓存配置对象
        RedisCacheConfiguration redisCacheDefaultConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheDefaultConfiguration = redisCacheDefaultConfiguration.entryTtl(Duration.ofSeconds(expireTime)) //设置缓存的默认超时时间
                .disableCachingNullValues()             //如果是空值，不缓存
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))         //设置key序列化器
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer((valueSerializer())));  //设置value序列化器

        if(this.keys!=null&&!this.keys.isEmpty()){
            Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
            for (String key : keys.keySet()) {
                RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
                        .disableCachingNullValues()
                        .entryTtl(Duration.ofSeconds(keys.get(key)));
                cacheConfigurations.put(key,redisCacheConfiguration);
            }
            return new RedisCacheManager(RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory),redisCacheDefaultConfiguration,cacheConfigurations);
        }else {
            return new RedisCacheManager(RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory),redisCacheDefaultConfiguration);
        }
    }


    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    private RedisSerializer<Object> valueSerializer() {
        return new GenericFastJsonRedisSerializer();
    }
}

package com.course.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

/**
 * Created by Admin on 24.06.2017.
 */
@Configuration
@PropertySource(value = "classpath:hibernate.properties")
public class RedisConfig {

    private static final String REDIS_HOST = "db.redis.host";

    @Resource
    private Environment env;

    @Bean
    public JedisPool getJedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(poolConfig, env.getProperty(REDIS_HOST), 6379, 30);
//        System.out.println("IN POOLFACTORY " + jedisPool.getResource());
        return jedisPool;
    }

}

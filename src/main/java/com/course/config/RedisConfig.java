package com.course.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * Created by Admin on 24.06.2017.
 */
//@Configuration
//@PropertySource(value = "classpath:hibernate.properties")
public class RedisConfig {
//    private static  final String REDIS_ADDRESS="db.redis.address";
//    private static  final String REDIS_PORT="db.redis.sentinel.port";
//    private static  final String REDIS_MASTER="db.redis.master";
//
//    @Resource
//    private Environment env;
//
//    //@Bean
//    public JedisConnectionFactory jedisConnectionFactory (){
//        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
//                .master(env.getRequiredProperty(REDIS_MASTER))
//                .sentinel(env.getRequiredProperty(REDIS_ADDRESS),
//                        Integer.parseInt(env.getRequiredProperty(REDIS_PORT)));
//        return new JedisConnectionFactory(sentinelConfig);
//    }
//    //@Bean
//    public StringRedisTemplate redisTemplate() {
//        StringRedisTemplate template=
//        new StringRedisTemplate(jedisConnectionFactory());
//        template.setEnableTransactionSupport(true);
//        return template;
//    }
}

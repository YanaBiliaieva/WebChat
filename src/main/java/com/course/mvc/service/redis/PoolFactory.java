package com.course.mvc.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

/**
 * Created by Admin on 10.06.2017.
 */
@Component
public class PoolFactory {

    //    private static PoolFactory instance = new PoolFactory();
    private JedisPool jedisPool;

    @Resource
    private Environment env;

    @Autowired
    public PoolFactory(JedisPool jedisPool){
        this.jedisPool = jedisPool;
        System.out.println("POOL FACTORY!");
    }

    public Jedis getJedis(){
        return jedisPool.getResource();
    }

}
package com.course.mvc.service.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Admin on 10.06.2017.
 */
public class PoolFactory {
    private static PoolFactory instance = new PoolFactory();
    private JedisPool jedisPool;

    private PoolFactory(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        jedisPool = new JedisPool(poolConfig,"jedis.host",6379,30);
    }

    public Jedis getJedis(){
        return jedisPool.getResource();
    }

    public static PoolFactory getInstance(){
        return instance;
    }
}

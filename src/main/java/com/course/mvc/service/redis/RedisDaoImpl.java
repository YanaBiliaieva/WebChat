package com.course.mvc.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
        import redis.clients.jedis.Jedis;
        import redis.clients.jedis.JedisPool;

        import java.util.List;

import java.util.List;

/**
 * Created by Admin on 10.06.2017.
 */
@Repository
public class RedisDaoImpl implements RedisDao {

    //private StringRedisTemplate redisTemplate;

    //@Autowired
  // public RedisDaoImpl (StringRedisTemplate redisTemplate){
       // this.redisTemplate=redisTemplate;
    //}
    private PoolFactory poolFactory = PoolFactory.getInstance();


    @Override
    public void saveDataByKey(String key, String value) {
    //    redisTemplate.opsForList().leftPush(key, value);

        poolFactory.getJedis().lpush(key,value);
    }

    @Override
    public List<String> getAllDataByKey(String key) {
    //    return redisTemplate.opsForList().range(key,0,-1);



        //0 - c первого елемента, -1 - до последнего
        return poolFactory.getJedis().lrange(key,0,-1);
    }//0-с первого. -1 -до последнего
    }


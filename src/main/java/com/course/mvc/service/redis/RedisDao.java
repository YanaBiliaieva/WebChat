package com.course.mvc.service.redis;

import java.util.List;

/**
 * Created by Admin on 10.06.2017.
 */
public interface RedisDao {
    void saveDataByKey(String key, String value);
    List<String> getAllDataByKey(String key);
}

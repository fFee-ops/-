package com.java_lettuce.ServiceImpl;

import com.java_lettuce.config.redisConfig;
import jdk.nashorn.internal.runtime.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Logger
@Service
public class dataTypeServiceImpl {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(redisConfig.class);

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return true 成功 false 失败
     */
    public boolean lpushAll(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().leftPushAll(key, String.valueOf(value));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}

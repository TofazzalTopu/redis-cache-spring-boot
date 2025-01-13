package com.info.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class StringCommands {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    public void setString(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        String retrieveValue = redisTemplate.opsForValue().get("key"); // GET
        redisTemplate.delete("key"); // DE
    }


}

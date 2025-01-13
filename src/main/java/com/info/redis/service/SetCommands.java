package com.info.redis.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SetCommands {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    //Store and retrieve unique values in a set.
    public void setHash(String key, String value) {
        redisTemplate.opsForSet().add("myset", "value1"); // SADD
        redisTemplate.opsForSet().add("myset", "value2");
        Set<Object> members = redisTemplate.opsForSet().members("myset"); // SMEMBERS
        redisTemplate.opsForSet().remove("myset", "value1"); // SREM


    }
}

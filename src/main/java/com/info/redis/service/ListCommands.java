package com.info.redis.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ListCommands {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    //Work with Redis lists (e.g., push, pop).
    public void setHash(String key, String value) {
        redisTemplate.opsForList().leftPush("mylist", "item1"); // LPUSH
        redisTemplate.opsForList().leftPush("mylist", "item2");
        String item = redisTemplate.opsForList().leftPop("mylist"); // LPOP
        redisTemplate.opsForList().rightPush("mylist", "item3"); // RPUSH

    }
}

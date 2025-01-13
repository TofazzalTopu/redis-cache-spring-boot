package com.info.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HashCommands {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    //Store and retrieve fields and values in a hash.
    public void setHash(String key, String value) {
        redisTemplate.opsForHash().put("user:1", key, value); // HSET
        redisTemplate.opsForHash().put("user:1", "name", "Alice"); // HSET
        Object name = redisTemplate.opsForHash().get("user:1", "name"); // HGET
        Map<Object, Object> user = redisTemplate.opsForHash().entries("user:1"); // HGETALL
        redisTemplate.opsForHash().delete("user:1", "name"); // HDEL

    }
}

package com.info.redis.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class SortedSetCommands {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    //Store and retrieve unique values in a set.
    public void setHash(String key, String value) {
        redisTemplate.opsForZSet().add("myzset", "value1", 1.0); // ZADD
        redisTemplate.opsForZSet().add("myzset", "value2", 2.0);
        Set<Object> range = redisTemplate.opsForZSet().range("myzset", 0, -1); // ZRANGE
        redisTemplate.opsForZSet().remove("myzset", "value1"); // ZREM
    }

    //Expire and TTL
    public void setHashWithExpire(String key, String value) {
        redisTemplate.expire("key", 60, TimeUnit.SECONDS); // Set expiration to 60 seconds
        Long ttl = redisTemplate.getExpire("key", TimeUnit.SECONDS); // Get remaining TTL
    }

    //Transactions
    public void setHashWithTransaction(String key, String value) {
        redisTemplate.multi();
        redisTemplate.opsForZSet().add("myzset", "value1", 1.0); // ZADD
        redisTemplate.opsForZSet().add("myzset", "value2", 2.0);
        redisTemplate.exec();

//        redisTemplate.execute((RedisOperations operations) -> {
//            operations.multi(); // Start transaction
//            operations.opsForValue().set("key1", "value1");
//            operations.opsForValue().set("key2", "value2");
//            return operations.exec(); // Commit transaction
//        });
    }


    //Pipelines
    public void setHashWithPipeline() {
//        List<Object> results = redisTemplate.executePipelined((RedisOperations operations) -> {
//            operations.opsForValue().set("key1", "value1");
//            operations.opsForValue().set("key2", "value2");
//            return null;
//        });
    }

    //Pub/Sub
    //Publish and subscribe to channels
    public void setHashWithPubSub() {
        redisTemplate.convertAndSend("channel", "Hello, Redis!");
    }
}

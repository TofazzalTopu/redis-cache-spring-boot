package com.info.redis.controller;

import com.info.redis.service.RedisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

    private final RedisService redisService;

    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @GetMapping("/save")
    public String save(@RequestParam String key, @RequestParam String value) {
        redisService.saveData(key, value);
        return "Data saved!";
    }

    @GetMapping("/get")
    public Object get(@RequestParam String key) {
        return redisService.getData(key);
    }

    @GetMapping("/delete")
    public String delete(@RequestParam String key) {
        redisService.deleteData(key);
        return "Data deleted!";
    }
}


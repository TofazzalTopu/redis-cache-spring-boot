package com.info.redis.controller;

import com.info.redis.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/cache")
@RestController
public class CacheController {

    @Autowired private CacheService cacheService;

    @GetMapping(value = "/{id}")
    private ResponseEntity<?> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(cacheService.getUserById(id));
    }
}

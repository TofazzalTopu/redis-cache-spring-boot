package com.info.redis.entity;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("Product")
public class Product {
    @Id
    private String id;

    @Indexed
    private String category;

    private String name;
    private Double price;

    // Getters and Setters
}


package com.info.redis.entity;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("Session")
public class Session {
    @Id
    private String sessionId;

    @TimeToLive
    private Long timeout;

    // Getters and Setters
}

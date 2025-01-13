package com.info.redis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Entity
@Table(name = "redis_test_users")
//@RedisHash("User")
public class User implements Serializable {

    @Id
    private Long id;
    private String name;
    private int age;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

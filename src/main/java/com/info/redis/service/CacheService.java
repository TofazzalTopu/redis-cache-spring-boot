package com.info.redis.service;

import com.info.redis.entity.User;
import com.info.redis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CacheService {


    @Autowired private UserRepository userRepository;

    private static final Map<Long, String> mockDb = new HashMap<>();

    static {
        mockDb.put(1L, "John Doe");
        mockDb.put(2L, "Jane Smith");
        mockDb.put(3L, "Bob Johnson");
    }

    //Explanation:
    //@Cacheable: Caches the result of the method call.
    //value: The name of the cache (logical grouping).
    //key: Specifies how to generate cache keys (default is the method arguments).
    @Cacheable(value = "users", key = "#id")
    public String getUserById(Long id) {
        System.out.println("Fetching user from DB for ID: " + id);
        return mockDb.get(id); // Simulating DB fetch
    }


    @CachePut(value = "users", key = "#user.id")
    public User updateUser(User user) {
        return userRepository.save(user); // Save user and update cache
    }

    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id); // Delete user from DB and cache
    }

    @CacheEvict(value = "users", allEntries = true) // Clear all entries in the cache
    public void clearCache() {
    }


    @Caching(
            put = { @CachePut(value = "users", key = "#user.id") },
            evict = { @CacheEvict(value = "users", key = "#user.oldId") }
    )
    public User saveUser(User user) {
        return userRepository.save(user);
    }


}

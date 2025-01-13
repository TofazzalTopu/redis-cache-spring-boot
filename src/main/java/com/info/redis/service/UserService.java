package com.info.redis.service;

import com.info.redis.entity.User;
import com.info.redis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    private static final String USER_CACHE_PREFIX = "user:";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public User getUserById(Long id) {
        String cacheKey = USER_CACHE_PREFIX + id;

        // Check cache first
        User cachedUser = (User) redisTemplate.opsForValue().get(cacheKey);
        if (cachedUser != null) {
            System.out.println("Cache hit for user ID: " + id);
            return cachedUser;
        }

        // If not in cache, fetch from DB and cache it
        System.out.println("Cache miss for user ID: " + id);
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            redisTemplate.opsForValue().set(cacheKey, user.get(), 10, TimeUnit.MINUTES); // Cache for 10 minutes
            return user.get();
        }

        return null;
    }

    public User saveUser(User user) {
        // Save to DB
        User savedUser = userRepository.save(user);

        // Update cache
        String cacheKey = USER_CACHE_PREFIX + user.getId();
        redisTemplate.opsForValue().set(cacheKey, savedUser, 10, TimeUnit.MINUTES); // Cache for 10 minutes

        return savedUser;
    }

    public void deleteUserById(Long id) {
        // Delete from DB
        userRepository.deleteById(id);

        // Remove from cache
        String cacheKey = USER_CACHE_PREFIX + id;
        redisTemplate.delete(cacheKey);
    }
}


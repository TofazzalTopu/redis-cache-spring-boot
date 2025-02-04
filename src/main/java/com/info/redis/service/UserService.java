package com.info.redis.service;

import com.info.redis.entity.User;
import com.info.redis.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
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

    public List<User> findAllByOpsForValue() {
        String cacheKey = USER_CACHE_PREFIX + "findAllForValue";
        // Retrieve the cached list from Redis
        List<User> users = (List<User>) redisTemplate.opsForValue().get(cacheKey);
        if (Objects.nonNull(users)) {
            logger.info("UserList retrieved from the cache.");
            return users;
        }

        // Fetch from the database if not found in cache
        users = userRepository.findAll();
        if (Objects.nonNull(users)) {
            // Store the entire list in Redis as a single value
            redisTemplate.opsForValue().set(cacheKey, users);
        }

        return users;
    }

    public List<User> findAllByOpsForSet() {
        String cacheKey = USER_CACHE_PREFIX + "findAllSet";
        // Retrieve the list of user IDs from Redis
        Set<Object> userIds = redisTemplate.opsForSet().members(cacheKey);
        if (userIds != null && !userIds.isEmpty()) {
            logger.info("UserList retrieved from the cache.");
            List<User> users = new ArrayList<>();
            for (Object userId : userIds) {
                User user = (User) redisTemplate.opsForHash().get(USER_CACHE_PREFIX + userId, "user");
                users.add(user);
            }
            return users;
        }

        // Fetch from the database if not found in cache
        List<User> users = userRepository.findAll();
        if (Objects.nonNull(users)) {
            // Store each user as a separate hash and their IDs in a set
            for (User user : users) {
                String userKey = USER_CACHE_PREFIX + user.getId();
                redisTemplate.opsForHash().put(userKey, "user", user);
                redisTemplate.opsForSet().add(cacheKey, user.getId());
            }
        }
        return users;
    }

    public List<User> findAllByOpsForList() {
        String cacheKey = USER_CACHE_PREFIX + "findAllForList";

        // Retrieve the cached list from Redis
        List<User> users = new ArrayList<>();
        List<Object> cachedUsers = redisTemplate.opsForList().range(cacheKey, 0, -1);
        if (cachedUsers != null && !cachedUsers.isEmpty()) {
            logger.info("UserList retrieved from the cache.");
            for (Object user : cachedUsers) {
                users.add((User) user);
            }
            return users;
        }

        // Fetch from the database if not found in cache
        users = userRepository.findAll();
        if (Objects.nonNull(users)) {
            // Store each user as a separate element in the Redis list
            for (User user : users) {
                redisTemplate.opsForList().rightPush(cacheKey, user);
            }
        }
        return users;
    }

    public Set<User> findAllByOpsForSet2() {
        String cacheKey = USER_CACHE_PREFIX + "findAllForSet2";
        // Retrieve the cached set from Redis
        Set<User> users = new HashSet<>();
        Set<Object> cachedUsers = redisTemplate.opsForSet().members(cacheKey);
        if (cachedUsers != null && !cachedUsers.isEmpty()) {
            logger.info("UserList retrieved from the cache.");
            for (Object user : cachedUsers) {
                users.add((User) user);
            }
            return users;
        }

        // Fetch from the database if not found in cache
        users = new HashSet<>(userRepository.findAll());
        if (Objects.nonNull(users)) {
            // Store each user as a separate element in the Redis set
            for (User user : users) {
                redisTemplate.opsForSet().add(cacheKey, user);
            }
        }
        return users;
    }

    public void otherSetOperations() {
        String SET_KEY = "fruits";
        boolean exists = redisTemplate.opsForSet().isMember(SET_KEY, "Apple");
        redisTemplate.opsForSet().remove(SET_KEY, "Apple");
        long size = redisTemplate.opsForSet().size(SET_KEY);
        String randomValue = (String) redisTemplate.opsForSet().pop(SET_KEY);
    }

}


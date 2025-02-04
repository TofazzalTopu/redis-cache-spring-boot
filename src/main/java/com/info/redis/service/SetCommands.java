package com.info.redis.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SetCommands {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final String SET_KEY = "mySet";


    //Store and retrieve unique values in a set.
    public void setHash(String key, String value) {
        redisTemplate.opsForSet().add("myset", "value1"); // SADD
        redisTemplate.opsForSet().add("myset", "value2");
        Set<Object> members = redisTemplate.opsForSet().members("myset"); // SMEMBERS
        redisTemplate.opsForSet().remove("myset", "value1"); // SREM
    }

    public void addValuesToSet(Set<String> values) {
        redisTemplate.opsForSet().add(SET_KEY, values.toArray(new String[0]));
    }

    // Retrieve all values from the Redis Set
    public Set<Object> getValuesFromSet() {
        return redisTemplate.opsForSet().members(SET_KEY);
    }

    //get the size of the set
    public long getSetSize() {
        return redisTemplate.opsForSet().size(SET_KEY);
    }

    //randomly pop a value from the set
    public Object popValueFromSet() {
        return redisTemplate.opsForSet().pop(SET_KEY);
    }

    //Usage of the SetCommands class
    public void test() {
        Set<String> values = Set.of("Apple", "Banana", "Cherry");
        addValuesToSet(values);

        Set<Object> storedValues = getValuesFromSet();

        Set<String> stingValues = new HashSet<>();
        storedValues.forEach(value -> stingValues.add((String) value));

        System.out.println("Stored Values: " + storedValues);

        updateValueInSet("Banana", "Mango");
        Set<Object> updatedValues = getValuesFromSet();
        Set<String> newValues = new HashSet<>();
        updatedValues.forEach(value -> newValues.add((String) value));

        System.out.println("Updated Set: " + updatedValues);
    }

    public void updateValueInSet(String oldValue, String newValue) {
        // Remove the old value
        redisTemplate.opsForSet().remove(SET_KEY, oldValue);

        // Add the new value
        redisTemplate.opsForSet().add(SET_KEY, newValue);
    }

    public void removeValueFromSet(String value) {
        redisTemplate.opsForSet().remove(SET_KEY, value);
    }

    public void deleteSet() {
        redisTemplate.delete(SET_KEY);
    }

}

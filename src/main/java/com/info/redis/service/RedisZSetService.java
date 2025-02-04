package com.info.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RedisZSetService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String ZSET_KEY = "leaderboard";


    // Add multiple values
    public void addMultipleValues(Set<ZSetEntry> values) {
        for (ZSetEntry entry : values) {
            redisTemplate.opsForZSet().add(ZSET_KEY, entry.getValue(), entry.getScore());
        }
    }

    // Retrieve sorted values in ascending order
    public Set<String> getSortedValues() {
        return redisTemplate.opsForZSet().range(ZSET_KEY, 0, -1);
    }


    //update stored value
    public void updateValueInSet(String oldValue, String newValue) {
        updateScore("Alice", 150);
    }

    //remove value from Zset
    public void removeValueFromZSet(String value) {
        redisTemplate.opsForZSet().remove(ZSET_KEY, value);
    }

    public void addValue(String value, double score) {
        redisTemplate.opsForZSet().add(ZSET_KEY, value, score);
    }

    public void updateScore(String value, double newScore) {
        redisTemplate.opsForZSet().add(ZSET_KEY, value, newScore);
    }

    public void removeValue(String value) {
        redisTemplate.opsForZSet().remove(ZSET_KEY, value);
    }

    public Set<String> getTopN(int n) {
        return redisTemplate.opsForZSet().reverseRange(ZSET_KEY, 0, n - 1);
    }

    public Double getScore(String value) {
        return redisTemplate.opsForZSet().score(ZSET_KEY, value);
    }

    public void test() {
        //Add single value
        addValue("Alice", 100);
        addValue("Bob", 200);

        //Retrieve sorted values
        Set<String> sortedValues = getSortedValues();
        System.out.println("Sorted Set: " + sortedValues);

        //Get top 2 players
        Set<String> topPlayers = getTopN(2);
        System.out.println("Top Players: " + topPlayers);

        //Update Alice's score
        updateScore("Alice", 250);

        //Remove Bob
        removeValue("Bob");
    }

    public static class ZSetEntry {
        private String value;
        private double score;

        public ZSetEntry() {
        }

        public ZSetEntry(String value, double score) {
            this.value = value;
            this.score = score;
        }

        public String getValue() {
            return value;
        }

        public double getScore() {
            return score;
        }

        List<ZSetEntry> entries = List.of(
                new ZSetEntry("Alice", 100),
                new ZSetEntry("Bob", 200),
                new ZSetEntry("Charlie", 300)
        );
    }
}

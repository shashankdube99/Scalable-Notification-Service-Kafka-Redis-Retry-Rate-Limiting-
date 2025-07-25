package com.shashank.kafkarabbit.ratelimiter;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RateLimiterService {

    private static final int MAX_REQUESTS = 5;         // limit
    private static final int WINDOW_SECONDS = 60;      // time window

    private final JedisPool jedisPool;

    public RateLimiterService(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public boolean isAllowed(String userEmail) {
        String key = "rate_limit:" + userEmail;

        try (Jedis jedis = jedisPool.getResource()) {
            Long count = jedis.incr(key);

            if (count == 1) {
                jedis.expire(key, WINDOW_SECONDS); // new window
            }

            return count <= MAX_REQUESTS;
        } catch (Exception e) {
            // optional fallback: allow or block
            System.err.println("Redis error, fallback to allowing request.");
            return true;
        }
    }
}
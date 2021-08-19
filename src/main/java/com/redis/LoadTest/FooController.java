package com.redis.LoadTest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
//@RequestMapping("/foos")
class FooController {

    final JedisPoolConfig poolConfig = buildPoolConfig();
    JedisPool jedisPool = new JedisPool(poolConfig, "redis-12000.kluster-east.demo.redislabs.com", 12000, 60000, "pass");
    Random rand = new Random();

    private JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }

    @GetMapping(value = "/hello")
    public List<String> get() {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = "Location:" + rand.nextInt(700);
            return jedis.hmget(key, "location", "active_pick");
        }
    }

    @GetMapping(value = "/update")
    public String update() {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = "Location:" + rand.nextInt(700);
            jedis.hset(key, "active_pick", String.valueOf(rand.nextInt(9999)));
            return "OK";
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody String resource) {
        return 0l;
    }
}

package com.redis.LoadTest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.List;
import java.util.Random;

@RestController
class FooController {

    final JedisPoolConfig poolConfig = buildPoolConfig();
    JedisPool jedisPool = new JedisPool(poolConfig, "redis-15547.c17333.us-east1-mz.gcp.cloud.rlrcp.com", 15547, 60000, "j3fzUXi7jS07GV137C4FWlxb1qnwX9ZU");
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
            long start = System.currentTimeMillis();
            List<String> retval = jedis.hmget(key, "active_pick");
            System.out.println("Took: " + (System.currentTimeMillis() - start) + " ms to get");
            return retval;
        }
    }

    @GetMapping(value = "/update")
    public ResponseEntity update() {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = "Location:" + rand.nextInt(700);
            long start = System.currentTimeMillis();
            jedis.hset(key, "active_pick", String.valueOf(rand.nextInt(9999)));
            System.out.println("Took: " + (System.currentTimeMillis() - start) + " ms to update");
            return new ResponseEntity<>("Hello World!", HttpStatus.ACCEPTED);
        }
    }

    @PostMapping
    public ResponseEntity create(@RequestBody String resource) {
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }
}

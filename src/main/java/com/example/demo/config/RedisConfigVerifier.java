package com.example.demo.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisConfigVerifier implements ApplicationRunner {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        RedisConnection redisConnection = redisConnectionFactory.getConnection();
        redisConnection.setConfig("notify-keyspace-events", "Ex");
        Properties config = redisConnection.getConfig("notify-keyspace-events");
        System.out.println("Current notify-keyspace-events: " + config);
    }

}


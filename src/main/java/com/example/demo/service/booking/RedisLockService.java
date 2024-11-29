package com.example.demo.service.booking;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisLockService {
	@Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取分布式锁
     * @param lockKey 锁的键
     * @param timeout 锁的过期时间（秒）
     * @return 是否成功获取锁
     */
    public boolean tryLock(String lockKey, long timeout) {
        Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "LOCKED", Duration.ofSeconds(timeout));
        return Boolean.TRUE.equals(result);
    }

    /**
     * 释放分布式锁
     * @param lockKey 锁的键
     */
    public void releaseLock(String lockKey) {
        stringRedisTemplate.delete(lockKey);
    }
}

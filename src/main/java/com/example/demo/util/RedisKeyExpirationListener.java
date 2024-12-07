package com.example.demo.util;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import com.example.demo.service.booking.MemberBuyTicketOrderService;

import jakarta.transaction.Transactional;

@Transactional
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
	@Autowired
    private MemberBuyTicketOrderService memberBuyTicketOrderService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
    	 String expiredKey = message.toString();

    	    // 确认是订单过期的 Key
    	    if (expiredKey.startsWith("order-expire:")) {
    	        Integer orderId = Integer.parseInt(expiredKey.split(":")[1]);

    	        // 检查 Redis 键的值是否为 "PENDING"
    	        System.out.println(expiredKey);

    	     // 从哈希表中获取键值
    	        String keyValue = (String) redisTemplate.opsForHash().get("order-details", expiredKey);
    	        System.out.println("Processing expired key: " + expiredKey);
    	        System.out.println("Key value from hash: " + keyValue);
    	        
    	        if ("PENDING".equals(keyValue)) {
    	            // 如果值是 "PENDING"，执行订单删除逻辑
    	            try {
    	                memberBuyTicketOrderService.deleteById(orderId);
    	            } catch (Exception e) {
    	            	System.err.println("Error deleting order: " + orderId);
    	                e.printStackTrace();
    	            }
    	        }
    	        redisTemplate.opsForHash().delete("order-details", expiredKey);
    	        System.out.println("Redis hash key deleted: " + expiredKey);
    	    }
    }
}

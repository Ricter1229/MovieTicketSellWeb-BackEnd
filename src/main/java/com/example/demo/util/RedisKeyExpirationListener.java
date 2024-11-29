package com.example.demo.util;

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
//        String expiredKey = message.toString();
//
//        // 檢查是否是訂單過期的 Key
//        if (expiredKey.startsWith("order-expire:")) {
//        	Integer orderId = Integer.parseInt(expiredKey.split(":")[1]);
//
//            // 刪除訂單與解鎖座位
//            try {
//				memberBuyTicketOrderService.deleteById(orderId);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        }
    	 String expiredKey = message.toString();

    	    // 确认是订单过期的 Key
    	    if (expiredKey.startsWith("order-expire:")) {
    	        Integer orderId = Integer.parseInt(expiredKey.split(":")[1]);

    	        // 检查 Redis 键的值是否为 "PENDING"
    	        String keyValue = redisTemplate.opsForValue().get(expiredKey);
    	        if ("PENDING".equals(keyValue)) {
    	            // 如果值是 "PENDING"，执行订单删除逻辑
    	            try {
    	                memberBuyTicketOrderService.deleteById(orderId);
    	            } catch (Exception e) {
    	                e.printStackTrace();
    	            }
    	        } else {
    	            // 如果值不是 "PENDING"，删除 Redis 键
    	            redisTemplate.delete(expiredKey);
    	            System.out.println("Redis key deleted because it was not PENDING: " + expiredKey);
    	        }
    	    }
    }
}

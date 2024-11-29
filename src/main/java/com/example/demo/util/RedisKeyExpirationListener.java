package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import com.example.demo.service.booking.MemberBuyTicketOrderService;
import com.example.demo.service.booking.SeatingListService;

import jakarta.transaction.Transactional;

@Transactional
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
	@Autowired
    private MemberBuyTicketOrderService memberBuyTicketOrderService;

    @Autowired
    private SeatingListService seatingListService;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();

        // 檢查是否是訂單過期的 Key
        if (expiredKey.startsWith("order-expire:")) {
        	Integer orderId = Integer.parseInt(expiredKey.split(":")[1]);

            // 刪除訂單與解鎖座位
            try {
				memberBuyTicketOrderService.deleteById(orderId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
}

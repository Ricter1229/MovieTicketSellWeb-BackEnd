package com.example.demo.service.booking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.SeatingListBean;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.MemberBuyTicketOrderRepository;
import com.example.demo.repository.SeatingListRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SeatingListService {
	@Autowired
	private RedisLockService redisLockService;
	
	@Autowired
	private SeatingListRepository seatingListRepository;
	@Autowired
	private MemberBuyTicketOrderRepository memberBuyTicketOrderRepository;
	
	/**
     * 鎖定座位
     * @param scheduleId 場次 ID
     * @param seatId 座位 ID
     * @param orderId 訂單 ID
     */
    public boolean lockSeat(Integer scheduleId, String seat) {
        String lockKey = "seat:" + scheduleId + ":" + seat;

        try { // 避免 redis 無法連線
			// 取得分布式鎖
			boolean isLocked = redisLockService.tryLock(lockKey, 30); // 鎖定 30 秒
			if (!isLocked) {
			    throw new CustomException("Seat is already locked", 400);
			}
		} catch (Exception e) {
			System.err.println("Redis connection failed: " + e.getMessage());
		}
        
        // 更新資料庫中的座位狀態
        SeatingListBean seatList = seatingListRepository.findSeatForUpdate(scheduleId, seat)
                .orElseThrow(() -> new CustomException("Seat not found", 404));
        if (seatList.getIsLocked() == 1 || seatList.getIsSold()  == 1) {
            redisLockService.releaseLock(lockKey); // 釋放 Redis 鎖
            return false;
            //throw new CustomException("Seat is not available", 400);
        }
        
    	seatList.setIsLocked(1);
        seatList.setSeatLockCreateTime(LocalDateTime.now());
        seatingListRepository.save(seatList);
        return true;
    }
    
    /**
     * 釋放座位
     * @param scheduleId 場次 ID
     * @param seatId 座位 ID
     */
    public boolean releaseSeat(Integer scheduleId, String seat) {
        String redisLockKey = "seat:" + scheduleId + ":" + seat;

        // Step 1: 使用資料庫行鎖更新座位狀態
        SeatingListBean seatList = seatingListRepository.findSeatForUpdate(scheduleId, seat)
        		.orElseThrow(() -> new CustomException("Seat not found", 404));
        if (seatList.getIsLocked() == 1) {
        	seatList.setIsLocked(0);
        	seatList.setIsSold(0);
        	seatList.setLockedByOrderId(null);
        	seatList.setSoldByOrderId(null);
        	seatList.setSeatLockCreateTime(null);
            seatingListRepository.save(seatList);
            
            // Step 2: 釋放 Redis 鎖
            redisLockService.releaseLock(redisLockKey);
            return true;
        }
        return false;
    }
    
    /**
     * 新增100個座位
     * @param
     * @return
     */
    public void insertSeat() {
    	int num = 100;
    	String startChar = "A";
        List<SeatingListBean> seatList = new ArrayList<>();

    	for (int i = 1; i <= num; i++) {
            SeatingListBean seatingListBean = new SeatingListBean();
            seatingListBean.setAuditoriumScheduleId(1);
            seatingListBean.setSeat(startChar + "-" + i);
            seatingListBean.setLockedByOrderId(null);
            seatingListBean.setAuditoriumScheduleBean(null);
            seatingListBean.setSoldByOrderId(null);
            seatList.add(seatingListBean);
        }

        seatingListRepository.saveAll(seatList);
    }
    
    /**
     * 修改座位狀態為已售出
     * @param
     * @return
     */
    public boolean purchaseSeat(Integer scheduleId, String seat, Integer orderId) {
    	SeatingListBean seatList = seatingListRepository.findSeatForUpdate(scheduleId, seat)
        		.orElseThrow(() -> new CustomException("Seat not found", 404));
    	memberBuyTicketOrderRepository.findById(orderId)
				.orElseThrow(() -> new CustomException("order not found", 404));
    	if(seatList.getIsSold() == 0) {
    		seatList.setIsSold(1);
        	seatList.setSoldByOrderId(orderId);
        	return true;
    	}
    	return false;
    }
    
    /**
     * 
     * 
     */
    public boolean isSeatSoldOrNot(Integer scheduleId, String seat) {
    	SeatingListBean seatList = seatingListRepository.findByAuditoriumScheduleIdAndSeat(scheduleId, seat)
        		.orElseThrow(() -> new CustomException("Seat not found", 404));
//    	return seatingListRepository.isSeatLock(scheduleId, seat);
    	return seatList.getIsSold() == 1;
    }
    
    /**
     * 查詢某場次的已售出座位
     * @param
     * @return
     */
    public List<SeatingListBean> getAllSoldSeatWithScheduleId(Integer scheduleId) {
    	List<SeatingListBean> seatLists = seatingListRepository.findByAuditoriumScheduleIdSoldSeat(scheduleId, 1);
        if(seatLists == null || seatLists.size() == 0) {
        	throw new CustomException("Auditorium Schedule not found", 404);
        }
        return seatLists;
    }
    
    
}

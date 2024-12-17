package com.example.demo.service.booking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.SeatingListBean;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.AuditoriumScheduleRepository;
import com.example.demo.repository.MemberBuyTicketOrderRepository;
import com.example.demo.repository.SeatingListRepository;
import com.example.demo.service.SeatingService;

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
	@Autowired
	private SeatingService seatingService;
	@Autowired
	private AuditoriumScheduleRepository auditoriumScheduleRepository;

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
			throw new CustomException("Redis connection failed: " + e.getMessage(), 400);
		}
        System.out.println("2");
        System.out.println(scheduleId + " " + seat);

        // 更新資料庫中的座位狀態
        SeatingListBean seatList = seatingListRepository.findSeatForUpdate(scheduleId, seat)
                .orElseThrow(() -> new CustomException("Seat not found", 404));
        System.out.println("3");

        if (seatList.getIsLocked() == 1 || seatList.getIsSold()  == 1) {
            redisLockService.releaseLock(lockKey); // 釋放 Redis 鎖
            throw new CustomException("Seat is not available", 400);
        }
        System.out.println("1");

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
     * 新增座位進 seatingList
     * @param
     * @return 
     * @return
     */
    public List<SeatingListBean> insertSeat(Integer auditoriumId, Integer auditoriumScheduleId) {    	    	
    	// 假设 auditoriumId 已传入，seatingListRepository 和 SeatingListBean 已定义。
    	Map<String, Object> seats = seatingService.findSeatingList(auditoriumId);
    	// 获取 "seats" 的值并强制转换为 List<Map<String, Object>>
    	List<Map<String, Object>> seatRows = (List<Map<String, Object>>) seats.get("seats");
        List<SeatingListBean> seatList = new ArrayList<>();
    	// 遍历每一行的座位数据
    	for (Map<String, Object> row : seatRows) {
    	    String rowLabel = (String) row.get("row"); // 获取行名（如 "A", "B", "walkway"）
    	    List<Object> seatNumbers = (List<Object>) row.get("seats"); // 获取座位列表
    	    
    	    // 如果是 "walkway"，跳过
    	    if ("walkway".equalsIgnoreCase(rowLabel)) {
    	        continue;
    	    }

    	    // 遍历座位
    	    for (Object seatNumber : seatNumbers) {
    	        // 跳过 null 座位
    	        if (seatNumber == null) {
    	            continue;
    	        }

    	        if(seatingListRepository.findByAuditoriumScheduleIdAndSeat(auditoriumScheduleId, rowLabel + "-" + seatNumber).isPresent()) {
    	        	continue;
    	        }
    	        					  
    	        // 创建 SeatingListBean
    	        SeatingListBean seatingListBean = new SeatingListBean();
    	        seatingListBean.setAuditoriumScheduleId(auditoriumScheduleId); // 假设使用 auditoriumId
    	        seatingListBean.setSeat(rowLabel + "-" + seatNumber); // 组合行名和座位号，例如 "A-1"
    	        seatingListBean.setLockedByOrderId(null);
    	        
    	        seatingListBean.setAuditoriumScheduleBean(auditoriumScheduleRepository.findById(auditoriumScheduleId).get());
    	        
    	        seatingListBean.setSoldByOrderId(null);

    	        // 添加到列表
    	        seatList.add(seatingListBean);
    	    }
    	}
    	System.out.println("ccc");
    	if(seatList != null && seatList.size() != 0) {
    		System.out.println("Seats saved: " + seatList.size());
    		return seatingListRepository.saveAll(seatList);
    	}
    	return null;
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
        	seatingListRepository.save(seatList);
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
    public List<String> getAllSoldSeatWithScheduleId(Integer scheduleId) {
    	List<String> seatLists = seatingListRepository.findByAuditoriumScheduleIdSoldSeat(scheduleId, 1);
        
        return seatLists;
    }
    
    /**
     * 查詢某場次的已選取座位
     * @param
     * @return
     */
    public List<String> getAllLockSeatWithScheduleId(Integer scheduleId) {
    	List<String> seatLists = seatingListRepository.findByAuditoriumScheduleIdAndIsLocked(scheduleId, 1, 0);
        
        return seatLists;
    }
}

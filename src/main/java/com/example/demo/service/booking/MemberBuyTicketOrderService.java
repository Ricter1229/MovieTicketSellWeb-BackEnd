package com.example.demo.service.booking;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.dto.api.MemberBuyTicketDetailRequestDto;
import com.example.demo.exception.CustomException;
import com.example.demo.model.MemberBean;
import com.example.demo.model.MemberBuyTicketDetailBean;
import com.example.demo.model.MemberBuyTicketOrderBean;
import com.example.demo.model.MovieBean;
import com.example.demo.repository.MemberBuyTicketOrderRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.MovieRepository;

import jakarta.transaction.Transactional;

/**
 * 用於處理與訂單相關的操作的服務層
 * 包括(新增、刪除、修改、查詢)訂單、更新訂單狀態、查詢某會員全部訂單 等功能
 */
@Service
@Transactional
public class MemberBuyTicketOrderService {
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private MemberBuyTicketOrderRepository memberBuyTicketOrderRepository;
	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private MemberBuyTicketDetailService memberBuyTicketDetailService;
	@Autowired
	private SeatingListService seatingListService;
	@Autowired
    private RedisTemplate<String, String> redisTemplate;
	
	/**
	 * 根據 member 創建訂單
	 * @param member 是用戶 * 
	 * @return 此新建立 order 的 id
	 */
	public MemberBuyTicketOrderBean insert(
            Integer memberId,
            Integer movieId,
            BigDecimal totalAmount,
            MemberBuyTicketDetailRequestDto orderDetail) {
        // 驗證會員是否存在
        MemberBean member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException("Member not found", 404));

        // 驗證電影是否存在
        MovieBean movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new CustomException("Movie not found", 404));

        // 嘗試鎖定所有座位
        List<String> successfullyLockedSeats = new ArrayList<>();
		try {
			for(String seat : orderDetail.getSeats()) {
				seatingListService.lockSeat(orderDetail.getAuditoriumScheduleId(), seat);
				successfullyLockedSeats.add(seat);
			}
		} catch (Exception e) {
			for(String seat : successfullyLockedSeats) {
				seatingListService.releaseSeat(orderDetail.getAuditoriumScheduleId(), seat);
			}
            throw new CustomException("Failed to lock seat. ", 400);
		}
        
        // 創建訂單主表
        MemberBuyTicketOrderBean newOrder = new MemberBuyTicketOrderBean();
        newOrder.setMember(member);
        newOrder.setMemberId(member.getId());
        newOrder.setMovieBean(movie);
        newOrder.setMovieId(movieId);
        newOrder.setState("PENDING"); // 訂單狀態初始化
        newOrder.setTotalAmount(totalAmount);
        newOrder = memberBuyTicketOrderRepository.save(newOrder);

        // 創建訂單詳情
        orderDetail.setOrderId(newOrder.getId());
        List<MemberBuyTicketDetailBean> orderDetailList = memberBuyTicketDetailService.insertOrderDetail(orderDetail);
        newOrder.setMemberBuyTicketDetailBeans(orderDetailList);

        // 更新座位狀態為已售出
    	boolean purchaseSuccess = false;
    	for(String seat : orderDetail.getSeats()) {
    		purchaseSuccess = seatingListService.purchaseSeat(
    				orderDetail.getAuditoriumScheduleId(),
                    seat,
                    newOrder.getId());
    		if (!purchaseSuccess) {
                throw new CustomException("Failed to mark seat " + seat + " as sold.", 400);
            }
    	}
        

        // redis 設定倒計時
        // 綁定訂單到 Redis，設置 15 分鐘過期時間
        String redisKey = "order-expire:" + newOrder.getId();
        redisTemplate.opsForValue().set(redisKey, "PENDING", 15, TimeUnit.MINUTES);
        
        return newOrder;
    }
	
	/**
	 * 根據 orderId 修改訂單狀態
	 * @param orderId 訂單 Id * 
	 * @param newState 此訂單將要變成的狀態 * 
	 */
	public void updateOrderStatus(Integer orderId, String newState) {
        // 查詢訂單
		MemberBuyTicketOrderBean order = memberBuyTicketOrderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found", 404));

		// 驗證新狀態是否有效
        if (!List.of("PENDING", "PAID", "CANCELED", "REFUNDED").contains(newState)) {
            throw new CustomException("Invalid order status: " + newState, 400);
        }
        // 驗證狀態流轉
        if (!isValidStatusTransition(order.getState(), newState)) {
            throw new CustomException("Invalid status transition", 400);
        }

        // 更新狀態
        order.setState(newState);
        memberBuyTicketOrderRepository.save(order);
    }
	
	/**
	 * 根據現在訂單狀態，決定下一狀態是否成立
	 * @param currentStatus 現在訂單狀態 * 
	 * @param newState 現在訂單將要變成的狀態 * 
	 */
	private boolean isValidStatusTransition(String currentStatus, String newStatus) {
		switch (currentStatus.trim()) {
            case "PENDING":
                return newStatus.equals("PAID") || newStatus.equals("CANCELED");
            case "PAID":
                return newStatus.equals("REFUNDED");
            case "CANCELED":
            case "REFUNDED":
                return false; // 已取消或退款的訂單不可變更
            default:
                return false;
        }
    }
	
	/**
	 * 根據 Id 查詢訂單
	 * @param orderId 訂單 Id * 
	 * @return 訂單物件 * 
	 */
	public MemberBuyTicketOrderBean findById(Integer orderId) {
		return memberBuyTicketOrderRepository.findById(orderId)
				.orElseThrow(() -> new CustomException("Order not found", 404));
	}
	
	/**
	 * 根據 Id 刪除訂單
	 * @param orderId 訂單 Id * 
	 */
	public void deleteById(Integer orderId) {
		MemberBuyTicketOrderBean order = memberBuyTicketOrderRepository.findById(orderId)
				.orElseThrow(() -> new CustomException("Order " + orderId + " not found, delete failed", 404));
		// 解鎖座位
	    for (MemberBuyTicketDetailBean detail : order.getMemberBuyTicketDetailBeans()) {
	        seatingListService.releaseSeat(detail.getAuditoriumScheduleId(), detail.getSeat());
	    }
	    memberBuyTicketDetailService.deleteTicketDetailByOrderId(orderId);

		try {
			memberBuyTicketOrderRepository.deleteById(orderId);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 查詢會員的所有訂單
	 * @param memberId 會員 Id 
	 */
	public List<MemberBuyTicketOrderBean> findByMemberId(Integer memberId) {
		MemberBean member = memberRepository.findById(memberId)
				.orElseThrow(() -> new CustomException("Member not found", 404));
		
		List<MemberBuyTicketOrderBean> orderList = member.getMemberBuyTicketOrders();
		if(orderList == null || orderList.size() == 0) {
			throw new CustomException("Member " + member.getAccount() + " didn't have order.", 404);
		}
		
		return orderList;
	}
	
}

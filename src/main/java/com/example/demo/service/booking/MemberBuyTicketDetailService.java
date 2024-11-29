package com.example.demo.service.booking;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.api.MemberBuyTicketDetailRequestDto;
import com.example.demo.exception.CustomException;
import com.example.demo.model.AuditoriumScheduleBean;
import com.example.demo.model.MemberBuyTicketDetailBean;
import com.example.demo.model.MemberBuyTicketOrderBean;
import com.example.demo.model.StoreBean;
import com.example.demo.repository.AuditoriumScheduleRepository;
import com.example.demo.repository.MemberBuyTicketDetailRepository;
import com.example.demo.repository.MemberBuyTicketOrderRepository;
import com.example.demo.repository.StoreRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MemberBuyTicketDetailService {
	@Autowired
	private MemberBuyTicketOrderRepository memberBuyTicketOrderRepository;
	@Autowired
	private StoreRepository storeRepository;
	@Autowired
	private AuditoriumScheduleRepository auditoriumScheduleRepository;	
	@Autowired
	private MemberBuyTicketDetailRepository memberBuyTicketDetailRepository;
	@Autowired
	private SeatingListService seatingListService;

	
//	find：查詢操作（如 findById、findAll）
//	insert：新增
//	update：更新
//	delete：刪除
	
	/**
	 * 查詢某筆訂單所有訂單詳細資訊
	 * @param orderId 訂單 Id
	 */
	public List<MemberBuyTicketDetailBean> getTicketDetailByOrderId(Integer orderId) {
		memberBuyTicketOrderRepository.findById(orderId)
				.orElseThrow(() -> new CustomException("Order not found", 404));
		return memberBuyTicketDetailRepository.findByMemberBuyTicketOrderId(orderId)
				.orElseThrow(() -> new CustomException("Detail not found", 404));
	}
	
	/**
	 * 新增一筆訂單細節
	 */
	public List<MemberBuyTicketDetailBean> insertOrderDetail(MemberBuyTicketDetailRequestDto request) {
		MemberBuyTicketOrderBean order = memberBuyTicketOrderRepository.findById(request.getOrderId())
				.orElseThrow(() -> new CustomException("Order not found", 404));
		StoreBean store = storeRepository.findById(request.getStoreId())
				.orElseThrow(() -> new CustomException("Store not found", 404));
		AuditoriumScheduleBean auditoriumSchedule = auditoriumScheduleRepository.findById(request.getAuditoriumScheduleId())
				.orElseThrow(() -> new CustomException("Auditorium Schedule not found", 404));
		
		List<MemberBuyTicketDetailBean> orderDetails = new ArrayList<>();
		for(String seat : request.getSeats()) {
			MemberBuyTicketDetailBean orderDetail = new MemberBuyTicketDetailBean();
			orderDetail.setMemberBuyTicketOrderId(request.getOrderId());
			orderDetail.setMemberBuyTicketOrderBean(order);
	        orderDetail.setStoreId(request.getStoreId());
	        orderDetail.setStore(store);
	        orderDetail.setAuditoriumScheduleId(request.getAuditoriumScheduleId());
	        orderDetail.setAuditoriumScheduleBean(auditoriumSchedule);
	        orderDetail.setSeat(seat);
	        orderDetails.add(orderDetail);
		}
		

        // 保存到數據庫
        return memberBuyTicketDetailRepository.saveAll(orderDetails);
	}
	
	/**
	 * 修改一筆訂單細節
	 */
	
	/**
	 * 刪除某訂單的所有訂單細節
	 */
	public void deleteTicketDetailByOrderId(Integer orderId) {
		memberBuyTicketOrderRepository.findById(orderId)
				.orElseThrow(() -> new CustomException("Order not found", 404));
		List<MemberBuyTicketDetailBean> details = memberBuyTicketDetailRepository.findByMemberBuyTicketOrderId(orderId)
				.orElseThrow(() -> new CustomException("Detail not found", 404));
	    
		for(MemberBuyTicketDetailBean detail : details) {
			seatingListService.releaseSeat(detail.getAuditoriumScheduleId(), detail.getSeat());
		}
		
		try {
			memberBuyTicketDetailRepository.deleteAll(details);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.example.demo.controller.booking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.api.common.ApiResponse;
import com.example.demo.domain.MemberBuyTicketDetailBean;
import com.example.demo.dto.api.MemberBuyTicketDetailRequestDto;
import com.example.demo.service.booking.MemberBuyTicketDetailService;

@RestController
@RequestMapping("/api/details")
@CrossOrigin
public class MemberBuyTicketDetailController {
	@Autowired
	private MemberBuyTicketDetailService memberBuyTicketDetailService;
	
	/**
	 * 查詢某筆訂單所有訂單詳細資訊
	 * @param orderId 訂單 Id
	 * @return
	 */
	@GetMapping("/")
	public ApiResponse<?> getAllTicketDetail(@RequestBody MemberBuyTicketDetailRequestDto request) {
		List<MemberBuyTicketDetailBean> order = memberBuyTicketDetailService.getTicketDetailByOrderId(request.getOrderId());
		return ApiResponse.success(order);
	}
	
	
	
}

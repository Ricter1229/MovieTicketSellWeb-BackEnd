package com.example.demo.controller.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.api.common.ApiResponse;
import com.example.demo.domain.MemberBuyTicketOrderBean;
import com.example.demo.dto.api.MemberBuyTicketOrderRequestDto;
import com.example.demo.service.booking.MemberBuyTicketOrderService;

@RestController
@CrossOrigin
@RequestMapping("/api/orders")
public class MemberBuyTicketOrderController {
	@Autowired
	private MemberBuyTicketOrderService orderService;

	
	@PostMapping("/")
	public ApiResponse<Object> createOrder(@RequestBody MemberBuyTicketOrderRequestDto request) {

		MemberBuyTicketOrderBean order = orderService.insert(
				request.getMemberId(), 
				request.getMovieId(), 
				request.getTotalAmount(),
				request.getOrderDetail()
		);
		return ApiResponse.success(order);
	}
	
	/**
	 * 更改訂單狀態
	 * @param id
	 * PENDING PAID CANCELED REFUNDED
	 * @return
	 */
	@PutMapping("/status")
	public ApiResponse<String> updateOrderStatus(@RequestBody MemberBuyTicketOrderRequestDto request) {
		orderService.updateOrderStatus(request.getOrderId(), request.getStatus());
		return ApiResponse.success(orderService.findById(request.getOrderId()).getState() + " successful.");
	}
	
	@DeleteMapping("/")
	public ApiResponse<String> removeOrder(@RequestBody MemberBuyTicketOrderRequestDto request) {
		orderService.deleteById(request.getOrderId());
		return ApiResponse.success("Remove Order successful.");
	}
	
	@GetMapping("/")
	public ApiResponse<Object> getOrderById(@RequestBody MemberBuyTicketOrderRequestDto request) {
		MemberBuyTicketOrderBean order = orderService.findById(request.getOrderId());
		return ApiResponse.success(order);
	}
	
	/**
	 * 查詢某個會員的所有訂單
	 * @param request
	 * @return
	 */
	@GetMapping("/member/")
	public ApiResponse<Object> getOrderByMemberId(@RequestBody MemberBuyTicketOrderRequestDto request) {
		return ApiResponse.success(orderService.findByMemberId(request.getMemberId()));
	}
	
}

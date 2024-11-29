package com.example.demo.controller.booking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.api.common.ApiResponse;
import com.example.demo.model.TicketTypeBean;
import com.example.demo.service.booking.TicketTypeService;

@RestController
@RequestMapping("/api/ticketType")
@CrossOrigin
public class TicketTypeController {
	@Autowired
	private TicketTypeService ticketTypeService;
	
	@PostMapping("/findAll")
	public ApiResponse<?> getAllTicketType() {
		List<TicketTypeBean> tickets = ticketTypeService.findAllType();
		return ApiResponse.success(tickets);
	}
}

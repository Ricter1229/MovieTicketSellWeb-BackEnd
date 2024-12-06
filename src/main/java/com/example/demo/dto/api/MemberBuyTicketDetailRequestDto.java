package com.example.demo.dto.api;

import java.util.List;

import lombok.Data;

@Data
public class MemberBuyTicketDetailRequestDto {
	private Integer orderId;
	private Integer storeId;
	private Integer auditoriumScheduleId;
	private List<String> seats;
}

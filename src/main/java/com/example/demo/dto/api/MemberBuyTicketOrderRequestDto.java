package com.example.demo.dto.api;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@Data
public class MemberBuyTicketOrderRequestDto {
	private Integer orderId;
	private Integer memberId;
	private Integer movieId;
	private BigDecimal totalAmount;
	private String status;
	private String action;
	private MemberBuyTicketDetailRequestDto orderDetail;
}

package com.example.demo.dto.api;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Data
public class SeatRequestDto {
	private Integer auditoriumScheduleId;  // 場次 ID
    private List<String> seats;    // 座位 A-11
    private String action;    // 操作類型（LOCK、RELEASE、PURCHASE）
    private Integer orderId;
}

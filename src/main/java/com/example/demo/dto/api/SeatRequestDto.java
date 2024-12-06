package com.example.demo.dto.api;

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
    private String seat;    // 座位 A-11
    private String action;    // 操作類型（LOCK、RELEASE、PURCHASE）
    private Integer orderId;
}

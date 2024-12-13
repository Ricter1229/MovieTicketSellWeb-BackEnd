package com.example.demo.dto.api;

import java.util.List;

import lombok.Data;

@Data
public class InsertSeatingListRequestDto {
	private Integer auditoriumId;
	private List<Integer> range;
	private List<Integer> walkwayPositionsX;
	private List<Integer> walkwayPositionsY;
	private List<String> nullSeats;
}

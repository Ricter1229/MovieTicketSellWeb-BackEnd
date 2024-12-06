package com.example.demo.dto.api;

import java.util.List;

import lombok.Data;

@Data
public class InsertSeatingListRequestDto {
	public List<Integer> range;
	public List<Integer> walkwayPositionsX;
	public List<Integer> walkwayPositionsY;
	public List<String> nullSeats;
}

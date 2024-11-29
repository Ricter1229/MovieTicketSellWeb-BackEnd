package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.api.common.ApiResponse;
import com.example.demo.dto.api.AuditoriumRequestDto;
import com.example.demo.dto.api.InsertSeatingListRequestDto;
import com.example.demo.service.SeatingService;

@RestController
@CrossOrigin
@RequestMapping("/api/Auditorium")
public class AuditoriumController {
	@Autowired
	private SeatingService seatingService;
	
	@PostMapping("/insertSeatingList")
	public ApiResponse<Object> createSeatingList(@RequestBody InsertSeatingListRequestDto request) {
		return ApiResponse.success(seatingService.insertSeatingList(1,request));
	}
	
	@PostMapping("/getSeatingList")
	public ApiResponse<Object> getSeatingList(@RequestBody AuditoriumRequestDto request) {
		return ApiResponse.success(seatingService.findSeatingList(request.getAuditoriumId()));
	}
}

package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.api.common.ApiResponse;
import com.example.demo.dto.AuditoriumScheduleRequestDto;
import com.example.demo.service.AuditoriumScheduleService;

@RestController
@CrossOrigin
@RequestMapping("/api/auditorium-schedule")
public class AuditoriumScheduleController {

	@Autowired
	private AuditoriumScheduleService auditoriumScheduleService;
	
	@PostMapping("/save-store")
	public ApiResponse<Object> saveStoreSchedule(@RequestBody AuditoriumScheduleRequestDto request) {

		return ApiResponse.success(auditoriumScheduleService.insert(request.getStoreId(), request.getMovies()));
	}
	@PostMapping("/get-auditorium-date")
	public ApiResponse<Object> getStoreSchedule(@RequestBody AuditoriumScheduleRequestDto request) {
		return ApiResponse.success(auditoriumScheduleService.findByAuditoriumId(request.getAuditoriumId(), request.getMovies()));
	}
}

package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.api.common.ApiResponse;
import com.example.demo.service.VersionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin
@RequestMapping("/api/version")
@RestController
public class VersionController {
	@Autowired
	private VersionService versionService;
	
	@GetMapping("/")
	public ApiResponse<Object> getAllVersion() {
		return ApiResponse.success(versionService.getAll());
	}
	
	@GetMapping("/aleast-one-movie")
	public ApiResponse<Object> getAleastOneMovie() {
		return ApiResponse.success(versionService.getAllVersionHaveAlestOneMovie());
	}
}

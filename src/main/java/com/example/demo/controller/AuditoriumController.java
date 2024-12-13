package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.api.common.ApiResponse;
import com.example.demo.dto.api.AuditoriumRequestDto;
import com.example.demo.dto.api.InsertSeatingListRequestDto;
import com.example.demo.service.AuditoriumService;
import com.example.demo.service.SeatingService;

@RestController
@CrossOrigin
@RequestMapping("/api/Auditorium")
public class AuditoriumController {
	@Autowired
	private SeatingService seatingService;
	@Autowired
	private AuditoriumService audService;

	@PostMapping("/insertSeatingList")
	public ApiResponse<Object> createSeatingList(@RequestBody InsertSeatingListRequestDto request) {
		return ApiResponse.success(seatingService.insertSeatingList(request));
	}

	@PostMapping("/getSeatingList")
	public ApiResponse<Object> getSeatingList(@RequestBody AuditoriumRequestDto request) {
		return ApiResponse.success(seatingService.findSeatingList(request.getAuditoriumId()));
	}

	@PostMapping("/insert/{id}")
	public String auditoriumInsert(@PathVariable Integer id, @RequestBody String jsonString) {
		JSONObject json = null;
		try {
			json = new JSONObject(jsonString);
			audService.updateAuditorium(id, json);

		} catch (JSONException e) {

			e.printStackTrace();
		}
		return json.toString();
	}
	
	@PostMapping("/getAll")
	public ApiResponse<Object> getAllAuditoriumWithStoreId(@RequestBody AuditoriumRequestDto request) {
		return ApiResponse.success(audService.findAllAuditoriumWithStoreId(request.getStoreId()));
	}
	
	@PostMapping("/insert-auditorium")
	public ApiResponse<Object> insertAuditorium(@RequestBody AuditoriumRequestDto request) {
		return ApiResponse.success(audService.insertAuditorium(request.getStoreId(), request.getAuditoriumList()));
	}
}

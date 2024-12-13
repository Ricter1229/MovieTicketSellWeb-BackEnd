package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.demo.domain.AuditoriumBean;
import com.example.demo.dto.api.InsertSeatingListRequestDto;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.AuditoriumRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class SeatingService {
	@Autowired
	private AuditoriumRepository auditoriumRepository;
	
	public Map<String, List<Map<String, Object>>> insertSeatingList(InsertSeatingListRequestDto request) {
		AuditoriumBean auditorium = auditoriumRepository.findById(request.getAuditoriumId())
				.orElseThrow(() -> new CustomException("Auditorium not found", 404));
		
        Map<String, List<Map<String, Object>>> seatingLayout = InsertSeating.generateSeatingLayout("seats", request);
        
        String seatingList = JSONObject.wrap(seatingLayout).toString();
        auditorium.setSeatingList(seatingList);
        auditoriumRepository.save(auditorium);
        
        return seatingLayout;
	}
	
	public Map<String, Object> findSeatingList(Integer auditoriumId) {
		AuditoriumBean auditorium = auditoriumRepository.findById(auditoriumId)
				.orElseThrow(() -> new CustomException("Auditorium not found", 404));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = null;
		try {
			map = objectMapper.readValue(auditorium.getSeatingList(), Map.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return map;
	}
}

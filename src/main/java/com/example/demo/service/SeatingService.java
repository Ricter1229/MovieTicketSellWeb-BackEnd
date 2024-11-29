package com.example.demo.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.AuditoriumRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class SeatingService {
	@Autowired
	private AuditoriumRepository auditoriumRepository;
	
	public Map<String, List<Map<String, Object>>> insertSeatingList() {
		Map<String, int[]> sections = new LinkedHashMap<>();
		//行含走道
        sections.put("seats", new int[]{10, 11}); // 單區域，3 行，每行 8 列
        // 設定走道位置（從 1 開始）
        List<Integer> walkwayPositions = List.of(3, 6, 9);

        // 生成座位表
        InsertSeating seatingList = new InsertSeating();
        Map<String, List<Map<String, Object>>> seatingLayout = seatingList.generateSeatingLayout(sections, walkwayPositions);
        
        return seatingLayout;
	}
}

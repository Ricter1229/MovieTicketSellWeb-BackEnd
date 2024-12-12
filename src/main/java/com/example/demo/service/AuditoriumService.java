package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.demo.domain.AuditoriumBean;
import com.example.demo.domain.StoreBean;
import com.example.demo.dto.api.AuditoriumRequestDto.AuditoriumDto;
import com.example.demo.repository.AuditoriumRepository;
import com.example.demo.repository.StoreRepository;
import com.example.demo.util.JsonToSomething;



@Service
public class AuditoriumService {
	@Autowired
	private AuditoriumRepository ar;
	@Autowired
	private StoreRepository sr;
	
	public AuditoriumBean updateAuditorium(Integer storeId,JSONObject json) {
		AuditoriumBean auditorium = JsonToSomething.jsonToAuditorium(json);
//		找的到廳的話
		if(auditorium.getId()!=null) {
			Optional<AuditoriumBean> audOpt = ar.findById(auditorium.getId());
			AuditoriumBean aud = audOpt.get();
			if(aud!=null) {
				return aud;
			}
		}
//		找不到廳
		
		Optional<StoreBean> storeOpt=sr.findById(storeId);
		StoreBean store = storeOpt.get();
		if(store!=null) {
			auditorium.setStore(store);
			AuditoriumBean aud=ar.save(auditorium);
			return aud;
		}
		return null;
	}
	
	public List<AuditoriumBean> findAllAuditoriumWithStoreId(Integer storeId) {
		System.out.println(storeId);
		return ar.findByStoreId(storeId);
	}
	
	public List<AuditoriumBean> insertAuditorium(Integer storeId, List<AuditoriumDto> request) {
		List<AuditoriumBean> auditoriumBeans = new ArrayList<>();
		
		StoreBean store = sr.findById(storeId).get();
		for(AuditoriumDto dto : request) {
			AuditoriumBean auditorium = new AuditoriumBean();
			auditorium.setName(dto.getAuditoriumName());
			auditorium.setStoreId(storeId);
			auditorium.setStore(store);
			auditoriumBeans.add(auditorium);
		}
		return ar.saveAll(auditoriumBeans);
	}
}

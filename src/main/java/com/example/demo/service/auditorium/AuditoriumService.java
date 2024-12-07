package com.example.demo.service.auditorium;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.demo.domain.AuditoriumBean;
import com.example.demo.domain.StoreBean;
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
}

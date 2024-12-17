package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.demo.domain.AuditoriumBean;
import com.example.demo.domain.StoreBean;
import com.example.demo.domain.StoreSubPhotoBean;
import com.example.demo.dto.api.AuditoriumRequestDto.AuditoriumDto;
import com.example.demo.dto.api.AuditoriumsDto;
import com.example.demo.repository.AuditoriumRepository;
import com.example.demo.repository.StoreRepository;
import com.example.demo.util.JsonToSomething;



@Service
public class AuditoriumService {
	@Autowired
	private AuditoriumRepository ar;
	@Autowired
	private StoreRepository sr;
	
	public List<com.example.demo.dto.api.AuditoriumsDto.AuditoriumDto> updateAuditorium(Integer storeId,JSONObject json) {
		List<com.example.demo.dto.api.AuditoriumsDto.AuditoriumDto> auditoriumList = JsonToSomething.jsonToAuditorium(json).getAuditoriumList();

//		try {
//			JSONArray array=json.isNull("auditoriums")?null:json.getJSONArray("auditoriums");
////			System.out.println("111");
//			if(array!=null) {
//				for (int i = 0; i < array.length(); i++) {
//					AuditoriumsDto.AuditoriumDto auditoriumDto = new AuditoriumsDto.AuditoriumDto();
//					JSONObject nameObject = array.getJSONObject(i);
//					String auditoriumId=nameObject.isNull("auditoriumId")?null:nameObject.getString("auditoriumId");
//					String auditoriumName=nameObject.isNull("auditoriumName")?null:nameObject.getString("auditoriumName");
//					auditoriumDto.setAuditoriumName(auditoriumName);
//					auditoriumDto.setAuditoriumId(auditoriumId);
//					auditoriumList.add(auditoriumDto);
//				}
//			}
//			System.out.println("22222");
//		System.out.println(auditoriumList);
		if(storeId!=null) {
			Optional<StoreBean> storeOpt = sr.findById(storeId);
			if(storeOpt.isPresent()) {
				StoreBean store = storeOpt.get();
				
				for (AuditoriumsDto.AuditoriumDto auditoriumDto : auditoriumList) {
					if(auditoriumDto.getAuditoriumId().length()!=0) {
						System.out.println("123");
						System.out.println(auditoriumDto);
						Optional<AuditoriumBean> auditoriumOpt = ar.findById(Integer.parseInt(auditoriumDto.getAuditoriumId()));
						if(auditoriumOpt.isPresent()) {
							AuditoriumBean auditoriumBean = auditoriumOpt.get();
							auditoriumBean.setName(auditoriumDto.getAuditoriumName());
							ar.save(auditoriumBean);
						}else {

							AuditoriumBean auditoriumBean = new AuditoriumBean();
							auditoriumBean.setName(auditoriumDto.getAuditoriumName());
							auditoriumBean.setStore(store);
							ar.save(auditoriumBean);
						}
						
					}else {

						AuditoriumBean auditoriumBean = new AuditoriumBean();
						auditoriumBean.setName(auditoriumDto.getAuditoriumName());
						auditoriumBean.setStore(store);
						ar.save(auditoriumBean);
						
					}
						
					
				}
//				for(int i=0;i<auditoriumList.size();i++) {
//					System.out.println("c");
//					if(i<store.getAuditoriumBeans().size()) {
//						System.out.println("a");
//						int auditoriumId = Integer.parseInt(auditoriumList.get(i).getAuditoriumId());
//						System.out.println("a1");
//						if(store.getAuditoriumBeans().get(i).getId()==auditoriumId) {
//							System.out.println("a2");
//							store.getAuditoriumBeans().get(i).setName(auditoriumList.get(i).getAuditoriumName());
//						}
//					}else {
//						System.out.println("b");
//						AuditoriumBean auditoriumBean = new AuditoriumBean();
//						auditoriumBean.setName(auditoriumList.get(i).getAuditoriumName());
//						auditoriumBean.setStore(store);
//					}
//					System.out.println("a3");
//					AuditoriumRepository.save(auditoriumBean);
//				}
			}
			
			return auditoriumList;
		}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return null;
	}
	
//	public AuditoriumBean updateAuditorium(Integer storeId,JSONObject json) {
//		AuditoriumBean auditorium = JsonToSomething.jsonToAuditorium(json);
////		找的到廳的話
//		if(auditorium.getId()!=null) {
//			Optional<AuditoriumBean> audOpt = ar.findById(auditorium.getId());
//			AuditoriumBean aud = audOpt.get();
//			if(aud!=null) {
//				return aud;
//			}
//		}
////		找不到廳
//		
//		Optional<StoreBean> storeOpt=sr.findById(storeId);
//		StoreBean store = storeOpt.get();
//		if(store!=null) {
//			auditorium.setStore(store);
//			AuditoriumBean aud=ar.save(auditorium);
//			return aud;
//		}
//		return null;
//	}
	
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
			auditorium.setStore(store);
			auditoriumBeans.add(auditorium);
		}
		return ar.saveAll(auditoriumBeans);
	}
	
	public List<AuditoriumBean> insertAuditoriumByDto(Integer storeId, AuditoriumsDto request) {
		List<AuditoriumBean> auditoriumBeans = new ArrayList<>();
		Optional<StoreBean> byId = sr.findById(storeId);
		if(byId.isPresent()) {
			StoreBean store = byId.get();
			
			for(AuditoriumsDto.AuditoriumDto dto : request.getAuditoriumList()) {
				AuditoriumBean auditorium = new AuditoriumBean();
				auditorium.setName(dto.getAuditoriumName());
				auditorium.setStore(store);
				auditoriumBeans.add(auditorium);
			}
			return ar.saveAll(auditoriumBeans);
		}
		return null;
	}
	
	public void deleteAuditoriumByIds(String jsons) {
		System.out.println("刪除1");
		System.out.println(jsons);
		System.out.println(jsons!=null);
		System.out.println(jsons.length()!=0);
		System.out.println(jsons!=null&&jsons.length()!=0);
		if(jsons!=null&&jsons.length()!=0) {
			try {
				JSONArray jsonArray = new JSONArray(jsons);
				System.out.println("jsonArray");
				System.out.println(jsonArray.toString());
				for(int i=0;i<jsonArray.length();i++) {
					int id=jsonArray.getInt(i);
					if(ar.existsById(id)) {
						ar.deleteById(id);
						
					}
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}

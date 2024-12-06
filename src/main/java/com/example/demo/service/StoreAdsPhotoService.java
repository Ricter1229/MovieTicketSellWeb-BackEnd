package com.example.demo.service;

import java.util.Base64.Encoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.StoreAdsPhotoBean;
import com.example.demo.dto.StoreAdsPhotoResponse;
import com.example.demo.dto.api.PhotoTypeDto;
import com.example.demo.dto.api.StoreAdsPhotoDto;
import com.example.demo.repository.StoreAdsPhotoRepository;
import com.example.demo.util.PhotoTurn;
import com.nimbusds.jose.shaded.gson.JsonObject;

import jakarta.transaction.Transactional;
@Transactional
@Service
public class StoreAdsPhotoService {
	@Autowired
	private StoreAdsPhotoRepository StoreAdsPhotoRepo;
	
	public List<StoreAdsPhotoDto> findAdsAll() {
		List<StoreAdsPhotoBean> adsAllSorted = StoreAdsPhotoRepo.findAdsAllSorted();
		List<StoreAdsPhotoDto> stos=new ArrayList<>();
		Base64.Encoder encoder = Base64.getEncoder();
		if(adsAllSorted.size()!=0) {
			for (StoreAdsPhotoBean storeAdsPhotoBean : adsAllSorted) {
				
				StoreAdsPhotoDto dto = new StoreAdsPhotoDto();
				dto.setId(storeAdsPhotoBean.getId());
				String adPhoto = encoder.encodeToString(storeAdsPhotoBean.getPhoto());
				adPhoto=storeAdsPhotoBean.getMimeType()+adPhoto;
				dto.setPhoto(adPhoto);
				dto.setSortId(storeAdsPhotoBean.getSortId());
				stos.add(dto);
			}
		}
		return stos;
	}
	
	public Integer insertAdss(String json) {
		Integer count=0;
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray=jsonObject.isNull("ads")? null:jsonObject.getJSONArray("ads");
			if(jsonArray!=null) {
				System.out.println("insertAdss的jsonArray");
				System.out.println("jsonArray.length()");
				System.out.println(jsonArray.length());
				for(int i=0;i<jsonArray.length();i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					Integer id=null;
					String photo=jsonObj.isNull("photo")?null:jsonObj.getString("photo");
					Integer sortId=jsonObj.isNull("sortId")? null:jsonObj.getInt("sortId");
					if(sortId!=null) {
						System.out.println("sortedIdExist(sortId)");
						System.out.println(sortedIdExist(sortId));
						if(sortedIdExist(sortId)) {
							continue;
						}
					}
					StoreAdsPhotoBean storeAdsPhotoBean = new StoreAdsPhotoBean(); 
					if(photo!=null) {
						PhotoTypeDto byteDto = PhotoTurn.base64ToByte(photo);
						
						storeAdsPhotoBean.setMimeType(byteDto.getMimeType());
						storeAdsPhotoBean.setPhoto(byteDto.getPhoto());
					}
					storeAdsPhotoBean.setId(id);
					storeAdsPhotoBean.setSortId(sortId);
					System.out.println("執行前");
					StoreAdsPhotoRepo.save(storeAdsPhotoBean);
					count++;
				}
				return count;
			}
		
		return 0;
	}
	
	public Boolean sortedIdExist(Integer sortedId) {
		if(StoreAdsPhotoRepo.findAdsBySortedId(sortedId).size()!=0) {
			return true;
		}
		return false;
	}
	
	public Integer removeById(List<Integer> ids) {
		Integer count=0;
		if(ids!=null&&ids.size()!=0) {
			for (Integer id : ids) {
				
				if(exist(id)) {
					StoreAdsPhotoRepo.deleteById(id);
					count++;
				}
				
			}
		}
		return count;
	}
	
	public boolean exist(Integer id) {
		if(id!=null) {
			
			return StoreAdsPhotoRepo.existsById(id);
		}
		return false;
	}
	
	public Integer deleteAdds(List<Integer> integers) {
		Integer count=0;
		if(integers.size()!=0) {
			count = removeById(integers);
			if(count!=0) {
			}
		}
		return count;
		
	}
	
	public List<Integer> getIntegersByJson(String json){
		List<Integer> ids=new ArrayList<>();
		if(json!=null && json.length()!=0) {
			System.out.println("222");
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray=jsonObject.isNull("ads")? null:jsonObject.getJSONArray("ads");
			System.out.println("jsonArray!=null");
			System.out.println(jsonArray!=null);
			if(jsonArray!=null) {
				System.out.println("jsonArray.length()");
				System.out.println(jsonArray.length());
//				System.out.println("jsonArray.toString()");
//				System.out.println(jsonArray.toString());
				for(int i=0;i<jsonArray.length();i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					Integer id=jsonObj.isNull("id")? null:jsonObj.getInt("id");
					ids.add(id);
				}
				
			}

			
		}
		return ids;
	}
	public List<Integer> getIntegersByJsonDelete(String json){
		List<Integer> ids=new ArrayList<>();
		if(json!=null && json.length()!=0) {
			System.out.println("dads");
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray=jsonObject.isNull("ads")? null:jsonObject.getJSONArray("ads");
			System.out.println("jsonArray!=null");
			System.out.println(jsonArray!=null);
			if(jsonArray!=null) {
				System.out.println("jsonArray.length()");
				System.out.println(jsonArray.length());
//				System.out.println("jsonArray.toString()");
//				System.out.println(jsonArray.toString());
				for(int i=0;i<jsonArray.length();i++) {
					Integer id=jsonArray.getInt(i);
					if(id!=null) {
						ids.add(id);
					}
				}
				
			}

			
		}
		return ids;
	}
}

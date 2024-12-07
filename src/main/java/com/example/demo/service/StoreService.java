package com.example.demo.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.demo.domain.StoreBean;
import com.example.demo.domain.StoreSubPhotoBean;
import com.example.demo.dto.api.PhotoTypeDto;
import com.example.demo.dto.api.RegionDto;
import com.example.demo.dto.api.StoreFindDto;
import com.example.demo.dto.api.StoreInnerDto;
import com.example.demo.dto.api.StoreOuterDto;
import com.example.demo.dto.api.StoreSubPhotoDto;
import com.example.demo.repository.StoreRepository;
import com.example.demo.util.PhotoTurn;

@Service
public class StoreService {
	@Autowired
	private StoreRepository sr;
	
	
	public StoreBean findById(Integer id) {
		if(id!=null) {
			Optional<StoreBean> optional = sr.findById(id);
			if(optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}
	
	public List<StoreBean> findStoreByRegion(Integer region){
		List<StoreBean> stores = sr.findStoreByRegion(region);
		return stores;
	}
	
	public List<Integer> findRegions() {
		return sr.findDistinctRegions();
	}
	
	public Integer getStoreIdByName(String name) {
		List<StoreBean> stores = sr.findStoreByName(name);
		return stores.get(0).getId();
	}
	
	public long count(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			return sr.count(jsonObject);
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<StoreBean> find(String json){
		try {
			JSONObject jsonObject = new JSONObject(json);
			return sr.find(jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean exists(Integer id) {
		if(id!=null) {
			return sr.existsById(id);
		}
		return false;
	}
	
	public boolean remove(Integer id) {
		if(id!=null && sr.existsById(id)) {
			sr.deleteById(id);
			return true;
		}
		return false;
	}
	
//	public StoreBean updateStore(StoreBean store) {
//		
//		List<StoreBean> stores = sr.findStoreByName(store.getName());
//		System.out.println("stores.size()"+stores.size());
//		if(stores.size()==0) {
//			StoreBean sto=sr.save(store);
//			System.out.println("sto:"+sto);
//			sto.setId(sr.findStoreByName(sto.getName()).get(0).getId());
//			return sto;          
//		}
//		return null;
//}
	public StoreBean insertStore(StoreBean store) {
		if(store!=null) {
			return sr.save(store);
		}
		return null;
	}
	public StoreBean storeModify(JSONObject obj) {
		try {
			
			Integer id=obj.isNull("storeId")? null:obj.getInt("storeId");
			String name=obj.isNull("name")? null:obj.getString("name");
			Integer region=obj.isNull("region")? null:obj.getInt("region");
			String address=obj.isNull("address")? null:obj.getString("address");
			String phoneNo=obj.isNull("phoneNo")? null:obj.getString("phoneNo");
			String mainPhotoStr=obj.isNull("mainPhoto")? null:obj.getString("mainPhoto");
			String introduce=obj.isNull("introduce")? null:obj.getString("introduce");
			String position=obj.isNull("position")? null:obj.getString("position");
			
			PhotoTypeDto mainPhotoDto = PhotoTurn.base64ToByte(mainPhotoStr);
			
			
			System.out.println("蝦蝦蝦");

			if(mainPhotoDto!=null) {
				System.out.println("mainPhotoStr的mimestr:"+mainPhotoDto.getMimeType());
				
			}
			System.out.println("id");
			System.out.println(id);
			if (id != null) {
				Optional<StoreBean> optional = sr.findById(id);
				System.out.println("optional");
				System.out.println(optional);
				if(optional.isPresent()) {
					StoreBean update = optional.get();
					update.setName(name);
					update.setRegion(region);
					update.setAddress(address);
					update.setPhoneNo(phoneNo);
					if(mainPhotoDto!=null) {
						update.setMainPhoto(mainPhotoDto.getPhoto());
						update.setMimeType(mainPhotoDto.getMimeType());
						
					}
					update.setIntroduce(introduce);
					update.setPosition(position);
					return sr.save(update);
				}
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public StoreFindDto setStoreToDto(StoreBean store) {
		StoreFindDto dto=new StoreFindDto();
		dto.setStoreId(store.getId());
		dto.setName(store.getName());
		dto.setRegion(store.getRegion());
		dto.setAddress(store.getAddress());
		dto.setPhoneNo(store.getPhoneNo());
		
		Base64.Encoder encoder = Base64.getEncoder();
		System.out.println(store.getMainPhoto());
		if(store.getMainPhoto()!=null) {
			String mainPhoto = encoder.encodeToString(store.getMainPhoto());
			String mimeType=store.getMimeType();
			mainPhoto=mimeType+mainPhoto;
			dto.setMainPhoto(mainPhoto);
		}
		System.out.println("456");
		dto.setIntroduce(store.getIntroduce());
		dto.setPosition(store.getPosition());
		dto.setStoreSubPhotoDtos(setStoreSubPhotoDtos(store));
		return dto;
		
	}
	
	public StoreInnerDto setStoreToInnerDto(StoreBean store) {
		StoreInnerDto dto=new StoreInnerDto();
		dto.setStoreId(store.getId());
		dto.setName(store.getName());
		dto.setAddress(store.getAddress());
		dto.setPhoneNo(store.getPhoneNo());
		
		Base64.Encoder encoder = Base64.getEncoder();
		if(store.getMainPhoto()!=null) {
			String mainPhoto = encoder.encodeToString(store.getMainPhoto());
			String mimeType=store.getMimeType();
			mainPhoto=mimeType+mainPhoto;
			dto.setMainPhoto(mainPhoto);
		}
		Integer region = store.getRegion();
		
		if(region!=null) {
			RegionDto regionRes=new RegionDto();
			regionRes.setRegionNum(region);
			switch (region) {
			case 1:regionRes.setRegion("北部");
			break;
			case 2:regionRes.setRegion("中部");
			break;
			case 3:regionRes.setRegion("南部");
			break;
			case 4:regionRes.setRegion("東部");
			break;
			}
			dto.setRegionDto(regionRes);
		}
		dto.setIntroduce(store.getIntroduce());
		dto.setPosition(store.getPosition());
		dto.setStoreSubPhotoDtos(setStoreSubPhotoDtos(store));
		return dto;
		
	}
	
	public StoreOuterDto setStoreToOuterDto(StoreBean store) {
		StoreOuterDto dto=new StoreOuterDto();
		dto.setStoreId(store.getId());
		dto.setName(store.getName());
		dto.setAddress(store.getAddress());
		dto.setPhoneNo(store.getPhoneNo());
		
		Base64.Encoder encoder = Base64.getEncoder();
		if(store.getMainPhoto()!=null) {
			String mainPhoto = encoder.encodeToString(store.getMainPhoto());
			String mimeType=store.getMimeType();
			mainPhoto=mimeType+mainPhoto;
			dto.setMainPhoto(mainPhoto);
		}

		dto.setStoreSubPhotoDtos(setStoreSubPhotoDtos(store));
		return dto;
		
	}
	public StoreSubPhotoDto[] setStoreSubPhotoDtos(StoreBean store) {
		
		List<StoreSubPhotoDto> dtos=new ArrayList<>();
		List<StoreSubPhotoBean> storeSubPhotos = store.getStoreSubPhoto();
		//獲取編碼器
		Base64.Encoder encoder = Base64.getEncoder();
		for (StoreSubPhotoBean storeSubPhotoBean : storeSubPhotos) {
			StoreSubPhotoDto dto=new StoreSubPhotoDto();
			dto.setId(storeSubPhotoBean.getId());
			if(storeSubPhotoBean.getPhoto()!=null) {
				String photo = encoder.encodeToString(storeSubPhotoBean.getPhoto());
				String mimeType = storeSubPhotoBean.getMimeType();
				photo=mimeType+photo;
				dto.setPhoto(photo);
			}
			dtos.add(dto);
		}
		System.out.println(dtos.size());
		return dtos.toArray(new StoreSubPhotoDto[0]);
	}
	
}

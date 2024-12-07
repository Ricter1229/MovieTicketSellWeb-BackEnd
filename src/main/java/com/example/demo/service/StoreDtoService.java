package com.example.demo.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONObject;

import com.example.demo.domain.StoreBean;
import com.example.demo.domain.StoreSubPhotoBean;
import com.example.demo.dto.api.StoreFindDto;
import com.example.demo.dto.api.StoreSubPhotoDto;
import com.example.demo.util.JsonToSomething;

public class StoreDtoService {
	
	public StoreFindDto findStoreDto(StoreBean storeBean,List<StoreSubPhotoBean> StoreSubPhotoBeans) {
		StoreFindDto storeDto=new StoreFindDto();
		storeDto.setStoreId(storeBean.getId());
		storeDto.setName(storeBean.getName());
		storeDto.setRegion(storeBean.getRegion());
		storeDto.setAddress(storeBean.getAddress());
		storeDto.setPhoneNo(storeBean.getPhoneNo());
		//取出資料庫byte[]轉成base64
		Base64.Encoder encoder = Base64.getEncoder();
        String setMainPhotoStr = encoder.encodeToString(storeBean.getMainPhoto());
		storeDto.setMainPhoto(setMainPhotoStr);
		storeDto.setIntroduce(storeBean.getIntroduce());
		storeDto.setStoreId(null);
		
		
		List<String> subPhotos=new ArrayList<>();
		for (StoreSubPhotoBean storeSubPhotoBean : StoreSubPhotoBeans) {
			StoreSubPhotoDto storeSubPhotoDto=new StoreSubPhotoDto();
			storeSubPhotoDto.setId(storeSubPhotoBean.getId());
			String setSubPhoto=new String();
			setSubPhoto = encoder.encodeToString(storeSubPhotoBean.getPhoto());
			storeSubPhotoDto.setPhoto(setSubPhoto);
			subPhotos.add(setSubPhoto);
		}
		storeDto.setStoreSubPhotoDtos(subPhotos.toArray(new StoreSubPhotoDto[0]));
		return storeDto;
	}
}

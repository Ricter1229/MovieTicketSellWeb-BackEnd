package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;


import com.example.demo.domain.AuditoriumBean;
import com.example.demo.domain.StoreBean;
import com.example.demo.domain.StoreSubPhotoBean;
import com.example.demo.repository.AuditoriumRepository;
import com.example.demo.repository.StoreRepository;
import com.example.demo.repository.StoreSubPhotoRepository;
import com.example.demo.util.JsonToSomething;

import jakarta.transaction.Transactional;


@Transactional
@Service
public class StoreSubPhotoService {
	@Autowired
	private StoreSubPhotoRepository subPhotoRepo;
	@Autowired
	private StoreRepository sr;
	
	public boolean deleteSubPhoto(Integer id) {
		if(id!=null && subPhotoRepo.existsById(id)) {
			subPhotoRepo.deleteById(id);
			return true;
		}
		return false;
	}
	public boolean exists(Integer id) {
		if(id!=null) {
			return subPhotoRepo.existsById(id);
		}
		return false;
	}
	
	public void deleteStoreSubPhoto(Integer storeId,List<Integer> subIds) {
		List<StoreSubPhotoBean> storeSubPhotoOld=new ArrayList<>();
		StoreBean store=new StoreBean();
//		找的到附圖的話
		Optional<StoreBean> storeOpt = sr.findById(storeId);
		System.out.println("1234");
		System.out.println("storeId"+storeId);
		if(storeId!=null){
			if(storeOpt.isPresent()) {
				store = storeOpt.get();
				System.out.println("photo是"+store.getStoreSubPhoto());
				//從store拿到的
				storeSubPhotoOld=store.getStoreSubPhoto();
			}
			//每個storeSubPhoto拆開執行
			System.out.println("3040");
			if(storeSubPhotoOld!=null&& !storeSubPhotoOld.isEmpty()) {
				System.out.println("ccc");
				for (Integer subId : subIds) {
					System.out.println("subId"+subId);
					subPhotoRepo.deleteStoreSubByStoreId(subId);
				}
			}
			
		}


	}
	public List<StoreSubPhotoBean> updateStoreSubPhoto(Integer storeId,List<StoreSubPhotoBean> storeSubPhotos) {
		StoreBean store=new StoreBean();
//		找的到附圖的話
		Optional<StoreBean> storeOpt = sr.findById(storeId);
		System.out.println("updateStoreSubPhoto的storeId"+storeId);
		if(storeId!=null) {
			if(storeOpt.isPresent()) {
				store = storeOpt.get();
				System.out.println("photo是"+store.getStoreSubPhoto());
				//從store拿到的
				
			}
			//每個storeSubPhoto拆開執行
			System.out.println("aaa");
			System.out.println("bbb");
			System.out.println("storeSubPhotos"+storeSubPhotos.size());
			for (StoreSubPhotoBean storeSubPhotoBean : storeSubPhotos) {
				storeSubPhotoBean.setStore(store);
				storeSubPhotoBean.setId(null);
				subPhotoRepo.save(storeSubPhotoBean);
			}
			return storeSubPhotos;
		}
		return null;
	}
}

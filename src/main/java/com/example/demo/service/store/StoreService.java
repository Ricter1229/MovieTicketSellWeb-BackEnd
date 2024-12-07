package com.example.demo.service.store;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.demo.domain.StoreBean;
import com.example.demo.repository.StoreRepository;
import com.example.demo.util.JsonToSomething;
@Service
public class StoreService {
	@Autowired
	private StoreRepository sr;
	
	

	@Value("${store.mainPhoto.path}")
	private String photoPath;
	
	public StoreBean updateStore(JSONObject json) {
		try {
			StoreBean store=JsonToSomething.jsonToStore(json);
			
			File file=new File(photoPath);
            byte[] mainPhoto = Files.readAllBytes(file.toPath());
            
            store.setMainPhoto(mainPhoto);
            	StoreBean sto=sr.save(store);
            	System.out.println("sto:"+sto);
            	return sto;
            
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public List<StoreBean> findStoreByRegion(Integer region){
		List<StoreBean> stores = sr.findStoreByRegion(region);
		return stores;
	}
	
	public List<String> findRegions() {
		return sr.findDistinctRegions();
	}
		
	}

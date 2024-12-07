package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.StoreBean;
import com.example.demo.service.store.StoreService;


@RequestMapping("/store")
@CrossOrigin
@RestController
public class StoreController {
	@Autowired
	private StoreService stoService;
	
	
//	傳一個string進來回傳原本的json
	@PostMapping("/insert")
	public String storeInsert(@RequestBody String jsonString) {
		JSONObject json=null;
		try {
			json = new JSONObject(jsonString);
			stoService.updateStore(json);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
		
	}
	
	
	@GetMapping("/region/{region}")
	public List<StoreBean> findStoresByRegion(@PathVariable Integer region) {
		List<StoreBean> stores = stoService.findStoreByRegion(region);
		for (StoreBean store : stores) {
			
			System.out.println(store.getName());
		}
		return stores;
	}
	
	@GetMapping("findallregions")
	public List<String> findAllRegions() {
		List<String> regions = stoService.findRegions();
		return regions;
	}
	
	
}

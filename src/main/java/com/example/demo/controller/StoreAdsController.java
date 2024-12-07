package com.example.demo.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.StoreAdsPhotoResponse;
import com.example.demo.dto.api.StoreAdsPhotoDto;

import com.example.demo.service.StoreAdsPhotoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin
@RequestMapping("/storeads") 
@RestController
public class StoreAdsController {
	@Autowired
	private  StoreAdsPhotoService sas;
	@GetMapping("/allads")
	public StoreAdsPhotoResponse findAllAds() {
		List<StoreAdsPhotoDto> adsAll = sas.findAdsAll();
		return new StoreAdsPhotoResponse(0,adsAll,true,"新增成功");
	}
	
	@PostMapping("/insertadds")
	public StoreAdsPhotoResponse insertAdds(@RequestBody String json) {
		if(json!=null&&json.length()!=0) {
			System.out.println("aaa");
			System.out.println(json);
			Integer insertAdsCount = sas.insertAdss(json);
			if(insertAdsCount!=0) {
				return new StoreAdsPhotoResponse(insertAdsCount,null,true,"新增成功");
			}
			return new StoreAdsPhotoResponse(0,null,false,"新增失敗");
		}
		return new StoreAdsPhotoResponse(0,null,false,"新增失敗，資料無傳入");
	}
	
	@PutMapping("/update")
	public StoreAdsPhotoResponse updateAdds(@RequestBody String json) {

		List<Integer> integers = sas.getIntegersByJson(json);
		sas.deleteAdds(integers);
		System.out.println("json.length()");
		System.out.println("?");
		if(json!=null&json.length()!=0) {
			Integer insertAdsCount = sas.insertAdss(json);
			if(insertAdsCount!=0) {
				return new StoreAdsPhotoResponse(insertAdsCount,null,true,"更新成功");
			}
			return new StoreAdsPhotoResponse(0,null,false,"更新失敗");
		}
		return new StoreAdsPhotoResponse(0,null,false,"更新失敗，資料無傳入");
	}
	
	//刪除全部傳入的dto
	@DeleteMapping("/deleteadds")
	public StoreAdsPhotoResponse deleteAdds(@RequestBody String json) {
		System.out.println("123");
		System.out.println(json);
		List<Integer> integers = sas.getIntegersByJsonDelete(json);
		Integer count = sas.deleteAdds(integers);
		if(count!=0) {
			return new StoreAdsPhotoResponse(count,null,true,"刪除成功");	
		}
		return new StoreAdsPhotoResponse(0,null,false,"刪除失敗");	
	}
	
	
}

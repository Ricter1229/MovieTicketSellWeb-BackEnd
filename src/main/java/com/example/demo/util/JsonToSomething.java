package com.example.demo.util;



import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import com.example.demo.domain.AuditoriumBean;
import com.example.demo.domain.StoreBean;
import com.example.demo.domain.StoreSubPhotoBean;
import com.example.demo.dto.api.PhotoTypeDto;



public class JsonToSomething {
	public static StoreBean jsonToStore(JSONObject json) {
			
			StoreBean store=new StoreBean();
			try {
				
				Integer id=json.isNull("storeId")? null:json.getInt("storeId");
				String name=json.isNull("name")? null:json.getString("name");
				Integer region=json.isNull("region")? null:json.getInt("region");
				String address=json.isNull("address")? null:json.getString("address");
				System.out.println("address的值是"+address);
				String phoneNo=json.isNull("phoneNo")? null:json.getString("phoneNo");
				String mainPhotoStr=json.isNull("mainPhoto")? null:json.getString("mainPhoto");
				String introduce=json.isNull("introduce")? null:json.getString("introduce");
				String position=json.isNull("position")? null:json.getString("position");
				
				PhotoTypeDto mainPhotoDto = PhotoTurn.base64ToByte(mainPhotoStr);
				
					if(mainPhotoDto!=null) {
						store.setMainPhoto(mainPhotoDto.getPhoto());
						store.setMimeType(mainPhotoDto.getMimeType());
					}
	            	store.setId(id);
	            	store.setName(name);
	            	store.setRegion(region);
	            	store.setAddress(address);
	            	store.setPhoneNo(phoneNo);
	            	store.setIntroduce(introduce);
	            	store.setPosition(position);
	            	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("報錯");
				e.printStackTrace();
			}
			return store;
		}
		
	public static List<StoreSubPhotoBean> jsonToSubPhotos(JSONObject json) {
		
		List<StoreSubPhotoBean> subPhotoList=new ArrayList<>();

		try {
			//拿到json陣列
			
			JSONArray storeSubPhotoDtos = json.isNull("storeSubPhotoDtos")?null:json.getJSONArray("storeSubPhotoDtos");
		
			Base64.Decoder decoder = Base64.getDecoder();
			if(storeSubPhotoDtos!=null) {
				
				for (int i = 0; i < storeSubPhotoDtos.length(); i++) {
					StoreSubPhotoBean subPhoto=new StoreSubPhotoBean();
					// 将 JSONArray 中的每个元素转换为 JSONObject
					JSONObject photoObj = storeSubPhotoDtos.getJSONObject(i);
					// 获取 photo 字段的值
					
					String photo = photoObj.isNull("photo")? null:photoObj.getString("photo");
					
					PhotoTypeDto subPhotoDto = PhotoTurn.base64ToByte(photo);
					
					
					
	
					System.out.println();
										
					System.out.println("photo的mimestr:"+subPhotoDto.getMimeType());
					Integer id=photoObj.isNull("subPhotoId")? null:photoObj.getInt("subPhotoId");
					subPhoto.setId(id);
					if(subPhotoDto!=null) {
						subPhoto.setPhoto(subPhotoDto.getPhoto());
						subPhoto.setMimeType(subPhotoDto.getMimeType());
					}
					subPhotoList.add(subPhoto);
				}
			}


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return subPhotoList;
	}
	
		public static AuditoriumBean jsonToAuditorium(JSONObject json) {
				
				AuditoriumBean audi=new AuditoriumBean();
				try {
					
					Integer id=json.isNull("id")? null:json.getInt("id");
	
					String auditoriumName=json.isNull("auditoriumName")? null:json.getString("auditoriumName");
	
					audi.setId(id);
					audi.setName(auditoriumName);
	
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return audi;
			}
}

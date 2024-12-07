package com.example.demo.util;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import com.example.demo.domain.AuditoriumBean;
import com.example.demo.domain.StoreBean;

public class JsonToSomething {
	public static StoreBean jsonToStore(JSONObject json) {
			
			StoreBean store=new StoreBean();
			try {
				
				Integer id=json.isNull("id")? null:json.getInt("id");
				String name=json.isNull("name")? null:json.getString("name");
				Integer region=json.isNull("region")? null:json.getInt("region");
				String address=json.isNull("address")? null:json.getString("address");
				String phoneNo=json.isNull("phoneNo")? null:json.getString("phoneNo");
	//			byte[] mainPhoto=json.isNull("mainPhoto")? null:json.getString("mainPhoto").getBytes();
				String introduce=json.isNull("introduce")? null:json.getString("introduce");
				String position=json.isNull("position")? null:json.getString("position");
				
				if(name!=null&& region!=null&& address!=null&& introduce!=null&& position!=null) {
	            	store.setId(id);
	            	store.setName(name);
	            	store.setRegion(region);
	            	store.setAddress(address);
	            	store.setPhoneNo(phoneNo);
	            	store.setIntroduce(introduce);
	            	store.setPosition(position);
	            	}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return store;
		}
		
		public static AuditoriumBean jsonToAuditorium(JSONObject json) {
				
				AuditoriumBean audi=new AuditoriumBean();
				try {
					
					Integer id=json.isNull("id")? null:json.getInt("id");
	
					String auditoriumName=json.isNull("auditoriumName")? null:json.getString("auditoriumName");
	
					audi.setId(id);
					audi.setAuditoriumName(auditoriumName);
	
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return audi;
			}
}

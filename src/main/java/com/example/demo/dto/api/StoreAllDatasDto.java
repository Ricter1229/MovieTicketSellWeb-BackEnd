package com.example.demo.dto.api;

import lombok.Data;

@Data
public class StoreAllDatasDto {
	private Integer id;
	private String name;
	private Integer region;
	private String address;
	private String phoneNo;
	private byte[] mainPhoto;
	private String introduce;
	private String position;
}

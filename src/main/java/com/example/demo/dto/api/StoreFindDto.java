package com.example.demo.dto.api;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.domain.StoreSubPhotoBean;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Component
@Data
public class StoreFindDto {
	private Integer storeId;
	private String name;
	private Integer region;
	private String address;
	private String phoneNo;
	private String mainPhoto;
	private String introduce;
	private String position;
	private	StoreSubPhotoDto[] storeSubPhotoDtos;

//	private String auditoriumName;
	
}

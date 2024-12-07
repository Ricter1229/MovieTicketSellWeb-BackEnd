package com.example.demo.dto.api;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.domain.StoreSubPhotoBean;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
//影城外部頁面dto
@Component
@Data
public class StoreOuterDto {
	private Integer storeId;
	private String name;
	private String address;
	private String phoneNo;
	private String mainPhoto;
	private	StoreSubPhotoDto[] storeSubPhotoDtos;

	
}

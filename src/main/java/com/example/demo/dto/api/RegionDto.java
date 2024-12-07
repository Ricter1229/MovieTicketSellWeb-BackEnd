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
public class RegionDto {
	private Integer regionNum;
	private String region;

	
}

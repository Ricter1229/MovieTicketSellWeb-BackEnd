package com.example.demo.dto.api;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.domain.StoreSubPhotoBean;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Component
@Data
public class StoreAdsPhotoDto {
	private Integer id;
	private String photo;
	private Integer sortId;
}

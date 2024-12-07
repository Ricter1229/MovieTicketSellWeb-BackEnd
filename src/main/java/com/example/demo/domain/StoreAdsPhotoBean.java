package com.example.demo.domain;

import java.sql.Blob;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "StoreAdsPhoto")
@Setter
@Getter
@NoArgsConstructor
public class StoreAdsPhotoBean {
	@Id
	@Column(name = "id",columnDefinition = "DECIMAL(3, 0)")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "storeId",referencedColumnName = "id")
	private StoreBean store;
	

	@Column(name = "photo")
	private byte[] photo;
	
}

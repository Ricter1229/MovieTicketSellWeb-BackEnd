package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name="StoreSubPhoto")
@ToString
public class StoreSubPhotoBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@JsonBackReference("StoreSubPhoto")
    @ManyToOne
	@JoinColumn(
			insertable = true, 
			updatable = true,
			name = "storeId",
			referencedColumnName = "id"
	)
	private StoreBean store;
	
	
	@Column(name="photo")
	private byte[] photo;
	
	@Column(name="mimeType")
	private String mimeType;
	
}

package com.example.demo.domain;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Store")
@Setter
@Getter
@NoArgsConstructor
//@ToString
public class StoreBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",columnDefinition = "DECIMAL(3, 0)")
	private Integer id;
	@Column(name = "name")
	private String name;
	@Column(name = "region",columnDefinition = "DECIMAL(1, 0)")
	private Integer region;
	@Column(name = "address")
	private String address;
	@Column(name = "phoneNo",columnDefinition = "nchar(20)")
	private String phoneNo;
	@Column(name = "mainPhoto")
	private byte[] mainPhoto;
	@Column(name = "introduce")
	private String introduce;
	@Column(name = "position")
	private String position;
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "store",fetch = FetchType.LAZY)
	private List<AuditoriumBean> auditorums;
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "store",fetch = FetchType.LAZY)
	private List<StoreSubPhotoBean> StoreSubPhoto;
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "store",fetch = FetchType.LAZY)
	private List<StoreAdsPhotoBean> StoreAdsPhoto;
	
	@JsonManagedReference("store")
	@OneToMany(mappedBy = "store",fetch = FetchType.LAZY)
	private List<MemberBuyTicketDetailBean> memberBuyTicketDetails = new ArrayList<>();
	@JsonManagedReference("store")
	@OneToMany(mappedBy = "store")
	private List<StoreReleaseMovieBean> storeReleaseMovieBeans = new ArrayList<>();
	
	
}
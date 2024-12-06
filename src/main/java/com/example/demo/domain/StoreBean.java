package com.example.demo.domain;

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
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name="Store")
public class StoreBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="name")
	private String name;

	@Column(name="region")
	private Integer region;
	
	@Column(name="address")
	private String address;
	
	@Column(name="phoneNo")
	private String phoneNo;
	
	@Column(name="mainPhoto")
	private byte[] mainPhoto;
	
	@Column(name="introduce")
	private String introduce;
	
	@Column(name = "position")
	private String position;
	
	@Column(name = "mimeType")
	private String mimeType;
	
	@JsonManagedReference("store")
	@OneToMany(mappedBy = "store")
	private List<StoreReleaseMovieBean> storeReleaseMovieBeans = new ArrayList<>();
	
	@JsonManagedReference("store")
	@OneToMany(mappedBy = "store")
	private List<AuditoriumBean> auditoriumBeans = new ArrayList<>();
	
	@JsonManagedReference("store")
	@OneToMany(mappedBy = "store")
	private List<MemberBuyTicketDetailBean> memberBuyTicketDetails = new ArrayList<>();
	
	@JsonManagedReference("store")
	@OneToMany(mappedBy = "store",cascade = CascadeType.ALL)
	private List<StoreSubPhotoBean> StoreSubPhoto=new ArrayList<>();


	
	
}

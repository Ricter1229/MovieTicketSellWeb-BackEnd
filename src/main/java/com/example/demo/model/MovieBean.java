package com.example.demo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="Movie")
public class MovieBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;

	@Column(name="chineseName")
	private String chineseName;
	
	@Column(name="englishName")
	private String englishName;
	
	@Column(name="cast")
	private String cast;

	@Column(name="releasedDate")
	private Date releasedDate;
	
	@Column(name="outOfDate")
	private Date outOfDate;
	
	@Column(name="intro")
	private String intro;
	
	@Column(name="price")
	private String price;
	
	@Column(name="ticketCount")
	private String ticketCount;
	
	@Column(name="style")
	private String style;
	
	@Column(name="rating")
	private Integer rating;
	
	@Column(name="runTime")
	private String runTime;
	
	@Column(name="commercialFilmURL")
	private String commercialFilmURL;
	
	@Column(name="photo")
	private String photo;

	@JsonManagedReference("movie")
	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
	private List<MovieVersionBean> MovieVersionBeans = new ArrayList<>();
	
	@JsonManagedReference("movieBean")
	@OneToMany(mappedBy = "movieBean")
	private List<MemberBuyTicketOrderBean> memberBuyTicketOrderBeans = new ArrayList<>();
}

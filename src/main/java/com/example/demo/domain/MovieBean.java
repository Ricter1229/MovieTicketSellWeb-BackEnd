package com.example.demo.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table(name = "Movie")
public class MovieBean {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="chineseName")
	private String chineseName;
	
	@Column(name="englishName")
	private String englishName;
	
	@Column(name="cast")
	private String cast;
	
	@JsonFormat(pattern="yyyy-MM-dd" , timezone="Asia/Taipei")
	@Column(name="releasedDate")
	private java.util.Date releasedDate;
	
	@Column(name="outOfDate")
	@JsonFormat(pattern="yyyy-MM-dd" , timezone="Asia/Taipei")
	private java.util.Date outOfDate;
	
	@Column(name="intro")
	private String intro;
	
	@Column(name="price")
	private Double price;
	
	@Column(name="ticketCount")
	private Integer ticketCount;
	
	@Column(name="style")
	private String style;
	
	@Column(name="rating")
	private String rating;
	
	@Column(name="runTime")
	private String runTime;
	
	@Column(name="commercialFilmURL")
	private String commercialFilmURL;
	
	@Column(name = "photo", columnDefinition = "varbinary(MAX)")
	private byte[] photo;
	
	@Column(name="mimeType")
	private String mimeType;
		
	@Override
	public String toString() {
		return "MovieBean [id=" + id + ", chineseName=" + chineseName + ", englishName=" + englishName + ", cast="
				+ cast + ", releasedDate=" + releasedDate + ", outOfDate=" + outOfDate + ", intro=" + intro + ", price="
				+ price + ", ticketCount=" + ticketCount + ", style=" + style + ", rating=" + rating + ", runTime="
				+ runTime + ", commercialFilmURL=" + commercialFilmURL + ", photo=" + Arrays.toString(photo)
				+ ", mimeType=" + mimeType + "]";
	}
	
	@JsonManagedReference("movie")
	@OneToMany(mappedBy = "movie")
	private List<MovieVersionBean> movieVersionBeans = new ArrayList<>();
}

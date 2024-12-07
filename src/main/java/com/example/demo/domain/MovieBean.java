package com.example.demo.domain;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
	
	
//	@Override
//	public String toString() {
//		return "MovieBean [id=" + id + ", chineseName=" + chineseName + ", englishName=" + englishName + ", cast="
//				+ cast + ", releasedDate=" + releasedDate + ", outOfDate=" + outOfDate + ", intro=" + intro + ", price="
//				+ price + ", ticketCount=" + ticketCount + ", style=" + style + ", rating=" + rating + ", runTime="
//				+ runTime + ", commercialFilmURL=" + commercialFilmURL + ", photo=" + photo + "]";
//	}

	public String getMimeType() {
		return mimeType;
	}


	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getCast() {
		return cast;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}

	public java.util.Date getReleasedDate() {
		return releasedDate;
	}

	public void setReleasedDate(java.util.Date releasedDate) {
		this.releasedDate = releasedDate;
	}

	public java.util.Date getOutOfDate() {
		return outOfDate;
	}

	public void setOutOfDate(java.util.Date outOfDate) {
		this.outOfDate = outOfDate;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getTicketCount() {
		return ticketCount;
	}

	public void setTicketCount(Integer ticketCount) {
		this.ticketCount = ticketCount;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getRunTime() {
		return runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

	public String getCommercialFilmURL() {
		return commercialFilmURL;
	}

	public void setCommercialFilmURL(String commercialFilmURL) {
		this.commercialFilmURL = commercialFilmURL;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	
	
	
	
	

}

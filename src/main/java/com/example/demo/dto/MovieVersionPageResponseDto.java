package com.example.demo.dto;

import lombok.Data;

@Data
public class MovieVersionPageResponseDto {
	private Integer movieId;
	private String chineseName;
	private String mimeType;
	private Integer movieVersionId;
	private byte[] photo;
	public MovieVersionPageResponseDto(Integer movieId, String chineseName, String mimeType, byte[] photo, Integer movieVersionId) {
		super();
		this.movieId = movieId;
		this.chineseName = chineseName;
		this.mimeType = mimeType;
		this.photo = photo;
		this.movieVersionId = movieVersionId;
	}
}

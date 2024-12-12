package com.example.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class AuditoriumScheduleRequestDto {
	private Integer storeId;
	private List<MovieDto> movies;
	private Integer auditoriumId;
	
	@Data
	public static class MovieDto {
		private Integer auditoriumId;
		private String date;
		private Integer storeReleaseMovieId;
		private String timeSlots;
		private Boolean isRemove;
	}
}

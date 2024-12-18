package com.example.demo.dto.api;

import java.util.List;

import lombok.Data;

@Data
public class AuditoriumsDto {
	private List<AuditoriumDto> auditoriumList;
	
	@Data
	public static class AuditoriumDto {
		private String auditoriumName;
		private String auditoriumId;
	}
}

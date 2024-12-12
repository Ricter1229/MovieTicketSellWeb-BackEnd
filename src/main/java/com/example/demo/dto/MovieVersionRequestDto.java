package com.example.demo.dto;

import lombok.Data;

@Data
public class MovieVersionRequestDto {
	private Integer versionId;
	private Integer page;
	private Integer size;
}

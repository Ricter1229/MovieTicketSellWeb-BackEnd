package com.example.demo.dto.api;

import lombok.Data;

@Data
public class PhotoTypeDto {
	private String mimeType;
	private byte[] photo;
}

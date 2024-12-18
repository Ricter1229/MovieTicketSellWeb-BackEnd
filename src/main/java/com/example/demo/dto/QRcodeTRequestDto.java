package com.example.demo.dto;

import lombok.Data;

@Data
public class QRcodeTRequestDto {
	private String encryptedText;
	private String email;
	private String data;
	private Integer orderId;
	
}

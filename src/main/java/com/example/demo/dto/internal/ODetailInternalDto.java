package com.example.demo.dto.internal;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class ODetailInternalDto {
	private byte[] qrcode;
	private Integer totalAmount;
	private Integer storeId;
    private String name;

    public ODetailInternalDto(byte[] qrcode, Integer totalAmount, Integer storeId, String name) {
        this.qrcode = qrcode;
        this.totalAmount = totalAmount;
    	this.storeId = storeId;
        this.name = name;
    }
    public ODetailInternalDto() {}
}

package com.example.demo.dto.internal;

import lombok.Data;

@Data
public class StoreInternalDto {
	private Integer storeId;
    private String name;

    public StoreInternalDto(Integer storeId, String name) {
        this.storeId = storeId;
        this.name = name;
    }
    public StoreInternalDto() {}
}

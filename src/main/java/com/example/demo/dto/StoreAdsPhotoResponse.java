package com.example.demo.dto;

import java.util.List;

import com.example.demo.dto.api.StoreAdsPhotoDto;

public record StoreAdsPhotoResponse (long count ,List<StoreAdsPhotoDto> list ,boolean success , String message) {

}

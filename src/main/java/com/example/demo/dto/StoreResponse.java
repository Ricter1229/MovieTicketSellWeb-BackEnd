package com.example.demo.dto;

import java.util.List;

import com.example.demo.domain.StoreBean;
import com.example.demo.dto.api.StoreFindDto;



public record StoreResponse (long count ,List<StoreFindDto> list ,boolean success , String message) {

}

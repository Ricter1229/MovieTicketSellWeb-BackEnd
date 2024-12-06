package com.example.demo.dto;

import java.util.List;


import com.example.demo.dto.api.StoreOuterDto;



public record StoreOuterResponse (long count ,List<StoreOuterDto> list ,boolean success , String message) {

}

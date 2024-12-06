package com.example.demo.dto;



import com.example.demo.dto.api.StoreInnerDto;




public record StoreInnerResponse (long count ,StoreInnerDto dto ,boolean success , String message) {

}

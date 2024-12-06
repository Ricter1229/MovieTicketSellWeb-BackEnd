package com.example.demo.dto;

import java.util.List;


import com.example.demo.dto.api.RegionDto;




public record RegionResponse (long count ,List<RegionDto> list ,boolean success , String message) {

}

package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.api.common.ApiResponse;
import com.example.demo.exception.CustomException;


@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/success")
    public ApiResponse<String> testSuccess() {
        return ApiResponse.success("This is a successful response.");
    }

    @GetMapping("/custom-error")
    public ApiResponse<Object> testCustomError() {
        throw new CustomException("Custom exception occurred.", 201);
    }

    @GetMapping("/general-error")
    public ApiResponse<Object> testGeneralError() {    	
		int[] data = {1, 2, 3};
		System.out.println(data[2]);
		System.out.println(data[3]);
        return ApiResponse.success("This is a successful response.");
    }
    
    

    
    
    
	
}
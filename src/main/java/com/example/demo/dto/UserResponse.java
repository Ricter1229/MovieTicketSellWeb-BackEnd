package com.example.demo.dto;

import java.util.List;

import com.example.demo.domain.MemberBean;

public record UserResponse(long count, List<MemberBean> list) {
    
}

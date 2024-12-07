package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.MemberBean;
import com.example.demo.dto.UserResponse;
import com.example.demo.service.MemberService;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private MemberService memberService;
    
    @PostMapping("/find")
    public UserResponse find(@RequestBody String entity) {
        long count = memberService.count(entity);
        List<MemberBean> users = memberService.find(entity);

        return new UserResponse(count, users);
    }
}

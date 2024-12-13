package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.MemberBean;
import com.example.demo.dto.UserResponse;
import com.example.demo.service.MemberService;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private MemberService memberService;
    
    @PutMapping("/{id}")
    public UserResponse modify(@PathVariable Integer id, @RequestBody String entity) {
        MemberBean update = memberService.modify(entity);

        if (update == null) {
            return new UserResponse(0, null);
        } else {
            return new UserResponse(1, List.of(update));
        }
    }
}

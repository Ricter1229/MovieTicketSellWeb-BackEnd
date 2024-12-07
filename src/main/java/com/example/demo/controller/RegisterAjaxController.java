package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.MemberBean;
import com.example.demo.service.MemberService;

@RestController
@RequestMapping("/ajax/secure")
@CrossOrigin
public class RegisterAjaxController {
    
    @Autowired
    private MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody MemberBean newMember) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 呼叫 service
            String resultMessage = memberService.register(newMember);

            if ("Registration successful!".equals(resultMessage)) {
                response.put("success", true);
                response.put("message", resultMessage);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", resultMessage);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

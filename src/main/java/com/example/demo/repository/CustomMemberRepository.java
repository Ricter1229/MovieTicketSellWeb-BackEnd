package com.example.demo.repository;

import java.util.List;
import org.json.JSONObject;

import com.example.demo.domain.MemberBean;

public interface CustomMemberRepository {
    List<MemberBean> find(JSONObject obj);
    
    long count(JSONObject obj);
}
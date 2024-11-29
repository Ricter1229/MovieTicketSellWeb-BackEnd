package com.example.demo.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.MemberBean;

public interface MemberRepository extends JpaRepository<MemberBean, Integer> {

}

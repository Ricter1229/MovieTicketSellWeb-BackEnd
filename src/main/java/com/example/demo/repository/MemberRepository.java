package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.MemberBean;


public interface MemberRepository extends JpaRepository<MemberBean, Integer>{
    boolean existsByAccountOrEmail(String account, String email);

    Optional<MemberBean> findByEmail(String email);

    Optional<MemberBean> findByAccount(String account);
}

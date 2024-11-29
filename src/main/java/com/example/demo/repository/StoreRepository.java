package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.StoreBean;

public interface StoreRepository extends JpaRepository<StoreBean, Integer> {

}

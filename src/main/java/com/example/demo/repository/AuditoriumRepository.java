package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.AuditoriumBean;

public interface AuditoriumRepository extends JpaRepository<AuditoriumBean, Integer> {

}

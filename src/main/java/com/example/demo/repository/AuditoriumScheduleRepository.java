package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.AuditoriumScheduleBean;

public interface AuditoriumScheduleRepository extends JpaRepository<AuditoriumScheduleBean, Integer> {

}

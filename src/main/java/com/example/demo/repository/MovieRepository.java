package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.MovieBean;

public interface MovieRepository extends JpaRepository<MovieBean, Integer> {

}

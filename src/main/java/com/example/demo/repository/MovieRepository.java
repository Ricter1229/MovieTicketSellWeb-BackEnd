package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.MovieBean;

public interface MovieRepository extends JpaRepository<MovieBean, Integer> ,MovieDAO {

	Optional<MovieBean> findByChineseName(String chineseName);

}

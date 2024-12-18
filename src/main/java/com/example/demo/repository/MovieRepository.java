package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.MovieBean;

public interface MovieRepository extends JpaRepository<MovieBean, Integer> ,MovieDAO {

	Optional<MovieBean> findByChineseName(String chineseName);
	// 吳其容修改
	List<MovieBean> findByDateRange();
	List<MovieBean> findByGreaterReleasedDate();

}

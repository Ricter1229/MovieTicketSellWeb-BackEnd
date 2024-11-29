package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.StoreReleaseMovieBean;

public interface StoreReleaseMovieRepository extends JpaRepository<StoreReleaseMovieBean, Integer> {

}

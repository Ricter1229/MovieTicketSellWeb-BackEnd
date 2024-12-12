package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.domain.VersionBean;

public interface VersionRepository extends JpaRepository<VersionBean, Integer> {

	@Query("SELECT v FROM VersionBean v WHERE EXISTS (" +
		       "SELECT mv FROM MovieVersionBean mv WHERE mv.versionId = v.id)")
	List<VersionBean> getAllWithMovies();
}

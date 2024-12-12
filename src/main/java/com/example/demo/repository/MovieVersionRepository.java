package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.MovieBean;
import com.example.demo.domain.MovieVersionBean;

public interface MovieVersionRepository extends JpaRepository<MovieVersionBean, Integer> {

	List<MovieVersionBean> findByMovieId(Integer movieId);

	List<MovieVersionBean> findByVersionId(Integer id);

	@Query("SELECT mv FROM MovieVersionBean mv WHERE mv.versionId = :versionId")
	Page<MovieVersionBean> findMovieVersionsByVersionId(@Param("versionId") Integer versionId, Pageable pageable);

	Optional<MovieVersionBean> findByMovieIdAndVersionId(Integer movieId, Integer versionId);
}

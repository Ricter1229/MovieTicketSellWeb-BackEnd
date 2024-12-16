package com.example.demo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.StoreBean;
import com.example.demo.dto.internal.StoreInternalDto;

@Repository
public interface StoreRepository extends JpaRepository<StoreBean, Integer>,StoreDAO {
	@Query(value="from StoreBean where region=:region")
	public List<StoreBean> findStoreByRegion(@Param(value="region")Integer region);
	@Query("SELECT DISTINCT s.region FROM StoreBean s")
    List<Integer> findDistinctRegions();
	@Query(value="from StoreBean where name=:name")
	public List<StoreBean> findStoreByName(@Param(value="name")String name);
	
	@Query("SELECT DISTINCT new com.example.demo.dto.internal.StoreInternalDto(s.id, s.name) " +
		       "FROM StoreBean s " +
		       "JOIN AuditoriumBean a on a.storeId = s.id " +
		       "JOIN AuditoriumScheduleBean aa on aa.auditoriumId = a.id " +
		       "JOIN StoreReleaseMovieBean srm on srm.id = aa.storeReleaseMovieId " +
		       "JOIN MovieVersionBean mv on mv.id = srm.movieVersionId " +
		       "WHERE mv.movieId = :movieId " +
		       "AND aa.date BETWEEN :startDate AND :endDate")
	List<StoreInternalDto> findDistinctStoresByMovieIdAndDateRange(
	    @Param("movieId") Integer movieId,
	    @Param("startDate") Date startDate,
	    @Param("endDate") Date endDate
	); 
}

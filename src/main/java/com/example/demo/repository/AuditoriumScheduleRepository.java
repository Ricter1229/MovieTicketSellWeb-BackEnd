package com.example.demo.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.AuditoriumScheduleBean;
import com.example.demo.dto.internal.ScheduleInternalDto;

public interface AuditoriumScheduleRepository extends JpaRepository<AuditoriumScheduleBean, Integer> {
	@Query("FROM AuditoriumScheduleBean WHERE auditoriumId = :auditoriumId AND date = :date ")
	List<AuditoriumScheduleBean> findByAuditoriumIdAndDate(@Param("auditoriumId") Integer auditoriumId,
			@Param("date") Date date);

	@Query("FROM AuditoriumScheduleBean WHERE auditoriumId = :auditoriumId AND (date >= :startDate AND date <= :endDate)")
	List<AuditoriumScheduleBean> findByAuditoriumIdAndWeek(@Param("auditoriumId") Integer auditoriumId,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	@Query("FROM AuditoriumScheduleBean WHERE auditoriumId = :auditoriumId AND date = :date AND timeSlots = :timeSlots")
	Optional<AuditoriumScheduleBean> findByALLFactor(@Param("auditoriumId") Integer auditoriumId,
			@Param("date") Date date, @Param("timeSlots") String timeSlots);

	@Query( "SELECT DISTINCT new com.example.demo.dto.internal.ScheduleInternalDto(" 
			+ "srm.id as storeReleaseMovieId, aa.auditoriumId, a.name as auditoriumName, v.id as versionId, v.version, a.id, aa.date, aa.timeSlots" 
			+ ") " 
			+ "FROM StoreBean s "
			+ "JOIN AuditoriumBean a on a.storeId = s.id "
			+ "JOIN AuditoriumScheduleBean aa on aa.auditoriumId = a.id " 
			+ "JOIN StoreReleaseMovieBean srm on srm.id = aa.storeReleaseMovieId " 
			+ "JOIN MovieVersionBean mv on mv.id = srm.movieVersionId " 
			+ "JOIN VersionBean v ON mv.versionId = v.id " 
			+ "WHERE s.id=:storeId AND mv.movieId = :movieId " 
			+ "AND aa.date BETWEEN :startDate AND :endDate " 
			+ "ORDER BY aa.date, aa.timeSlots")
	List<ScheduleInternalDto> findSchedulesByStoreIdAndDateRange(@Param("storeId") Integer storeId, @Param("movieId") Integer movieId,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}

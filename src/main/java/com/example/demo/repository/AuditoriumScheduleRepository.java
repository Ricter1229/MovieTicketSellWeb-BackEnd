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

	@Query( "SELECT DISTINCT new com.example.demo.dto.internal.ScheduleInternalDto(v.version, a.date, a.timeSlots) " +
			"FROM AuditoriumScheduleBean a " +
	        "JOIN StoreReleaseMovieBean srm ON a.storeReleaseMovieId = srm.id " +
	        "JOIN MovieVersionBean mv ON srm.movieVersionId = mv.id " +
	        "JOIN VersionBean v ON mv.versionId = v.id " +
	        "WHERE srm.storeId = :storeId " +
	        "AND a.date BETWEEN :startDate AND :endDate " +
	        "ORDER BY a.date, a.timeSlots")
	List<ScheduleInternalDto> findSchedulesByStoreIdAndDateRange(@Param("storeId") Integer storeId,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}

package com.example.demo.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.AuditoriumScheduleBean;
import com.example.demo.domain.StoreReleaseMovieBean;

public interface AuditoriumScheduleRepository extends JpaRepository<AuditoriumScheduleBean, Integer> {
	@Query("FROM AuditoriumScheduleBean WHERE auditoriumId = :auditoriumId AND date = :date ")
	List<AuditoriumScheduleBean> findByAuditoriumIdAndDate(@Param("auditoriumId")Integer auditoriumId, @Param("date") Date date);

	@Query("FROM AuditoriumScheduleBean WHERE auditoriumId = :auditoriumId AND (date >= :startDate AND date <= :endDate)")
	List<AuditoriumScheduleBean> findByAuditoriumIdAndWeek(@Param("auditoriumId") Integer auditoriumId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	@Query("FROM AuditoriumScheduleBean WHERE auditoriumId = :auditoriumId AND date = :date AND timeSlots = :timeSlots")
	Optional<AuditoriumScheduleBean> findByALLFactor(@Param("auditoriumId")Integer auditoriumId, @Param("date") Date date, @Param("timeSlots") String timeSlots);


}

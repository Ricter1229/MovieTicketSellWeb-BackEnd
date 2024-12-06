package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.SeatingListBean;

import jakarta.persistence.LockModeType;

public interface SeatingListRepository extends JpaRepository<SeatingListBean, Integer> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("FROM SeatingListBean WHERE auditoriumScheduleId = :scheduleId AND seat = :seat")
	Optional<SeatingListBean> findSeatForUpdate(@Param("scheduleId") Integer scheduleId, @Param("seat") String seat);

	@Query("FROM SeatingListBean WHERE auditoriumScheduleId = :scheduleId AND isSold = :isSold")
	List<SeatingListBean> findByAuditoriumScheduleIdSoldSeat(@Param("scheduleId") Integer scheduleId, @Param("isSold") Integer isSold);
	
	@Query("FROM SeatingListBean WHERE auditoriumScheduleId = :scheduleId AND seat = :seat")
	Boolean isSeatLock(@Param("scheduleId") Integer scheduleId, @Param("seat") String seat);

	Optional<SeatingListBean> findByAuditoriumScheduleIdAndSeat(Integer scheduleId, String seat);

}

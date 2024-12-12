package com.example.demo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.AuditoriumScheduleBean;
import com.example.demo.dto.AuditoriumScheduleRequestDto.MovieDto;
import com.example.demo.repository.AuditoriumRepository;
import com.example.demo.repository.AuditoriumScheduleRepository;
import com.example.demo.repository.StoreReleaseMovieRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuditoriumScheduleService {
	@Autowired
	private AuditoriumScheduleRepository auditoriumScheduleRepository;
	@Autowired
	private AuditoriumRepository auditoriumRepository;
	@Autowired
	private StoreReleaseMovieRepository storeReleaseMovieRepository;

	public List<AuditoriumScheduleBean> insert(Integer storeId, List<MovieDto> request) {		
		try {
			for(MovieDto insert : request) {
				Optional<AuditoriumScheduleBean> oldSchedule = auditoriumScheduleRepository.findByALLFactor(
						insert.getAuditoriumId(),
						new SimpleDateFormat("yyyy-MM-dd").parse(insert.getDate()),
						insert.getTimeSlots()
				);
				
				if(oldSchedule.isEmpty()) {
					AuditoriumScheduleBean schedule = new AuditoriumScheduleBean();
					schedule.setAuditoriumId(insert.getAuditoriumId());				
					schedule.setAuditoriumBean(auditoriumRepository.findById(insert.getAuditoriumId()).get());
					schedule.setStoreReleaseMovieId(insert.getStoreReleaseMovieId());
					schedule.setStoreReleaseMovieBean(storeReleaseMovieRepository.findById(insert.getStoreReleaseMovieId()).get());
					schedule.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(insert.getDate()));
					schedule.setTimeSlots(insert.getTimeSlots());
					auditoriumScheduleRepository.save(schedule);
				} else if(insert.getIsRemove() == true) {
					AuditoriumScheduleBean delete = oldSchedule.get();
					delete.setAuditoriumBean(null);
					delete.setAuditoriumId(null);
					delete.setStoreReleaseMovieBean(null);
					delete.setStoreReleaseMovieId(null);
				    auditoriumScheduleRepository.delete(delete);
				    auditoriumScheduleRepository.flush();
				} else {
					AuditoriumScheduleBean update = oldSchedule.get();
					update.setStoreReleaseMovieId(insert.getStoreReleaseMovieId());
					update.setStoreReleaseMovieBean(storeReleaseMovieRepository.findById(insert.getStoreReleaseMovieId()).get());
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<AuditoriumScheduleBean> findByAuditoriumId(Integer auditoriumId, List<MovieDto> request) {
		try {
			if(request.size() == 1) {
				return auditoriumScheduleRepository.findByAuditoriumIdAndDate(auditoriumId, new SimpleDateFormat("yyyy-MM-dd").parse(request.get(0).getDate()));
			} else {
				String startDate = request.get(0).getDate();
				String endDate = request.get(request.size() - 1).getDate();
				return auditoriumScheduleRepository.findByAuditoriumIdAndWeek(
						auditoriumId, 
						new SimpleDateFormat("yyyy-MM-dd").parse(startDate), 
						new SimpleDateFormat("yyyy-MM-dd").parse(endDate));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

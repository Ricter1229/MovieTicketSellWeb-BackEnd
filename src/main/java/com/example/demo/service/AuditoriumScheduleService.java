package com.example.demo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.AuditoriumScheduleBean;
import com.example.demo.domain.SeatingListBean;
import com.example.demo.dto.AuditoriumScheduleRequestDto.MovieDto;
import com.example.demo.dto.internal.ScheduleInternalDto;
import com.example.demo.repository.AuditoriumRepository;
import com.example.demo.repository.AuditoriumScheduleRepository;
import com.example.demo.repository.SeatingListRepository;
import com.example.demo.repository.StoreReleaseMovieRepository;
import com.example.demo.service.booking.SeatingListService;

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
	@Autowired
	private SeatingListService seatingListService;
	@Autowired
	private SeatingListRepository seatingListRepository;

	public List<AuditoriumScheduleBean> insert(Integer storeId, List<MovieDto> request) {		
		try {
			for(MovieDto insert : request) {
				Optional<AuditoriumScheduleBean> oldSchedule = auditoriumScheduleRepository.findByALLFactor(
						insert.getAuditoriumId(),
						new SimpleDateFormat("yyyy-MM-dd").parse(insert.getDate()),
						insert.getTimeSlots()
				);
				
				if(oldSchedule.isEmpty()) {
					System.out.println("1");
					AuditoriumScheduleBean schedule = new AuditoriumScheduleBean();
					schedule.setAuditoriumId(insert.getAuditoriumId());				
					schedule.setAuditoriumBean(auditoriumRepository.findById(insert.getAuditoriumId()).get());
					schedule.setStoreReleaseMovieId(insert.getStoreReleaseMovieId());
					schedule.setStoreReleaseMovieBean(storeReleaseMovieRepository.findById(insert.getStoreReleaseMovieId()).get());
					schedule.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(insert.getDate()));
					schedule.setTimeSlots(insert.getTimeSlots());
					schedule = auditoriumScheduleRepository.save(schedule);

					List<SeatingListBean> seatingList = seatingListService.insertSeat(insert.getAuditoriumId(), schedule.getId());
					schedule.setSeatingListBeans(seatingList);

				} else if(insert.getIsRemove() == true) {
					System.out.println("2");

					AuditoriumScheduleBean delete = oldSchedule.get();
			
					List<SeatingListBean> seatingList = delete.getSeatingListBeans();
					seatingListRepository.deleteAll(seatingList);
					seatingListRepository.flush();

				    auditoriumScheduleRepository.delete(delete);
				    auditoriumScheduleRepository.flush();
				} else {
					System.out.println("3");

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
	
	public List<ScheduleInternalDto> getSchedulesByStoreIdAndDateRange(Integer storeId, Integer movieId) {
        if (storeId == null) {
            throw new IllegalArgumentException("Store ID cannot be null");
        }
        // 计算日期范围
        Date today = new Date();
        
        // 计算一周后的日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date startOfToday = calendar.getTime();

        // 设置为一周后
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date endOfNextWeek = calendar.getTime();

        return auditoriumScheduleRepository.findSchedulesByStoreIdAndDateRange(storeId, movieId, startOfToday, endOfNextWeek);
    }
}

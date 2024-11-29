package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.api.common.ApiResponse;
import com.example.demo.domain.MovieBean;
import com.example.demo.repository.AuditoriumRepository;
import com.example.demo.repository.AuditoriumScheduleRepository;
import com.example.demo.repository.MemberBuyTicketDetailRepository;
import com.example.demo.repository.MemberBuyTicketOrderRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.MovieVersionRepository;
import com.example.demo.repository.StoreReleaseMovieRepository;
import com.example.demo.repository.StoreRepository;

@RestController
@CrossOrigin
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private MemberBuyTicketOrderRepository memberBuyTicketOrderRepository;
	@Autowired
	private MovieRepository movieRepo;
	@Autowired
	private MovieVersionRepository movieVersionRepo;
	@Autowired
	private StoreRepository storeRepo;
	@Autowired
	private StoreReleaseMovieRepository StoreReleaseMovieRepo;
	@Autowired
	private AuditoriumRepository auditoriumRepo;
	
	@Autowired
	private AuditoriumScheduleRepository auditoriumScheduleRepo;
	
	@Autowired
	private MemberBuyTicketDetailRepository memberBuyTicketDetailRepo;

	@GetMapping("/find")
	public ApiResponse<?> findUser() {        
//        Optional<MemberBean> op = memberRepository.findById(new BigDecimal(1));
//        MemberBean member = op.get();
//
//        Optional<MemberBuyTicketOrderBean> op = memberBuyTicketOrderRepository.findById(1);
		
		Optional<MovieBean> op = movieRepo.findById(1);
//		Optional<MovieVersionBean> op = movieVersionRepo.findById(1);
//		Optional<StoreBean> op = storeRepo.findById(1);
//		Optional<StoreReleaseMovieBean> op = StoreReleaseMovieRepo.findById(1);
//		Optional<AuditoriumBean> op = auditoriumRepo.findById(1);
//		Optional<AuditoriumScheduleBean> op = auditoriumScheduleRepo.findById(3);
//		Optional<MemberBuyTicketDetailBean> op = memberBuyTicketDetailRepo.findById(1);

		return ApiResponse.success(op.get());
	}
	
}

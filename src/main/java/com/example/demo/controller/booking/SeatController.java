package com.example.demo.controller.booking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.api.common.ApiResponse;
import com.example.demo.domain.SeatingListBean;
import com.example.demo.dto.api.SeatRequestDto;
import com.example.demo.exception.CustomException;
import com.example.demo.service.booking.SeatingListService;

@RestController
@CrossOrigin
@RequestMapping("/api/seats")
public class SeatController {
	@Autowired
	private SeatingListService seatingListService;
	
	/**
	 * 對座位狀態設定，有鎖定(LOCK)、釋放(RELEASE)、購買(PURCHASE)
	 * @param SeatRequestDto 
	 * @return
	 */
	@PostMapping("/action")
	public ApiResponse<Object> handleSeatAction(@RequestBody SeatRequestDto request) {
	    switch (request.getAction().trim()) {
	        case "LOCK":
	        	seatingListService.lockSeat(request.getAuditoriumScheduleId(), request.getSeat());
	            return ApiResponse.success("Seat locked successfully");
	        case "RELEASE":
	        	seatingListService.releaseSeat(request.getAuditoriumScheduleId(), request.getSeat());
	            return ApiResponse.success("Seat released successfully");
	        case "PURCHASE":
	        	seatingListService.purchaseSeat(request.getAuditoriumScheduleId(), request.getSeat(), request.getOrderId());
	            return ApiResponse.success("Seat purchased successfully");
	        default:
	            throw new CustomException("Invalid action", 400);
	    }
	}
	
    
    /**
     * 新增100個座位
     * @param
     * @return
     */
    @PostMapping("/create")
    public ApiResponse<Object> createSeat() {
		seatingListService.insertSeat();
        return ApiResponse.success("Seat released successfully");
    }
    
    /**
     * 查詢某場次的所有已售出座位
     * @param
     * @return
     */
    @GetMapping("/sold")
    public ApiResponse<Object> soldSeat(@RequestBody SeatRequestDto request) {
    	List<SeatingListBean> result = seatingListService.getAllSoldSeatWithScheduleId(request.getAuditoriumScheduleId());
        return ApiResponse.success(result);
    }

}

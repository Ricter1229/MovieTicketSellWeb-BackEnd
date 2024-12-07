package com.example.demo.controller.booking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.api.common.ApiResponse;
import com.example.demo.domain.AuditoriumScheduleBean;
import com.example.demo.domain.MemberBuyTicketOrderBean;
import com.example.demo.dto.api.MemberBuyTicketOrderRequestDto;
import com.example.demo.service.booking.MemberBuyTicketOrderService;

@RestController
@CrossOrigin
@RequestMapping("/api/orders")
public class MemberBuyTicketOrderController {
	@Autowired
	private MemberBuyTicketOrderService orderService;

	
	@PostMapping("/")
	public ApiResponse<Object> createOrder(@RequestBody MemberBuyTicketOrderRequestDto request) {

		MemberBuyTicketOrderBean order = orderService.insert(
				request.getMemberId(), 
				request.getMovieId(), 
				request.getTotalAmount(),
				request.getOrderDetail()
		);
		return ApiResponse.success(order.getId());
	}
	
	/**
	 * 更改訂單狀態
	 * @param id
	 * PENDING PAID CANCELED REFUNDED
	 * @return
	 */
	@PutMapping("/status")
	public ApiResponse<Object> updateOrderStatus(@RequestBody MemberBuyTicketOrderRequestDto request) {
		Integer orderId = orderService.updateOrderStatus(request.getOrderId(), request.getStatus());
    	System.out.println("我要return "+ orderId);
        ApiResponse<Object> response = ApiResponse.success(orderId);
        System.out.println("Response: " + response.toString());

		return response;
	}	
	
	@DeleteMapping("/")
	public ApiResponse<String> removeOrder(@RequestBody MemberBuyTicketOrderRequestDto request) {
		orderService.deleteById(request.getOrderId());
		return ApiResponse.success("Remove Order successful.");
	}
	
	@GetMapping("/")
	public ApiResponse<Object> getOrderById(@RequestBody MemberBuyTicketOrderRequestDto request) {
		MemberBuyTicketOrderBean order = orderService.findById(request.getOrderId());
		return ApiResponse.success(order);
	}

	/**
	 * 查詢某個會員的訂單 依據傳入的動作確定是過去還是未來
	 * @param request
	 * @return
	 */
	@PostMapping("/member/tickets")
	public ApiResponse<Object> getNowOrderByMemberId(@RequestBody MemberBuyTicketOrderRequestDto request) {
		List<MemberBuyTicketOrderBean> orders = orderService.findByMemberId(request.getMemberId());
		
		List<Map<String, Object>> result = new ArrayList<>();

 		for(MemberBuyTicketOrderBean order : orders) {
 			AuditoriumScheduleBean auditoriumSchedule = order.getMemberBuyTicketDetailBeans().get(0).getAuditoriumScheduleBean();
 			if(findDateIsPastOrFuture(auditoriumSchedule).equals(request.getAction())) {
 				Map<String, Object> orderInfo = new LinkedHashMap<>();
 				orderInfo.put("orderId", order.getId());
 				orderInfo.put("orderDetailCount", order.getMemberBuyTicketDetailBeans().size());
 			    orderInfo.put("movieName", order.getMovieBean().getChineseName());
 			    orderInfo.put("schedule", assembleScheduleDateTimeSlots(auditoriumSchedule));
 				
 			    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
 		        String formattedDate = order.getTimeBuying().format(formatter); // 格式化日期
 			    orderInfo.put("orderCreateTime", formattedDate);
 			    result.add(orderInfo);
 			}
		}
 		
		return ApiResponse.success(result);
	}
	
	/**
	 * 組裝Schedule日期+時段
	 */
	private Date assembleScheduleDateTimeSlots(AuditoriumScheduleBean auditoriumSchedule) {
		// 示例数据
        Date auditoriumDate = auditoriumSchedule.getDate(); // 替换为 auditoriumSchedule.getDate()
        String timeSlots = auditoriumSchedule.getTimeSlots(); // 替换为 auditoriumSchedule.getTimeSlots()

        // 格式化日期为 yyyy-MM-dd
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(auditoriumDate); // 格式化日期

        // 拼接日期和时间
        String formattedDateTime = formattedDate + " " + timeSlots + ":00"; // 得到 yyyy-MM-dd HH:mm:ss 格式的字符串

        // 解析为 Date 对象
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
			return dateFormat.parse(formattedDateTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
	}
	/**
	 * 判斷日期跟當下相比是 future 或是 past
	 * @param auditoriumSchedule
	 * @return
	 */
	private String findDateIsPastOrFuture(AuditoriumScheduleBean auditoriumSchedule) {
		// 格式化日期
			try {
	            
				Date scheduleDateTime =assembleScheduleDateTimeSlots(auditoriumSchedule);
	            // 当前时间
	            Date now = new Date();

	            // 打印解析结果
	            System.out.println("Schedule DateTime: " + scheduleDateTime);

	            // 比较日期
	            if (scheduleDateTime.after(now)) {
	                return "future";
	            } else if (scheduleDateTime.before(now)) {
	            	return "past";
	            } else {
	            	return "now";
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.err.println("Error parsing schedule date.");
	        }
		return null;
	}
	
}

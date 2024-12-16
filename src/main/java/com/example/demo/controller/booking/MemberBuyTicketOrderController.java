package com.example.demo.controller.booking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.api.common.ApiResponse;
import com.example.demo.domain.AuditoriumScheduleBean;
import com.example.demo.domain.MemberBuyTicketOrderBean;
import com.example.demo.dto.api.MemberBuyTicketOrderRequestDto;
import com.example.demo.dto.api.RefundRequestDto;
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
		//原本updateOrderStatus
//		orderService.updateOrderStatus(request.getOrderId(), request.getStatus());
		
		Map<String, Object> returnData = new LinkedHashMap<>();
		returnData.put("orderId",order.getId());
		return ApiResponse.success(returnData);
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
		return ApiResponse.success(orderId);
	}
	
	@PostMapping("/netSection") 
	public ApiResponse<Object> netSection(@RequestBody MemberBuyTicketOrderRequestDto request) {
		String ecPayWeb = orderService.ecPayService(request,request.getOrderId());
		
		Map<String, Object> returnData = new LinkedHashMap<>();
		returnData.put("netSection", ecPayWeb);
		return ApiResponse.success(returnData);
	}
	/**
	 * 更改訂單狀態
	 * @param id
	 * PENDING PAID CANCELED REFUNDED
	 * @return
	 */
//	@PutMapping("/status")
//	public ApiResponse<Object> updateOrderStatus(@RequestBody MemberBuyTicketOrderRequestDto request) {
//		Integer orderId = orderService.updateOrderStatus(request.getOrderId(), request.getStatus());
//		String aioCheckOutALL = orderService.getAioCheckOutALL(request);
//
//		System.out.println("orderId");
//		System.out.println(orderId);
//		aioCheckOutALL = aioCheckOutALL.replaceAll("<script[^>]*>.*?</script>", "");
//		
//		Map<String, Object> returnData = new LinkedHashMap<>();
//		returnData.put("orderId",orderId);
//		returnData.put("netSection", aioCheckOutALL);
//		return ApiResponse.success(returnData);
//	}	
	
	
	
	//退款
	@PostMapping("/refund")
    public ApiResponse processRefund(@RequestBody RefundRequestDto refundRequest) {
        try {
            String result = orderService.refund(refundRequest);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.fail(400,"","錯誤處理");
        }
    }

	
	/**
	 * 綠界刷卡後返回網址
	 */
	 @PostMapping("/receive")
	    public String handleEcpayNotification(@RequestParam Map<String,String> notifyParams) {
		 System.out.println("後端posthand");

		 Hashtable<String, String> hashtable = new Hashtable<>(notifyParams);
	        try {
	            boolean isvalid = orderService.handleEcpayNotification(hashtable);

	            if (isvalid) {
	                // 處理成功後，綠界要求回傳固定字串 "1|OK"
	            	System.out.println("1|OK");
	                return "1|OK";
	            } else {
	                // 檢查碼驗證失敗或其他錯誤
	            	System.out.println("檢查碼驗證失敗或其他錯誤");
	                return "0|FAIL";
	            }
	        } catch (Exception e) {
	            // 捕獲異常並返回失敗
	            System.err.println("綠界通知處理失敗：" + e.getMessage());
	            return "0|FAIL";
	        }
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
	
	@GetMapping("/{id}")
	public ApiResponse<Object> getOrderById(@PathVariable Integer id) {
		Map<String, Object> returnData = orderService.setOrderAndMovieNameById(id);
		return ApiResponse.success(returnData);
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
 			if (!order.getMemberBuyTicketDetailBeans().isEmpty()) {
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

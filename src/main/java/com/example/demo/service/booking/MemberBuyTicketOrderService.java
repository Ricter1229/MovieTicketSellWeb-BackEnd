package com.example.demo.service.booking;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.api.common.ApiResponse;
import com.example.demo.domain.MemberBean;
import com.example.demo.domain.MemberBuyTicketDetailBean;
import com.example.demo.domain.MemberBuyTicketOrderBean;
import com.example.demo.domain.MovieBean;
import com.example.demo.dto.api.MemberBuyTicketDetailRequestDto;
import com.example.demo.dto.api.MemberBuyTicketOrderRequestDto;
import com.example.demo.dto.api.RefundRequestDto;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.MemberBuyTicketOrderRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.MovieRepository;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import ecpay.payment.integration.domain.AioCheckOutOneTime;
import ecpay.payment.integration.domain.DoActionObj;
import ecpay.payment.integration.domain.QueryTradeObj;
import ecpay.payment.integration.ecpayOperator.EcpayFunction;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;


/**
 * 用於處理與訂單相關的操作的服務層
 * 包括(新增、刪除、修改、查詢)訂單、更新訂單狀態、查詢某會員全部訂單 等功能
 */
@Service
@Transactional
public class MemberBuyTicketOrderService {
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private MemberBuyTicketOrderRepository memberBuyTicketOrderRepository;
	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private MemberBuyTicketDetailService memberBuyTicketDetailService;
	@Autowired
	private SeatingListService seatingListService;
	@Autowired
    private RedisTemplate<String, String> redisTemplate;
	@Value("${returnURL}")
	private String returnURL;
	@Value("${backReturnURL}")
	private String backReturnURL;
	private static final AllInOne all = new AllInOne("");

	/**
	 * 根據 member 創建訂單
	 * @param member 是用戶 * 
	 * @return 此新建立 order 的 id
	 */
	public MemberBuyTicketOrderBean insert(
            Integer memberId,
            Integer movieId,
            BigDecimal totalAmount,
            MemberBuyTicketDetailRequestDto orderDetail) {
        // 驗證會員是否存在
        MemberBean member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException("Member not found", 404));

        // 驗證電影是否存在
        MovieBean movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new CustomException("Movie not found", 404));

        // 嘗試鎖定所有座位
        List<String> successfullyLockedSeats = new ArrayList<>();
		try {
			for(String seat : orderDetail.getSeats()) {
				seatingListService.lockSeat(orderDetail.getAuditoriumScheduleId(), seat);
				successfullyLockedSeats.add(seat);
			}
		} catch (Exception e) {
			for(String seat : successfullyLockedSeats) {
				seatingListService.releaseSeat(orderDetail.getAuditoriumScheduleId(), seat);
			}
            throw new CustomException("Failed to lock seat. ", 400);
		}
        // 創建訂單主表
        MemberBuyTicketOrderBean newOrder = new MemberBuyTicketOrderBean();
        newOrder.setMember(member);
        newOrder.setMemberId(member.getId());
        newOrder.setMovieBean(movie);
        newOrder.setMovieId(movieId);
        newOrder.setState("PENDING"); // 訂單狀態初始化
        newOrder.setTotalAmount(totalAmount);
        newOrder = memberBuyTicketOrderRepository.save(newOrder);

        // 創建訂單詳情
        orderDetail.setOrderId(newOrder.getId());
        List<MemberBuyTicketDetailBean> orderDetailList = memberBuyTicketDetailService.insertOrderDetail(orderDetail);
        newOrder.setMemberBuyTicketDetailBeans(orderDetailList);
        

        // 更新座位狀態為已售出
//    	boolean purchaseSuccess = false;
//    	for(String seat : orderDetail.getSeats()) {
//    		purchaseSuccess = seatingListService.purchaseSeat(
//    				orderDetail.getAuditoriumScheduleId(),
//                    seat,
//                    newOrder.getId());
//    		if (!purchaseSuccess) {
//                throw new CustomException("Failed to mark seat " + seat + " as sold.", 400);
//            }
//    	}

        // redis 設定倒計時
        // 綁定訂單到 Redis，設置 15 分鐘過期時間
        String redisKey = "order-expire:" + newOrder.getId();
        redisTemplate.opsForValue().set(redisKey, "PENDING", 2, TimeUnit.MINUTES);
        
     // 在哈希表中保存键值对
        redisTemplate.opsForHash().put("order-details", redisKey, "PENDING");
        
        return newOrder;
    }
	
	/**
	 * 根據 orderId 修改訂單狀態
	 * @param orderId 訂單 Id * 
	 * @param newState 此訂單將要變成的狀態 * 
	 */
	public Integer updateOrderStatus(Integer orderId, String newState) {
        // 查詢訂單

		MemberBuyTicketOrderBean order = memberBuyTicketOrderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found", 404));

		// 驗證新狀態是否有效
        if (!List.of("PENDING", "PAID", "CANCELED", "REFUNDED").contains(newState)) {
            throw new CustomException("Invalid order status: " + newState, 400);
        }
        // 驗證狀態流轉
        if (!isValidStatusTransition(order.getState(), newState)) {
            throw new CustomException("Invalid status transition", 400);
        }

        // 更新狀態
        order.setState(newState);
        memberBuyTicketOrderRepository.save(order);
        if(newState.equals("PAID")) {
        	try {
    			for(MemberBuyTicketDetailBean orderDetail : order.getMemberBuyTicketDetailBeans()) {
    				seatingListService.purchaseSeat(orderDetail.getAuditoriumScheduleId(), orderDetail.getSeat(), order.getId());
    			}
    		} catch (Exception e) {
                throw new CustomException("Failed to set purchase seat. ", 400);
    		}
        }
        redisTemplate.opsForHash().put("order-details", "order-expire:" + orderId, "PAID");        
        
        return orderId;
    }
	
	/**
	 * 根據現在訂單狀態，決定下一狀態是否成立
	 * @param currentStatus 現在訂單狀態 * 
	 * @param newState 現在訂單將要變成的狀態 * 
	 */
	private boolean isValidStatusTransition(String currentStatus, String newStatus) {
		switch (currentStatus.trim()) {
            case "PENDING":
                return newStatus.equals("PAID") || newStatus.equals("CANCELED");
            case "PAID":
                return newStatus.equals("REFUNDED");
            case "CANCELED":
            case "REFUNDED":
                return false; // 已取消或退款的訂單不可變更
            default:
                return false;
        }
    }
	
	/**
	 * 根據 Id 查詢訂單
	 * @param orderId 訂單 Id * 
	 * @return 訂單物件 * 
	 */
	public MemberBuyTicketOrderBean findById(Integer orderId) {
		return memberBuyTicketOrderRepository.findById(orderId)
				.orElseThrow(() -> new CustomException("Order not found", 404));
	}
	
	/**
	 * 根據 Id 刪除訂單
	 * @param orderId 訂單 Id * 
	 */
	public void deleteById(Integer orderId) {
		MemberBuyTicketOrderBean order = memberBuyTicketOrderRepository.findById(orderId)
				.orElseThrow(() -> new CustomException("Order " + orderId + " not found, delete failed", 404));
		// 解鎖座位
	    for (MemberBuyTicketDetailBean detail : order.getMemberBuyTicketDetailBeans()) {
	        seatingListService.releaseSeat(detail.getAuditoriumScheduleId(), detail.getSeat());
	    }
	    memberBuyTicketDetailService.deleteTicketDetailByOrderId(orderId);

		try {
			memberBuyTicketOrderRepository.deleteById(orderId);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 查詢會員的所有訂單
	 * @param memberId 會員 Id 
	 */
	public List<MemberBuyTicketOrderBean> findByMemberId(Integer memberId) {
		MemberBean member = memberRepository.findById(memberId)
				.orElseThrow(() -> new CustomException("Member not found", 404));
		
		List<MemberBuyTicketOrderBean> orderList = member.getMemberBuyTicketOrders();
		if(orderList == null || orderList.size() == 0) {
			throw new CustomException("Member " + member.getAccount() + " didn't have order.", 404);
		}
		
		return orderList;
	}
	//金流service
	public String ecPayService(MemberBuyTicketOrderRequestDto request,Integer id) {
		String aioCheckOutALL = getAioCheckOutALL(request,id);
		System.out.println(aioCheckOutALL);
		aioCheckOutALL = aioCheckOutALL.replaceAll("<script[^>]*>.*?</script>", "");
		System.out.println(aioCheckOutALL);
		return aioCheckOutALL;
	}	
	//金流
	public String getAioCheckOutALL(MemberBuyTicketOrderRequestDto request,Integer id){
		AioCheckOutALL obj = new AioCheckOutALL();
		//產生20碼交易編號亂數
		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
		obj.setMerchantTradeNo(uuId);
		Optional<MemberBuyTicketOrderBean> byId = memberBuyTicketOrderRepository.findById(id);
		if (byId.isPresent()) {
			MemberBuyTicketOrderBean orderBean = byId.get();
			orderBean.setMerchantTradeNo(uuId);
		}
		 // 獲取當前的日期和時間
        LocalDateTime now = LocalDateTime.now();
        // 定義日期時間的格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        // 格式化當前日期時間
        String formattedDateTime = now.format(formatter);
        System.out.println("formattedDateTime");
        System.out.println(formattedDateTime);
        System.out.println(request.getTotalAmount());
        System.out.println(request.getTotalAmount());
		obj.setMerchantTradeDate(formattedDateTime);
		
//		obj.setTotalAmount("114514");
		obj.setTotalAmount(request.getTotalAmount().toString());
//		obj.setTotalAmount(request.getTotalAmount().toString());
		//orderId
		obj.setCustomField1(id.toString());
		String seats="";
		if(request.getOrderDetail()!=null) {
			System.out.println(request.getOrderDetail().toString());
			for (String seat : request.getOrderDetail().getSeats()) {
				
				seats+=seat+" ";
			}
		};
		if(request!=null) {
			MovieBean movieBean = movieRepository.findById(request.getMovieId()) .orElseThrow(() -> new CustomException("Movie not found", 404));
			String tradeDesc=movieBean.getChineseName()+"*"+request.getOrderDetail().getSeats().size()+",";
			tradeDesc=tradeDesc+seats;
			obj.setTradeDesc(tradeDesc);
			String testItem=request.getMovieId()+" ";
			testItem=testItem+seats;
			obj.setItemName(tradeDesc);
		}
		obj.setReturnURL(backReturnURL+"/api/orders/receive");
		obj.setClientBackURL (returnURL+"/booking/ticket-detail/"+id);
//		System.out.println("obj.setOrderResultURL:"+returnURL+"/booking/ticket-detail");
		System.out.println("obj.setReturnURL:"+backReturnURL+"/api/orders/receive");
		obj.setNeedExtraPaidInfo("Y");
		String form = all.aioCheckOut(obj, null);
		return form;
	}
	
	public String refund(RefundRequestDto refundRequest) throws Exception {
        
        // 設定退款參數
		DoActionObj obj = new DoActionObj(); 
		obj.setAction("N");
//		obj.setMerchantID("3002607");
//		obj.setMerchantTradeNo(refundRequest.getMerchantTradeNo());
		obj.setTotalAmount(refundRequest.getTotalAmount());
//		obj.setTotalAmount(refundRequest.getTotalAmount());
		obj.setTradeNo(refundRequest.getTradeNo());
//        refundParams.setMerchantID("3002607");   // 綠界商店代號
////        refundParams.setMerchantTradeNo(refundRequest.getMerchantTradeNo());
//        refundParams.setMerchantTradeNo(refundRequest.getMerchantTradeNo());
////        refundParams.setTotalAmount(refundRequest.getRefundAmount());
//        refundParams.setMerchantTradeDate(refundRequest.getMerchantTradeDate());
//        refundParams.setTradeDesc("test Description");
//        refundParams.setTotalAmount("114514");
//        refundParams.setReturnURL(backReturnURL+"/api/orders/receive");
//		refundParams.setNeedExtraPaidInfo("N");
////        refundParams.setReason(refundRequest.getReason());
        // 呼叫退款 API
		String result;
        try {
            result = all.doAction(obj);
            System.out.println("退款結果: " + result);
        } catch (Exception e) {
            throw new Exception("退款失敗: " + e.getMessage());
        } 

        return result;
    }
	//綠界信用卡付款完成後處理
	public boolean handleEcpayNotification(Hashtable<String, String> notifyParams) {
        try {
        	//驗證檢查碼
            boolean isValid = all.compareCheckMacValue(notifyParams);
            notifyParams.forEach((key, value) -> {
                System.out.println("Key: " + key + ", Value: " + value);
            });
            //確保前後資訊沒有遭到調換
            if (isValid) {
                // 根據通知資料更新訂單狀態
                String merchantTradeNo = notifyParams.get("MerchantTradeNo"); // 訂單編號
                String rtnCodeStr = notifyParams.get("RtnCode"); // 回傳狀態碼
                String paymentDate = notifyParams.get("PaymentDate"); // 付款日期
                
                String checkMacValue=notifyParams.get("CheckMacValue");//驗證碼
                String tradeNo=notifyParams.get("TradeNo");//綠界的訂單編號
                String tradeDate=notifyParams.get("TradeDate");//交易日期
                String id=notifyParams.get("CustomField1");
                System.out.println("id");
                System.out.println(id);
                if(id!=null) {
                	Optional<MemberBuyTicketOrderBean> orserBeanOpt= memberBuyTicketOrderRepository.findById(Integer.parseInt(id));
                	if(orserBeanOpt.isPresent()) {
                		System.out.println("新增");
                		MemberBuyTicketOrderBean ticketOrderBean = orserBeanOpt.get();
                		if(rtnCodeStr!=null) {
                			Integer rtnCode=Integer.parseInt(rtnCodeStr);
                			ticketOrderBean.setRtnCode(rtnCode);
                		}
                		ticketOrderBean.setMerchantTradeNo(merchantTradeNo);
                		ticketOrderBean.setPaymentDate(paymentDate);
                		ticketOrderBean.setCheckMacValue(checkMacValue);
                		ticketOrderBean.setTradeNo(tradeNo);
                		ticketOrderBean.setTradeDate(tradeDate);
                	}	
                }
                //交易狀態:若回傳值為1時，為付款成功
                //其餘代碼皆為交易異常，請至廠商管理後台確認後再出貨。
                if ("1".equals(rtnCodeStr)) {
                    // 處理成功邏輯，例如更新資料庫訂單狀態
                    System.out.println("訂單：" + merchantTradeNo + " 已成功支付，付款時間：" + paymentDate);
                    
                    
                } else {
                    // 處理其他狀態邏輯
                    System.out.println("訂單：" + merchantTradeNo + " 未成功支付，狀態碼：" + rtnCodeStr);
                }
                return true;
	            } else {
	                System.err.println("綠界檢查碼驗證失敗");
	                return false;
	            }
        } catch (Exception e) {
            System.err.println("處理綠界通知失敗：" + e.getMessage());
            return false;
        }
    }
	public Map<String,Object> setOrderAndMovieNameById(Integer id){
		Map<String, Object> returnData = new LinkedHashMap<>();
		if(id!=null && id!=0) {
			MemberBuyTicketOrderBean order =findById(id);
			returnData.put("order",order);
			if(order.getMovieId()!=null && order.getMovieId()!=0) {
				MovieBean movie = movieRepository.findById(order.getMovieId()).orElseThrow(() -> new CustomException("Movie not found", 404));
				returnData.put("movieName", movie.getChineseName());
				returnData.put("movieReleasedDate", movie.getReleasedDate());
			}
			returnData.put("storeName",order.getMemberBuyTicketDetailBeans().get(0).getStore().getName());
			
		}
		return returnData;
	}
	
}

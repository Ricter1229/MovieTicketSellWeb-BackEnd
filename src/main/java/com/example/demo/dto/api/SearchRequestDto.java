package com.example.demo.dto.api;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.domain.StoreSubPhotoBean;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
//影城外部頁面dto
@Component
@Data
public class SearchRequestDto {
	 private String MerchantTradeNo; // 原訂單編號
	 private String refundAmount;   // 退款金額
	 private String tradeNo;
//	 private String reason;         // 退款原因

	
}

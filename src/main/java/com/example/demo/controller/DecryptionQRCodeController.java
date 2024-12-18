package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.QRcodeTRequestDto;
import com.example.demo.service.EncryptionService;
import com.example.demo.service.booking.MemberBuyTicketOrderService;

@RestController
@CrossOrigin
public class DecryptionQRCodeController {
    private static final String AES = "AES";
//    private static final byte[] SECRET_KEY = "1234567890123456".getBytes(); // 确保密钥为16字节
    
	@Autowired
	private EncryptionService encryptionService;

	@Autowired 
	private MemberBuyTicketOrderService memberBuyTicketOrderService;
    /**
     * 接收上传的二维码图片并解密内容
     */
    @PostMapping("/decrypt-qrcode")
    public Map<String, String> decryptQRCode(@RequestBody QRcodeTRequestDto request) throws Exception {
        String encryptedData = request.getData();

        // 解密 QR Code 数据
        String originalText = encryptionService.decrypt(encryptedData);

        JSONObject jsonObject = new JSONObject(originalText);
        
        // 获取 orderId
        int orderId = jsonObject.getInt("orderId");
        System.out.println("orderId" + orderId);
        memberBuyTicketOrderService.updateOrderStatus(orderId, "USED");
        
        // 返回解密结果
        Map<String, String> response = new HashMap<>();
        response.put("originalText", originalText);
        return response;
    }

}

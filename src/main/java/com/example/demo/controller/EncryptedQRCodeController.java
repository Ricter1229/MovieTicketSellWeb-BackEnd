package com.example.demo.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.api.common.ApiResponse;
import com.example.demo.domain.MemberBuyTicketOrderBean;
import com.example.demo.dto.QRcodeTRequestDto;
import com.example.demo.repository.MemberBuyTicketOrderRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.EncryptionService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@RestController
@CrossOrigin
public class EncryptedQRCodeController {
	@Autowired
    private EmailService emailService;
	@Autowired
	private MemberBuyTicketOrderRepository memberBuyTicketOrderRepository;
	@Autowired
	private EncryptionService encryptionService;
	
	@PostMapping("/get-qrcode")
	public ApiResponse<HashMap<String, Object>> getQrcode(@RequestBody QRcodeTRequestDto request) {
		MemberBuyTicketOrderBean order = memberBuyTicketOrderRepository.findById(request.getOrderId()).get();
		HashMap<String, Object> response = new HashMap<>();
        response.put("photo", order.getQrcode());
        return  ApiResponse.success(response) ;
	}
	
    @PostMapping("/generate-qrcode")
    public ApiResponse<HashMap<String, Object>>  generateQRCode(@RequestBody QRcodeTRequestDto request) throws Exception {
    	String encryptedText = request.getEncryptedText();
        String decryptedText = encryptionService.decrypt(encryptedText); // 解密数据，便于调试

     // 解析 JSON 字符串
        JSONObject jsonObject = new JSONObject(decryptedText);
        
        // 获取 orderId
        int orderId = jsonObject.getInt("orderId");
        // 调试打印解密后的数据
        System.out.println("Decrypted Text: " + orderId);

        // 2. 生成 QR Code
        byte[] qrCodeImageBytes = generateQRCodeImage(encryptedText);

        System.out.println(encryptionService.decrypt(encryptedText));
        MemberBuyTicketOrderBean temp = memberBuyTicketOrderRepository.findById(orderId).get();
        temp.setQrcode(qrCodeImageBytes);
        memberBuyTicketOrderRepository.save(temp);
        HashMap<String, Object> response = new HashMap<>();
        response.put("mimeType", "data:image/png;base64,");
        response.put("photo", qrCodeImageBytes);
        return  ApiResponse.success(response) ;
    }
    
    @PostMapping("/send-qrcode")
    public String sendQRCodeEmail(@RequestBody QRcodeTRequestDto request) throws Exception {
     
        byte[] qrCodeImageBytes = memberBuyTicketOrderRepository.findById(request.getOrderId()).get().getQrcode();

        // 3. 发送邮件
        emailService.sendEmailWithQRCode(
                request.getEmail(),
                "Your Encrypted QR Code",
                "<p>Here is your encrypted QR Code:</p><img src='cid:qrcode' />",
                qrCodeImageBytes // 将字节数组传递给邮件服务
        );

        return "Email sent successfully!";
    }

    private byte[] generateQRCodeImage(String text) throws WriterException, IOException {
        int width = 300;
        int height = 300;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        return outputStream.toByteArray();
    }
}

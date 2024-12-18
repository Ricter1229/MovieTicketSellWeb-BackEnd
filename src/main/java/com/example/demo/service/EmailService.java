package com.example.demo.service;

import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendValidationCode(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your Login Validation Code");
        message.setText("驗證碼是: " + code + "\n驗證碼於5分鐘後過期");
        mailSender.send(message);
        System.out.println("Validation code sent to: " + email);
    }

    public void sendResetPasswordEmail(String email, String resetToken) {
        String resetUrl = "http://localhost:5173/secure/reset-password?token=" + resetToken;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("重設密碼");
        message.setText("請點擊以下連結重設密碼:\n" + resetUrl + "\n\n此連結有效期限為5分鐘。");
        mailSender.send(message);
    }
    
    public void sendEmailWithQRCode(String toEmail, String subject, String text, byte[] qrCodeImage) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(text, true);
        saveQRCodeToFile(qrCodeImage, "debug-qrcode.png");

        // 附加 QR Code 图片
        helper.addInline("qrcode", new ByteArrayResource(qrCodeImage), "image/png");
        System.out.println(message);
        mailSender.send(message);
    }
    private void saveQRCodeToFile(byte[] qrCodeImage, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(qrCodeImage);
        }
    }

	public void sendEmailWithQRCode(String email, String subject, String text, String qrCodeImage) {
		// TODO Auto-generated method stub
		
	}

}

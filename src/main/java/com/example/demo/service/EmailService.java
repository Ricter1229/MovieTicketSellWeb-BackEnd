package com.example.demo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
}

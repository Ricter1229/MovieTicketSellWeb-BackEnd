package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

@Service
public class EncryptionService {
	@Value("${secret.key}") // 注入配置项
    private String key;
    private static final String AES = "AES";

    @Autowired
    private SecretKey secretKey; // 从 KeyConfig 注入密钥

    // 加密方法
    public String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(AES); // 使用 AES 算法
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes); // 返回 Base64 编码的加密结果
    }

    // 解密方法
    public String decrypt(String encryptedText) throws Exception {
//    	System.out.println("Injected SecretKey: " + key);
//    	System.out.println("Encrypted Base64: " + encryptedText);
//    	System.out.println("Injected SecretKey: " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));

    	Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText); // Base64 解码
        byte[] decryptedBytes = cipher.doFinal(decodedBytes); // 执行解密
        return new String(decryptedBytes, "UTF-8");
    }
}

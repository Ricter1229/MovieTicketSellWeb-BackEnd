package com.example.demo.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyConfig {
	@Value("${secret.key}") // 注入配置项
    private String key;

    private static final String AES = "AES";

    @Bean
    public SecretKey secretKey() {
        try {
//            KeyGenerator keyGen = KeyGenerator.getInstance(AES);
//            keyGen.init(128); // AES-128
//            return new SecretKeySpec(keyGen.generateKey().getEncoded(), AES);
        	return new SecretKeySpec(key.getBytes(), AES);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize AES key", e);
        }
    }
}
package com.example.demo.exception;

//自訂異常
@SuppressWarnings("serial")
public class CustomException extends RuntimeException {
 private int code; // 自定義錯誤代碼

 public CustomException(String message, int code) {
     super(message);
     this.code = code;
 }

 public int getCode() {
     return code;
 }
}
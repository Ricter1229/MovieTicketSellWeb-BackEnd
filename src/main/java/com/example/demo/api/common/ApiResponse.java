package com.example.demo.api.common;

public class ApiResponse<T> {
	private Integer code; // HTTP 狀態碼或自定義代碼
    private String message; // 錯誤描述
    private T data; // 成功時返回的數據，失敗時為 null
    private String errorDetail; // 錯誤詳細訊息（新增）

    // 成功響應
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Success", data, null);
    }

    // 失敗響應
    public static <T> ApiResponse<T> fail(Integer code, String message, String errorDetail) {
        return new ApiResponse<>(code, message, null, errorDetail);
    }

    // 私有構造方法
    private ApiResponse(Integer code, String message, T data, String errorDetail) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.errorDetail = errorDetail;
    }

    // Getter 和 Setter
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getErrorDetail() {
		return errorDetail;
	}

	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}


}

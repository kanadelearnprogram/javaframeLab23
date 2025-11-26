package com.kanade.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应封装，遵循 develop.md 中 code/msg/data 结构。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private int code;
    private String msg;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "操作成功", data);
    }

    public static <T> ApiResponse<T> success(String msg, T data) {
        return new ApiResponse<>(200, msg, data);
    }

    public static ApiResponse<Void> successMessage(String msg) {
        return new ApiResponse<>(200, msg, null);
    }

    public static <T> ApiResponse<T> failure(int code, String msg) {
        return new ApiResponse<>(code, msg, null);
    }

    public static <T> ApiResponse<T> badRequest(String msg) {
        return failure(400, msg);
    }

    public static <T> ApiResponse<T> unauthorized(String msg) {
        return failure(401, msg);
    }

    public static <T> ApiResponse<T> forbidden(String msg) {
        return failure(403, msg);
    }

    public static <T> ApiResponse<T> serverError(String msg) {
        return failure(500, msg);
    }
}


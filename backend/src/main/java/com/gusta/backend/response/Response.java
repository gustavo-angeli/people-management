package com.gusta.backend.response;

import lombok.Data;

@Data
public class Response<T> {
    private boolean success;
    private String message;
    private T data;

    private Response(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(true, "Operation successful", data);
    }

    public static <T> Response<T> failure(String message) {
        return new Response<>(false, message, null);
    }
}
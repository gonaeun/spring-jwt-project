package com.gonaeun.springjwtproject.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private final boolean success;
    private final T data;

    public static <T> ApiResponse<T> ok(T data)      { return new ApiResponse<>(true,  data); }
    public static <T> ApiResponse<T> fail(T error)   { return new ApiResponse<>(false, error); }
}

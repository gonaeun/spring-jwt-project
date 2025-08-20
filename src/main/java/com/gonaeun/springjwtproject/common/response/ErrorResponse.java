package com.gonaeun.springjwtproject.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(name = "ErrorResponse", description = "API 에러 응답")
public class ErrorResponse {

    @Schema(example = "ACCESS_DENIED")
    private final String code;

    @Schema(example = "접근 권한이 없습니다.")
    private final String message;
}

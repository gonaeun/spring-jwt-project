package com.gonaeun.springjwtproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
    @NotBlank(message = "username은 필수입니다.")
    String username,

    @NotBlank(message = "password는 필수입니다.")
    String password,

    @NotBlank(message = "nickname은 필수입니다.")
    String nickname
) {}

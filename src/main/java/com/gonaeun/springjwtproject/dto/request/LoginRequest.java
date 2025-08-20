package com.gonaeun.springjwtproject.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    String username,
    String password
) {}

package com.gonaeun.springjwtproject.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RoleResponse")
public record RoleResponse(
    @Schema(example = "USER") String role
) {}
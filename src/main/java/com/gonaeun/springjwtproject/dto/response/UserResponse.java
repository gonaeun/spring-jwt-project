package com.gonaeun.springjwtproject.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "UserResponse")
public record UserResponse(
    @Schema(example = "JIN HO") String username,
    @Schema(example = "Mentos") String nickname,
    @ArraySchema(schema = @Schema(implementation = RoleResponse.class))
    List<RoleResponse> roles
) {}

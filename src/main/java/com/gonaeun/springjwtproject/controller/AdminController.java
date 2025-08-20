package com.gonaeun.springjwtproject.controller;

import com.gonaeun.springjwtproject.common.response.ErrorResponse;
import com.gonaeun.springjwtproject.dto.response.UserResponse;
import com.gonaeun.springjwtproject.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @Operation(description = "특정 사용자에게 ADMIN 권한을 부여 (관리자 전용)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "관리자 권한 부여 성공",
            content = @Content(schema = @Schema(implementation = UserResponse.class),
                examples = @ExampleObject(
                    value = "{ \"username\": \"JIN HO\", \"nickname\": \"Mentos\", \"roles\": [{\"role\": \"ADMIN\"}] }"
                ))),
        @ApiResponse(responseCode = "401", description = "토큰이 유효하지 않거나 만료된 경우",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = "{ \"error\": { \"code\": \"INVALID_TOKEN\", \"message\": \"유효하지 않은 인증 토큰입니다.\" } }"
                ))),
        @ApiResponse(responseCode = "403", description = "접근 권한 없음 (ACCESS_DENIED)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = "{ \"error\": { \"code\": \"ACCESS_DENIED\", \"message\": \"접근 권한이 없습니다.\" } }"
                ))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 (USER_NOT_FOUND)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = "{ \"error\": { \"code\": \"USER_NOT_FOUND\", \"message\": \"존재하지 않는 사용자입니다.\" } }"
                )))
    })
    @PatchMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 호출 가능
    public ResponseEntity<UserResponse> grantAdminRole(
        @Parameter(description = "관리자 권한을 부여할 사용자 ID")
        @PathVariable Long userId) {
        return ResponseEntity.ok(userService.grantAdminRole(userId));
    }
}

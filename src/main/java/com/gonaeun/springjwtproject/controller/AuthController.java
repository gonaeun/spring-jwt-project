package com.gonaeun.springjwtproject.controller;

import com.gonaeun.springjwtproject.common.response.ErrorResponse;
import com.gonaeun.springjwtproject.dto.request.LoginRequest;
import com.gonaeun.springjwtproject.dto.request.SignUpRequest;
import com.gonaeun.springjwtproject.dto.response.LoginResponse;
import com.gonaeun.springjwtproject.dto.response.UserResponse;
import com.gonaeun.springjwtproject.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "회원가입 성공",
            content = @Content(schema = @Schema(implementation = UserResponse.class),
                examples = @ExampleObject(
                    value = "{ \"username\": \"JIN HO\", \"nickname\": \"Mentos\", \"roles\": [{\"role\": \"USER\"}] }"
                ))),
        @ApiResponse(responseCode = "400", description = "회원가입 실패 (이미 가입된 사용자)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = "{ \"error\": { \"code\": \"USER_ALREADY_EXISTS\", \"message\": \"이미 가입된 사용자입니다.\" } }"
                )))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @Content(
            schema = @Schema(implementation = SignUpRequest.class),
            examples = @ExampleObject(
                value = "{ \"username\": \"JIN HO\", \"password\": \"12341234\", \"nickname\": \"Mentos\" }"
            )
        )
    )
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody @Valid SignUpRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(req));
    }

    @Operation(summary = "로그인")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "로그인 성공 (토큰 반환)",
            content = @Content(schema = @Schema(implementation = LoginResponse.class),
                examples = @ExampleObject(
                    value = "{ \"token\": \"eKDIkdfjoakIdkfjpekdkcjdkoIOdjOKJDFOlLDKFJKL\" }"
                ))),
        @ApiResponse(responseCode = "401", description = "로그인 실패 (잘못된 계정 정보)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = "{ \"error\": { \"code\": \"INVALID_CREDENTIALS\", \"message\": \"아이디 또는 비밀번호가 올바르지 않습니다.\" } }"
                )))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @Content(
            schema = @Schema(implementation = LoginRequest.class),
            examples = @ExampleObject(
                value = "{ \"username\": \"JIN HO\", \"password\": \"12341234\" }"
            )
        )
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }
}

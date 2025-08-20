package com.gonaeun.springjwtproject.controller;

import com.gonaeun.springjwtproject.dto.request.SignUpRequest;
import com.gonaeun.springjwtproject.dto.response.UserResponse;
import com.gonaeun.springjwtproject.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public UserResponse signUp(@RequestBody @Valid SignUpRequest req) {
        return authService.signUp(req);
    }
}

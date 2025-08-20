package com.gonaeun.springjwtproject.service;

import com.gonaeun.springjwtproject.common.exception.CustomException;
import com.gonaeun.springjwtproject.common.exception.ErrorCode;
import com.gonaeun.springjwtproject.domain.Role;
import com.gonaeun.springjwtproject.domain.User;
import com.gonaeun.springjwtproject.dto.request.LoginRequest;
import com.gonaeun.springjwtproject.dto.request.SignUpRequest;
import com.gonaeun.springjwtproject.dto.response.LoginResponse;
import com.gonaeun.springjwtproject.dto.response.RoleResponse;
import com.gonaeun.springjwtproject.dto.response.UserResponse;
import com.gonaeun.springjwtproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse signUp(SignUpRequest req) {
        // 중복 사용자 체크
        userRepository.findByUsername(req.username())
            .ifPresent(u -> { throw new CustomException(ErrorCode.USER_ALREADY_EXISTS); });

        // 저장 (기본 권한 USER)
        User saved = userRepository.save(User.builder()
            .username(req.username())
            .password(passwordEncoder.encode(req.password()))
            .nickname(req.nickname())          // 닉네임 저장
            .role(Role.USER)                   // 기본 권한
            .build());

        return new UserResponse(
            saved.getUsername(),
            saved.getNickname(),
            List.of(new RoleResponse(saved.getRole().name()))
        );
    }

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest req) {
        User user = userRepository.findByUsername(req.username())
            .orElseThrow(() -> new CustomException(
                ErrorCode.INVALID_CREDENTIALS, "아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new CustomException(
                ErrorCode.INVALID_CREDENTIALS, "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        // 흐름 검증용 임시 토큰. jwt로 교체 예정
        String token = "TEMP_TOKEN_" + user.getId();
        return new LoginResponse(token);
    }

}

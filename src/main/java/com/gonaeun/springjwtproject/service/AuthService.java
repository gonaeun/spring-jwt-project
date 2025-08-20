package com.gonaeun.springjwtproject.service;

import com.gonaeun.springjwtproject.common.exception.CustomException;
import com.gonaeun.springjwtproject.common.exception.ErrorCode;
import com.gonaeun.springjwtproject.domain.Role;
import com.gonaeun.springjwtproject.domain.User;
import com.gonaeun.springjwtproject.dto.request.SignUpRequest;
import com.gonaeun.springjwtproject.dto.response.UserResponse;
import com.gonaeun.springjwtproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse signUp(SignUpRequest req) {
        userRepository.findByUsername(req.username())
            .ifPresent(u -> { throw new CustomException(ErrorCode.USER_ALREADY_EXISTS); });

        User saved = userRepository.save(User.builder()
            .username(req.username())
            .password(passwordEncoder.encode(req.password()))
            .role(Role.USER)
            .build());

        return new UserResponse(saved.getId(), saved.getUsername(), saved.getRole().name());
    }
}

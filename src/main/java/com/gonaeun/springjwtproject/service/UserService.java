package com.gonaeun.springjwtproject.service;

import com.gonaeun.springjwtproject.common.exception.CustomException;
import com.gonaeun.springjwtproject.common.exception.ErrorCode;
import com.gonaeun.springjwtproject.domain.Role;
import com.gonaeun.springjwtproject.domain.User;
import com.gonaeun.springjwtproject.dto.response.RoleResponse;
import com.gonaeun.springjwtproject.dto.response.UserResponse;
import com.gonaeun.springjwtproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse grantAdminRole(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.setRole(Role.ADMIN);
        userRepository.save(user);

        return new UserResponse(
            user.getUsername(),
            user.getNickname(),
            List.of(new RoleResponse(user.getRole().name()))
        );
    }
}

package com.gonaeun.springjwtproject.controller;

import com.gonaeun.springjwtproject.dto.response.UserResponse;
import com.gonaeun.springjwtproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PatchMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 호출 가능
    public ResponseEntity<UserResponse> grantAdminRole(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.grantAdminRole(userId));
    }
}

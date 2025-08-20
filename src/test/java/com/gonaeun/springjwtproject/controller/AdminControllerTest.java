package com.gonaeun.springjwtproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonaeun.springjwtproject.domain.Role;
import com.gonaeun.springjwtproject.domain.User;
import com.gonaeun.springjwtproject.dto.request.LoginRequest;
import com.gonaeun.springjwtproject.dto.request.SignUpRequest;
import com.gonaeun.springjwtproject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private String adminToken;
    private String userToken;

    @BeforeEach
    void setUp() throws Exception {
        // 매번 다른 username을 써서 충돌 방지
        String adminUsername = "admin_" + System.currentTimeMillis();
        String userUsername = "user_" + System.currentTimeMillis();

        // 관리자 계정 회원가입
        SignUpRequest adminSignup = new SignUpRequest(adminUsername, "1234", "관리자");
        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminSignup)))
            .andExpect(status().isCreated());

        // 일반 사용자 계정 회원가입
        SignUpRequest userSignup = new SignUpRequest(userUsername, "1234", "일반유저");
        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignup)))
            .andExpect(status().isCreated());

        // 관리자 계정 role 직접 바꾸기 (USER → ADMIN)
        User admin = userRepository.findByUsername(adminUsername).orElseThrow();
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        // 토큰 발급
        adminToken = getToken(adminUsername, "1234");
        userToken = getToken(userUsername, "1234");
    }

    private String getToken(String username, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest(username, password);

        MvcResult result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        // {"token":"..."} → token 값만 뽑아내기
        return objectMapper.readTree(responseBody).get("token").asText();
    }

    @Test
    @DisplayName("관리자 권한 사용자가 요청할 때 정상 처리")
    void grantAdminRole_success_byAdmin() throws Exception {
        // 일반 사용자 ID 찾기
        User targetUser = userRepository.findAll().stream()
            .filter(u -> u.getRole() == Role.USER)
            .findFirst()
            .orElseThrow();

        mockMvc.perform(patch("/admin/users/{id}/roles", targetUser.getId())
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.roles[0].role").value("ADMIN"));
    }

    @Test
    @DisplayName("일반 사용자가 요청할 때 권한 오류")
    void grantAdminRole_fail_byUser() throws Exception {
        // 일반 사용자 ID 찾기
        User targetUser = userRepository.findAll().stream()
            .filter(u -> u.getRole() == Role.USER)
            .findFirst()
            .orElseThrow();

        mockMvc.perform(patch("/admin/users/{id}/roles", targetUser.getId())
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.error.code").value("ACCESS_DENIED"));
    }

    @Test
    @DisplayName("존재하지 않는 사용자에게 권한 부여 시도")
    void grantAdminRole_fail_notFoundUser() throws Exception {
        mockMvc.perform(patch("/admin/users/99999/roles")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error.code").value("USER_NOT_FOUND"));
    }
}

package com.gonaeun.springjwtproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonaeun.springjwtproject.dto.request.LoginRequest;
import com.gonaeun.springjwtproject.dto.request.SignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공")
    void signUp_success() throws Exception {
        // 매번 고유한 username 생성
        String uniqueUsername = "user_" + System.currentTimeMillis();
        SignUpRequest request = new SignUpRequest(uniqueUsername, "1234", "닉네임1");

        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value(uniqueUsername))
            .andExpect(jsonPath("$.roles[0].role").value("USER"));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 사용자")
    void signUp_fail_userExists() throws Exception {
        SignUpRequest request = new SignUpRequest("user1", "1234", "닉네임1");

        // 먼저 가입
        mockMvc.perform(post("/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        // 다시 같은 계정으로 가입 시도
        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error.code").value("USER_ALREADY_EXISTS"));
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() throws Exception {
        // given
        SignUpRequest signup = new SignUpRequest("user2", "1234", "닉네임2");
        mockMvc.perform(post("/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signup)));

        LoginRequest request = new LoginRequest("user2", "1234");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 계정 정보")
    void login_fail_invalidCredentials() throws Exception {
        LoginRequest request = new LoginRequest("nouser", "wrongpw");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error.code").value("INVALID_CREDENTIALS"));
    }
}

package com.gonaeun.springjwtproject.security;

import com.gonaeun.springjwtproject.common.exception.CustomException;
import com.gonaeun.springjwtproject.common.exception.ErrorCode;
import com.gonaeun.springjwtproject.common.util.JwtProvider;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        if (token != null) {
            try {
                if (!jwtProvider.validateToken(token)) {
                    setErrorResponse(response, ErrorCode.INVALID_TOKEN);
                    return; // 필터 체인 중단
                }
                Authentication auth = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (JwtException | IllegalArgumentException e) {
                setErrorResponse(response, ErrorCode.INVALID_TOKEN);
                return; // 필터 체인 중단
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode code) throws IOException {
        response.setStatus(code.getStatus().value());
        response.setContentType("application/json; charset=UTF-8");
        String body = String.format("{\"error\": {\"code\": \"%s\", \"message\": \"%s\"}}",
            code.name(), code.getDefaultMessage());
        response.getWriter().write(body);
    }
}

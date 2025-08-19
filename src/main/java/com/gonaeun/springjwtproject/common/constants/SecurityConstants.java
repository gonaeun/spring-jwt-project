package com.gonaeun.springjwtproject.common.constants;

public final class SecurityConstants {
    private SecurityConstants() {}
    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final long ACCESS_TOKEN_EXP_MS = 1000L * 60 * 60 * 2; // 2시간
}
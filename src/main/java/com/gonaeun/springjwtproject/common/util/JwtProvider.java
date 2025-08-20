package com.gonaeun.springjwtproject.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtProvider { // 토큰 생성 및 검증

    // 최소 32자 이상 (HS256 요구사항)
    private final String secretKey = "mySuperSecretKeyForJwtDemo1234567890";
    private final long validityInMs = 2 * 60 * 60 * 1000; // 2시간
    private final Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

    // 토큰 생성
    public String createToken(String username, Set<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);

        Set<String> authorities = roles.stream()
            .map(r -> "ROLE_" + r)   // USER → ROLE_USER, ADMIN → ROLE_ADMIN
            .collect(Collectors.toSet());

        claims.put("roles", authorities);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    // 토큰에서 인증 정보 추출
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String username = claims.getSubject();
        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) claims.get("roles");

        List<GrantedAuthority> authorities = roles.stream()
            .map(SimpleGrantedAuthority::new) // JWT에 ROLE_ADMIN이 들어있음
            .collect(Collectors.toList());

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, "", authorities);
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}

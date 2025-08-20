package com.gonaeun.springjwtproject.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "JWT 인증/인가 및 권한 부여 API 명세서",
        version = "v1.0.0"
    )
)
public class OpenApiConfig {}

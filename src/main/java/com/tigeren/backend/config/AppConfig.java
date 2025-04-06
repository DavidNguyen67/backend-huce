package com.tigeren.backend.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConfig {
    private final String jwtSecret = "bb8b7d28bc9afab9a001573b50431c21";
    private final Integer jwtExpiration = 3 * 24 * 60 * 60 * 1000; // 3 days in milliseconds
}

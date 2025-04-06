package com.tigeren.backend.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, java.io.IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401

        String message = "Unauthorized or invalid token";

        if (authException.getCause() instanceof ExpiredJwtException) {
            message = "Token has expired";
        } else if (authException.getCause() instanceof MalformedJwtException) {
            message = "Token is malformed";
        } else if (authException.getCause() instanceof SignatureException) {
            message = "Token signature is invalid";
        }

        response.getWriter().write("{\"message\": \"" + message + "\"}");
    }
}

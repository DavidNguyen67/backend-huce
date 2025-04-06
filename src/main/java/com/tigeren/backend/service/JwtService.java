package com.tigeren.backend.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.tigeren.backend.config.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final AppConfig appConfig;
    private final Algorithm algorithm;

    public JwtService(AppConfig appConfig) {
        this.appConfig = appConfig;
        this.algorithm = Algorithm.HMAC256(appConfig.getJwtSecret());
    }

    public String generateToken(String userId, String email, String role) {
        return com.auth0.jwt.JWT.create()
                .withSubject(userId)
                .withClaim("email", email)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + appConfig.getJwtExpiration()))
                .sign(algorithm);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(appConfig.getJwtSecret().getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()));
    }

    public UserDetails loadUserByUsername(String username) {
        // Tùy vào logic của bạn, ví dụ lấy từ DB
        return new User(username, "", Collections.emptyList());
    }
}

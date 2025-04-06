package com.tigeren.backend.controller;

import com.tigeren.backend.dto.LoginUserDTO;
import com.tigeren.backend.dto.OAuth2DTO;
import com.tigeren.backend.dto.RegisterUserDTO;
import com.tigeren.backend.response.AuthResponse;
import com.tigeren.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleLogin(@Valid @RequestBody OAuth2DTO oAuth2DTO) {
        AuthResponse authResponse = this.authService.getUserInfoFromGoogleAccessToken(oAuth2DTO.getAccessToken());
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterUserDTO registerUserDTO) {
        AuthResponse authResponse = authService.registerUser(registerUserDTO);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginUserDTO loginUserDTO) {
        AuthResponse authResponse = authService.loginUser(loginUserDTO);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}

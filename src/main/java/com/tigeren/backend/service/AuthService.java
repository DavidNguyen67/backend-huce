package com.tigeren.backend.service;

import com.tigeren.backend.dto.GoogleUserInfoDTO;
import com.tigeren.backend.dto.LoginUserDTO;
import com.tigeren.backend.dto.RegisterUserDTO;
import com.tigeren.backend.entity.Role;
import com.tigeren.backend.entity.User;
import com.tigeren.backend.repository.RoleRepository;
import com.tigeren.backend.repository.UserRepository;
import com.tigeren.backend.response.AuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final GoogleApiService googleApiService;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    public AuthService(GoogleApiService googleApiService, UserRepository userRepository, JwtService jwtService,
                       RoleRepository roleRepository) {
        this.googleApiService = googleApiService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public AuthResponse getUserInfoFromGoogleAccessToken(String googleAccessToken) {
        try {
            GoogleUserInfoDTO userInfo = googleApiService.getUserInfo(googleAccessToken);

            log.info("userInfo: {}", userInfo.getEmail());

            Optional<User> userOpt = userRepository.findByEmail(userInfo.getEmail());
            User newUser;

            if (userOpt.isPresent()) {
                log.info("User already exists: {}", userOpt.get().getEmail());
                newUser = userOpt.get();

                AuthResponse authResponse = new AuthResponse();
                Role savedRole = newUser.getRole();

                String accessToken = jwtService.generateToken(newUser.getId(), newUser.getEmail(), savedRole != null ? savedRole.getId() : null);
                String refreshToken = UUID.randomUUID().toString();

                authResponse.setAccessToken(accessToken);
                authResponse.setRefreshToken(refreshToken);

                return authResponse;
            }

            log.info("User does not exist: {}", userInfo.getEmail());

            RegisterUserDTO registerUserDTO = new RegisterUserDTO();

            registerUserDTO.setEmail(userInfo.getEmail());
            registerUserDTO.setFirstName(userInfo.getGiven_name());
            registerUserDTO.setLastName(userInfo.getFamily_name());
            registerUserDTO.setPassword(userInfo.getId());
            registerUserDTO.setConfirmPassword(userInfo.getId());

            return this.registerUser(registerUserDTO);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public AuthResponse registerUser(RegisterUserDTO registerUserDTO) {
        User user = new User();

        String hashPassword = BCrypt.hashpw(registerUserDTO.getPassword(), BCrypt.gensalt());

        user.setId(UUID.randomUUID().toString());
        user.setEmail(registerUserDTO.getEmail());
        user.setFirstName(registerUserDTO.getFirstName());
        user.setLastName(registerUserDTO.getLastName());
        Role role = registerUserDTO.getRoleId() != null
                ? roleRepository.findById(registerUserDTO.getRoleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role ID")) : null;

        user.setRole(role);
        user.setPassword(hashPassword);

        User savedUser = userRepository.save(user);

        AuthResponse authResponse = new AuthResponse();
        String accessToken = jwtService.generateToken(savedUser.getId(), savedUser.getEmail(), savedUser.getRole() != null ? savedUser.getRole().getId() : null);
        String refreshToken = UUID.randomUUID().toString();

        log.info("accessToken: {}", accessToken);

        authResponse.setAccessToken(accessToken);
        authResponse.setRefreshToken(refreshToken);
        return authResponse;

    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public AuthResponse loginUser(LoginUserDTO loginUserDTO) {
        Optional<User> userOpt = userRepository.findByEmail(loginUserDTO.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (BCrypt.checkpw(loginUserDTO.getPassword(), user.getPassword())) {
                AuthResponse authResponse = new AuthResponse();
                String accessToken = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole().getId());
                String refreshToken = UUID.randomUUID().toString();

                authResponse.setAccessToken(accessToken);
                authResponse.setRefreshToken(refreshToken);
                return authResponse;
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

    }
}

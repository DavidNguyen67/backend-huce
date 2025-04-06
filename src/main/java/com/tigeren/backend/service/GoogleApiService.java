package com.tigeren.backend.service;

import com.tigeren.backend.dto.GoogleUserInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class GoogleApiService {

    private final RestTemplate restTemplate;

    public GoogleApiService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public GoogleUserInfoDTO getUserInfo(String accessToken) {
        String url = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + accessToken;

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<GoogleUserInfoDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                GoogleUserInfoDTO.class
        );

        return response.getBody();
    }
}

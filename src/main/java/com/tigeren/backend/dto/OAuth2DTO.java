package com.tigeren.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuth2DTO {
    @NotBlank(message = "{access_token_required}")
    private String accessToken;
}

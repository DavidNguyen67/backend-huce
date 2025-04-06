package com.tigeren.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginUserDTO {
    @NotBlank(message = "{email_required}")
    @Email(message = "{email_invalid}")
    private String email;

    @NotBlank(message = "{password_required}")
    private String password;
}

package com.tigeren.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterUserDTO extends LoginUserDTO {
    @NotBlank(message = "{first_name_required}")
    private String firstName;

    @NotBlank(message = "{last_name_required}")
    private String lastName;

    @NotBlank(message = "{confirm_password_required}")
    private String confirmPassword;

    @NotBlank(message = "{role_id_required}")
    private String roleId;
}

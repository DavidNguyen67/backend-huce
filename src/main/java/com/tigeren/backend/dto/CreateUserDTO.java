package com.tigeren.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDTO {
    @NotBlank(message = "{email_required}")
    @Email(message = "{email_invalid}")
    private String email;

    @NotBlank(message = "{first_name_required}")
    private String firstName;

    @NotBlank(message = "{last_name_required}")
    private String lastName;

    private Boolean active;
}

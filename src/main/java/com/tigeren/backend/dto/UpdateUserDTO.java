package com.tigeren.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserDTO {
    @NotNull(message = "{user_id_required}")
    private String id;

    @NotBlank(message = "{email_required}")
    @Email(message = "{email_invalid}")
    private String email;

    @NotBlank(message = "{first_name_required}")
    private String firstName;

    @NotBlank(message = "{last_name_required}")
    private String lastName;

    private Boolean active;
}

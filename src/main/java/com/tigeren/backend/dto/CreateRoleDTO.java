package com.tigeren.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoleDTO {
    @NotBlank(message = "{role_name_required}")
    private String name;
}

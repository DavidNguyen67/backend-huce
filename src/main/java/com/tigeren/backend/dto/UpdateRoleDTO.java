package com.tigeren.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateRoleDTO extends CreateRoleDTO {
    @NotNull(message = "{role_id_required}")
    private String id;
}

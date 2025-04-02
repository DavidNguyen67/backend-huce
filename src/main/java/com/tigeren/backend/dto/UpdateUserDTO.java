package com.tigeren.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO extends CreateUserDTO {
    @NotNull(message = "{user_id_required}")
    private String id;

}

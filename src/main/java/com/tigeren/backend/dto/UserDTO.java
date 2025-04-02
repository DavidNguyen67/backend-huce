package com.tigeren.backend.dto;

import com.tigeren.backend.entity.Base;
import lombok.Getter;

@Getter
public class UserDTO extends Base {
    private String email;
    private String firstName;
    private String lastName;
    private Boolean active;
    private RoleDTO role;
}

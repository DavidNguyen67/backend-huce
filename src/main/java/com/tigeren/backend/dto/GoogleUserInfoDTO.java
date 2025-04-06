package com.tigeren.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleUserInfoDTO {
    private String id;

    private String email;

    private String name;

    private String given_name;

    private String family_name;

    private String picture;

    private String locale;
}

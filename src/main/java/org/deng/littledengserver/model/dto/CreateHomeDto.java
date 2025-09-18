package org.deng.littledengserver.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateHomeDto {
    @NotBlank
    private String token;

    private String userName;

    private String userRole;

    private String homeName;

    private String avatarBase64;
}

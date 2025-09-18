package org.deng.littledengserver.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserJoinHomeDto {
    @NotBlank
    private String token;

    private String userName;

    private String userRole;

    private String homeCode;

    private String avatarBase64;
}

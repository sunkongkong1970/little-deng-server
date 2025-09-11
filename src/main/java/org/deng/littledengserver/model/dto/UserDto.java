package org.deng.littledengserver.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    @NotBlank
    private String openid;

    @NotBlank
    private String userName;

    @NotBlank
    private String userAvatarUrl;

    @NotBlank
    private String userRoleName;

    @NotBlank
    private Long homeId;

    @NotBlank
    private Boolean isHouseholder;
}

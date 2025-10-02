package org.deng.littledengserver.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateHomeVo {
    @NotBlank
    private String token;

    private String userName;

    private String userRole;

    private String homeName;

    private String avatarBase64;
}

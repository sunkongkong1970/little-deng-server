package org.deng.littledengserver.model.vo;

import lombok.Data;

@Data
public class UserEditVo {
    private String token;

    private String userName;

    private String userRole;

    private String userAvatarBase64;

    private String userAvatarUrl;
}

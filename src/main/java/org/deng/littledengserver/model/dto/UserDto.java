package org.deng.littledengserver.model.dto;

import lombok.Data;
import org.deng.littledengserver.config.dict.Dict;
import org.deng.littledengserver.constant.DictConstant;

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
    @Dict(type = DictConstant.ROLE)
    private String userRole;

    @NotBlank
    private Boolean isHouseholder;
}

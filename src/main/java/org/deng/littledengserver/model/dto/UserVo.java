package org.deng.littledengserver.model.dto;

import lombok.Data;
import org.deng.littledengserver.config.dict.Dict;
import org.deng.littledengserver.constant.DictConstant;

@Data
public class UserVo {
    private String openid;

    private String userName;

    @Dict(type = DictConstant.ROLE)
    private String userRole;

    private Boolean isHouseholder;

    private Long homeId;
}

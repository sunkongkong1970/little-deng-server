package org.deng.littledengserver.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.deng.littledengserver.config.dict.Dict;
import org.deng.littledengserver.config.dict.DictTranslatable;
import org.deng.littledengserver.constant.DictConstant;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserVo extends DictTranslatable {
    private String openid;

    private String userName;

    @Dict(type = DictConstant.ROLE)
    private String userRole;

    private Boolean isHouseholder;

    private Long homeId;

    private String userAvatarBase64;

    private String userAvatarUrl;
}

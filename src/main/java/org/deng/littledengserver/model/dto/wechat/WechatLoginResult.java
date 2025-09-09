package org.deng.littledengserver.model.dto.wechat;

import lombok.Data;

@Data
public class WechatLoginResult {
    private String openid;       // 用户唯一标识
    private String sessionKey;   // 会话密钥
    private String unionid;      // 多平台统一标识
    private Integer errcode;     // 错误码
    private String errmsg;       // 错误信息

    // 判断登录是否成功
    public boolean isSuccess() {
        return errcode == null || errcode == 0;
    }
}

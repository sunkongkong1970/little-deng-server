package org.deng.littledengserver.model.dto.wechat;

import lombok.Data;

@Data
public class WeChatLoginResponse {
    private String token;    // 自定义登录态
    private String openid;   // 用户唯一标识

    public WeChatLoginResponse(String token, String openid) {
        this.token = token;
        this.openid = openid;
    }
}

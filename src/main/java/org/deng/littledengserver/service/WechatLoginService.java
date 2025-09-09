package org.deng.littledengserver.service;

import org.deng.littledengserver.model.dto.wechat.WeChatLoginResponse;

public interface WechatLoginService {
    WeChatLoginResponse login(String code);
}

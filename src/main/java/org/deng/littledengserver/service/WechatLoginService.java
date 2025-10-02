package org.deng.littledengserver.service;

import org.deng.littledengserver.model.vo.wechat.WeChatLoginResponse;

public interface WechatLoginService {
    WeChatLoginResponse login(String code);
}

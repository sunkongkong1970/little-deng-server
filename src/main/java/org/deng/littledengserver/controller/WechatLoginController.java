package org.deng.littledengserver.controller;

import org.deng.littledengserver.config.BaseResult;
import org.deng.littledengserver.model.dto.wechat.WeChatLoginResponse;
import org.deng.littledengserver.service.WechatLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wechat")
public class WechatLoginController {

    @Autowired
    private WechatLoginService wechatLoginService;

    @PostMapping("/login")
    public BaseResult<WeChatLoginResponse> login(@RequestParam("code") String code) {
        return BaseResult.success(wechatLoginService.login(code));
    }
}




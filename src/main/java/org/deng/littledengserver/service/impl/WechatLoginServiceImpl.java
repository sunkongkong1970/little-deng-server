package org.deng.littledengserver.service.impl;

import com.alibaba.fastjson2.JSONObject;
import org.deng.littledengserver.config.BusinessException;
import org.deng.littledengserver.constant.ErrorEnum;
import org.deng.littledengserver.model.entity.UserEntity;
import org.deng.littledengserver.model.vo.wechat.WeChatLoginResponse;
import org.deng.littledengserver.model.vo.wechat.WechatLoginResult;
import org.deng.littledengserver.repository.UserRepository;
import org.deng.littledengserver.service.UserService;
import org.deng.littledengserver.service.WechatLoginService;
import org.deng.littledengserver.util.CacheUtil;
import org.deng.littledengserver.util.WeChatUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class WechatLoginServiceImpl implements WechatLoginService {
    @Resource
    private WeChatUtil wechatLoginUtil;
    @Resource
    private UserRepository userRepository;
    @Resource
    private UserService userService;

    @Override
    @Transactional
    public WeChatLoginResponse login(String code) {
        // 1. 调用工具类获取微信登录信息
        WechatLoginResult loginResult = wechatLoginUtil.getOpenidAndSessionKey(code);
        // 2. 检查登录结果
        if (!loginResult.isSuccess()) {
            throw new BusinessException(ErrorEnum.WE_CHAT_LOGIN_FAIL.getCode(), ErrorEnum.WE_CHAT_LOGIN_FAIL.getMessage() + ":" + loginResult.getErrmsg());
        }

        String openid = loginResult.getOpenid();
        String sessionKey = loginResult.getSessionKey();

        String token = CacheUtil.generateToken(openid, sessionKey);

        // 4. 检查用户是否已存在，不存在则创建新用户
        UserEntity user = userService.getByCode(openid);
        if (user == null) {
            user = new UserEntity();
            user.setOpenid(openid);
            user.setUnionid(loginResult.getUnionid());
            userRepository.save(user);
        }

        // 7. 返回登录结果（不包含sessionKey）
        return new WeChatLoginResponse(token, openid);
    }

    /**
     * 解密用户手机号
     */
    public String getPhoneNumber(String openid, String encryptedData, String iv) {
        // 1. 从Redis获取sessionKey
        String sessionKey = CacheUtil.getSessionKeyByOpenid(openid);
        if (sessionKey == null) {
            throw new BusinessException(ErrorEnum.WE_CHAT_LOGIN_OVERTIME);
        }

        // 2. 调用工具类解密数据
        String decryptedData = wechatLoginUtil.decryptData(encryptedData, sessionKey, iv);

        // 3. 解析手机号并更新到用户信息
        JSONObject data = JSONObject.parseObject(decryptedData);
        String phoneNumber = data.getString("phoneNumber");

        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            UserEntity userEntity = userRepository.findByOpenid(openid);
            userEntity.setPhoneNum(phoneNumber);
            userRepository.save(userEntity);
        }

        return phoneNumber;
    }
}

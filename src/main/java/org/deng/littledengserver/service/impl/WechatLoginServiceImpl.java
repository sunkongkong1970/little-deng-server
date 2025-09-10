package org.deng.littledengserver.service.impl;

import com.alibaba.fastjson2.JSONObject;
import javax.annotation.Resource;
import org.deng.littledengserver.config.BusinessException;
import org.deng.littledengserver.constant.ErrorEnum;
import org.deng.littledengserver.model.dto.wechat.WeChatLoginResponse;
import org.deng.littledengserver.model.dto.wechat.WechatLoginResult;
import org.deng.littledengserver.model.entity.UserEntity;
import org.deng.littledengserver.repository.UserRepository;
import org.deng.littledengserver.service.UserService;
import org.deng.littledengserver.service.WechatLoginService;
import org.deng.littledengserver.util.WeChatUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class WechatLoginServiceImpl implements WechatLoginService {
    @Resource
    private WeChatUtil wechatLoginUtil;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private UserRepository userRepository;
    @Resource
    private UserService userService;

    // session_key在Redis中的过期时间（2小时）
    private static final long SESSION_KEY_EXPIRE_HOURS = 2;

    // 自定义登录态token的过期时间（7天）
    private static final long TOKEN_EXPIRE_DAYS = 7;

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

        //todo
        // 3. 将sessionKey存入Redis，设置过期时间
//        redisTemplate.opsForValue().set(
//                "wechat:session_key:" + openid,
//                sessionKey,
//                SESSION_KEY_EXPIRE_HOURS,
//                TimeUnit.HOURS
//        );

        // 4. 检查用户是否已存在，不存在则创建新用户
        UserEntity user = userService.getByCode(openid);
        if (user == null) {
            user = new UserEntity();
            user.setOpenid(openid);
            user.setUnionid(loginResult.getUnionid());
            userRepository.save(user);
        }

        // 5. 生成自定义登录态token
        String token = generateToken(openid);
        //todo
        // 6. 将token与openid关联存储
//        redisTemplate.opsForValue().set(
//                "wechat:token:" + token,
//                openid,
//                TOKEN_EXPIRE_DAYS,
//                TimeUnit.DAYS
//        );

        // 7. 返回登录结果（不包含sessionKey）
        return new WeChatLoginResponse(token, openid, user.getId());
    }

    /**
     * 解密用户手机号
     */
    public String getPhoneNumber(String openid, String encryptedData, String iv) {
        // 1. 从Redis获取sessionKey
        String sessionKey = (String) redisTemplate.opsForValue().get("wechat:session_key:" + openid);
        if (sessionKey == null) {
            throw new BusinessException(ErrorEnum.WE_CHAT_LOGIN_OVERTIME);
        }

        // 2. 调用工具类解密数据
        String decryptedData = wechatLoginUtil.decryptData(encryptedData, sessionKey, iv);

        // 3. 解析手机号并更新到用户信息
        JSONObject data = JSONObject.parseObject(decryptedData);
        String phoneNumber = data.getString("phoneNumber");

        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            //todo
//            userMapper.updatePhoneByOpenid(openid, phoneNumber);
        }

        return phoneNumber;
    }

    /**
     * 生成自定义登录态token
     */
    private String generateToken(String openid) {
        // 使用UUID和openid生成唯一token
        return UUID.randomUUID().toString().replaceAll("-", "")
                + "_" + openid.substring(0, 8);
    }
}

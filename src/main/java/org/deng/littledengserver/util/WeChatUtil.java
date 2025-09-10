package org.deng.littledengserver.util;

import com.alibaba.fastjson2.JSONObject;
import javax.annotation.Resource;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.deng.littledengserver.config.BusinessException;
import org.deng.littledengserver.constant.ErrorEnum;
import org.deng.littledengserver.model.dto.wechat.WechatLoginResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class WeChatUtil {
    private static final Logger log = LoggerFactory.getLogger(WeChatUtil.class);
    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.secret}")
    private String secret;

    @Resource
    private RestTemplate restTemplate;

    private static final String ERROR_CODE = "errcode";
    private static final String ERROR_MSG = "errmsg";
    private static final String OPEN_ID = "openid";
    private static final String SESSION_KEY = "session_key";
    private static final String UNION_ID = "unionid";


    // 微信code2session接口地址
    private static final String CODE2SESSION_URL =
            "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    /**
     * 通过code获取openid和session_key
     * @param code 小程序端获取的code
     * @return 包含openid、session_key等信息的Map，失败时包含错误信息
     */
    public WechatLoginResult getOpenidAndSessionKey(String code) {
        Map<String, String> result = new HashMap<>();

        // 参数校验
        if (code == null || code.trim().isEmpty()) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR);
        }

        try {
            // 构建请求URL
            String url = String.format(CODE2SESSION_URL, appid, secret, code);

            // 调用微信接口
            String response = restTemplate.getForObject(url, String.class);

            // 解析JSON响应
            JSONObject jsonObject = JSONObject.parseObject(response);

            if (jsonObject == null){
                throw new BusinessException(ErrorEnum.JS_CODE_2_SESSION_ERROR);
            }

            // 检查是否有错误
            if (jsonObject.containsKey(ERROR_CODE) && jsonObject.getInteger(ERROR_CODE) != 0) {
                result.put(ERROR_MSG, jsonObject.getString(ERROR_MSG));
                result.put(ERROR_CODE, jsonObject.getString(ERROR_CODE));
                throw new BusinessException(ErrorEnum.JS_CODE_2_SESSION_RETURN_ERROR.getCode(),JSONObject.toJSONString(result));
            }

            return JSONObject.parseObject(response, WechatLoginResult.class);
        } catch (Exception e) {
            throw new BusinessException(ErrorEnum.JS_CODE_2_SESSION_ANALYSIS_ERROR.getCode(),ErrorEnum.JS_CODE_2_SESSION_ANALYSIS_ERROR.getMessage()+":"+e.getMessage());
        }
    }

    /**
     * 解密用户敏感数据
     */
    public String decryptData(String encryptedData, String sessionKey, String iv) {
        try {
            // 初始化加密算法
            Security.addProvider(new BouncyCastleProvider());

            // Base64解码
            byte[] sessionKeyBytes = Base64.getDecoder().decode(sessionKey);
            byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData);
            byte[] ivBytes = Base64.getDecoder().decode(iv);

            // 初始化加密参数
            SecretKeySpec keySpec = new SecretKeySpec(sessionKeyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
            params.init(new IvParameterSpec(ivBytes));

            // 解密
            cipher.init(Cipher.DECRYPT_MODE, keySpec, params);
            byte[] result = cipher.doFinal(encryptedDataBytes);

            return new String(result, "UTF-8");
        } catch (Exception e) {
            log.info("数据解密失败: {}", e.getMessage());
            throw new RuntimeException("数据解密失败: " + e.getMessage(), e);
        }
    }

}

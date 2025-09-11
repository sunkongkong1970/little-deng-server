package org.deng.littledengserver.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class TokenCacheUtil {
    // 初始化Caffeine缓存，设置2小时过期
    private static final Cache<String, String> tokenCache = Caffeine.newBuilder()
            .maximumSize(10000)  // 最大缓存数量
            .expireAfterWrite(7, TimeUnit.DAYS)  // 2小时过期
            .build();

    private static final Cache<String, String> sessionCache = Caffeine.newBuilder()
            .maximumSize(10000)  // 最大缓存数量
            .expireAfterWrite(2, TimeUnit.HOURS)  // 2小时过期
            .build();

    // 生成token并存储到缓存
    public static String generateToken(String openid,String sessionKey) {
        // 生成UUID作为token
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        // 存储token与openid的映射
        if (!openid.isEmpty()){
            tokenCache.put(token, openid);
        }
        if (!sessionKey.isEmpty()){
            sessionCache.put(token, sessionKey);
        }
        return token;
    }

    // 根据token获取openid
    public static String getOpenidByToken(String token) {
        if (token == null) {
            return null;
        }
        return tokenCache.getIfPresent(token);
    }

    // 根据openid获取sessionKey
    public static String getSessionKeyByOpenid(String openid) {
        if (openid == null) {
            return null;
        }
        return sessionCache.getIfPresent(openid);
    }

    // 移除token（用户退出登录）
    public static void removeToken(String token) {
        if (token != null) {
            tokenCache.invalidate(token);
        }
    }
}

package org.deng.littledengserver.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class CacheUtil {
    // 初始化Caffeine缓存，设置2小时过期
    private static final Cache<String, String> tokenCache = Caffeine.newBuilder()
            .maximumSize(10000)  // 最大缓存数量
            .expireAfterWrite(7, TimeUnit.DAYS)  // 7天过期
            .build();

    private static final Cache<String, String> sessionCache = Caffeine.newBuilder()
            .maximumSize(10000)  // 最大缓存数量
            .expireAfterWrite(2, TimeUnit.HOURS)  // 2小时过期
            .build();

    private static final Cache<String, String> homeCodeCache = Caffeine.newBuilder()
            .maximumSize(10000)  // 最大缓存数量
            .expireAfterWrite(7, TimeUnit.DAYS)  // 7天过期
            .build();

    private static final Cache<String, Long> homeIdCache = Caffeine.newBuilder()
            .maximumSize(10000)  // 最大缓存数量
            .expireAfterWrite(7, TimeUnit.DAYS)  // 7天过期
            .build();

    public static String generateHomeCode(String token) {
        String homeCode = UUID.randomUUID().toString().split("-")[0];
        homeCodeCache.put(token, homeCode);

        return homeCode;
    }

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

    public static Long getHomeId(String homeCode) {
        return homeIdCache.getIfPresent(homeCode);
    }

    /**
     * 获取token并自动续期
     * 实现思路：先获取token，若存在则重新存入以刷新过期时间
     */
    public static String getTokenAndRenew(String token) {
        // 获取当前token
        String openid = tokenCache.getIfPresent(token);

        // 如果token存在，则重新存入以刷新过期时间（续期）
        if (openid != null) {
            tokenCache.put(token, openid);
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

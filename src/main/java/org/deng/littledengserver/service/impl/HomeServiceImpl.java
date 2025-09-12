package org.deng.littledengserver.service.impl;

import org.deng.littledengserver.config.BusinessException;
import org.deng.littledengserver.constant.ErrorEnum;
import org.deng.littledengserver.model.entity.UserEntity;
import org.deng.littledengserver.service.HomeService;
import org.deng.littledengserver.service.UserService;
import org.deng.littledengserver.util.CacheUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HomeServiceImpl implements HomeService {
    @Resource
    UserService userService;

    @Override
    public String generateHomeCode(String token) {
        UserEntity user = userService.getByToken(token);

        if (!user.getIsHouseholder()){
            throw new BusinessException(ErrorEnum.USER_PERMISSION_DENIED);
        }

        return CacheUtil.generateHomeCode(token, user.getHomeId());
    }
}

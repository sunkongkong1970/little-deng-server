package org.deng.littledengserver.service.impl;

import org.deng.littledengserver.config.BusinessException;
import org.deng.littledengserver.constant.ErrorEnum;
import org.deng.littledengserver.model.dto.CreateHomeVo;
import org.deng.littledengserver.model.entity.HomeEntity;
import org.deng.littledengserver.model.entity.UserEntity;
import org.deng.littledengserver.repository.HomeRepository;
import org.deng.littledengserver.repository.UserRepository;
import org.deng.littledengserver.service.HomeService;
import org.deng.littledengserver.service.UserService;
import org.deng.littledengserver.util.CacheUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HomeServiceImpl implements HomeService {
    @Resource
    UserService userService;
    @Resource
    HomeRepository homeRepository;
    @Resource
    UserRepository userRepository;

    @Override
    public String generateHomeCode(String token) {
        UserEntity user = userService.getByToken(token);

        if (!user.getIsHouseholder()){
            throw new BusinessException(ErrorEnum.USER_PERMISSION_DENIED);
        }

        return CacheUtil.generateHomeCode(token);
    }

    @Override
    public Long createHome(CreateHomeVo createHomeVo) {
        UserEntity user = userService.getByToken(createHomeVo.getToken());
        HomeEntity homeEntity = new HomeEntity();
        homeEntity.setHomeName(createHomeVo.getHomeName());
        homeEntity.setHouseholderUserId(user.getId());

        Long homeId = homeRepository.save(homeEntity).getId();

        user.setHomeId(homeId);
        user.setIsHouseholder(true);
        user.setUserName(createHomeVo.getUserName());
        user.setUserRole(createHomeVo.getUserRole());
        user.setUserAvatarBase64(createHomeVo.getAvatarBase64());
        userRepository.save(user);

        return homeId;
    }
}

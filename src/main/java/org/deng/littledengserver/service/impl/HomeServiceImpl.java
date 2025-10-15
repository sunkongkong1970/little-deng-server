package org.deng.littledengserver.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.deng.littledengserver.config.BusinessException;
import org.deng.littledengserver.constant.ErrorEnum;
import org.deng.littledengserver.model.entity.HomeEntity;
import org.deng.littledengserver.model.entity.UserEntity;
import org.deng.littledengserver.model.vo.CreateHomeVo;
import org.deng.littledengserver.repository.HomeRepository;
import org.deng.littledengserver.repository.UserRepository;
import org.deng.littledengserver.service.HomeService;
import org.deng.littledengserver.service.UserService;
import org.deng.littledengserver.util.CacheUtil;
import org.springframework.beans.BeanUtils;
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
    public String getHomeCode(String token) {
        UserEntity user = userService.getByToken(token);

        if (!user.getIsHouseholder()){
            throw new BusinessException(ErrorEnum.USER_PERMISSION_DENIED);
        }

        String code = CacheUtil.getUserHomeCode(token);
        if (StringUtils.isNotBlank(code)) {
            return code;
        }

        return CacheUtil.generateHomeCode(user.getHomeId(), token);
    }

    @Override
    public Long createHome(CreateHomeVo createHomeVo) {
        UserEntity user = userService.getByToken(createHomeVo.getToken());
        HomeEntity homeEntity = new HomeEntity();
        homeEntity.setHomeName(createHomeVo.getHomeName());
        homeEntity.setHouseholderUserId(user.getId());

        Long homeId = homeRepository.save(homeEntity).getId();

        BeanUtils.copyProperties(createHomeVo, user);
        user.setHomeId(homeId);
        user.setIsHouseholder(true);

        userRepository.save(user);

        return homeId;
    }
}

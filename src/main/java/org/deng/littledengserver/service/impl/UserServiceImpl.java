package org.deng.littledengserver.service.impl;

import org.deng.littledengserver.config.BusinessException;
import org.deng.littledengserver.constant.ErrorEnum;
import org.deng.littledengserver.model.dto.UserDto;
import org.deng.littledengserver.model.dto.UserJoinHomeDto;
import org.deng.littledengserver.model.entity.UserEntity;
import org.deng.littledengserver.repository.UserRepository;
import org.deng.littledengserver.service.UserService;
import org.deng.littledengserver.util.CacheUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserEntity> queryUsers(UserDto query) {
        if (query == null) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR);
        }
        
        UserEntity probe = new UserEntity();
        BeanUtils.copyProperties(query, probe);

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        Example<UserEntity> example = Example.of(probe, matcher);
        return userRepository.findAll(example);
    }

    @Override
    public UserEntity getByCode(String code) {
        if (code == null || code.isEmpty()) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR);
        }

        return userRepository.findByOpenid(code);
    }

    @Override
    public UserDto getUserDtoByToken(String token) {
        UserEntity user  = getByToken(token);
        UserDto userInfoDto = new UserDto();
        BeanUtils.copyProperties(user, userInfoDto);

        return userInfoDto;
    }

    @Override
    public UserEntity getByToken(String token){
        if (token == null || token.isEmpty()) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR);
        }

        String openId = CacheUtil.getOpenidByToken(token);
        if (openId == null || openId.isEmpty()) {
            throw new BusinessException(ErrorEnum.WE_CHAT_LOGIN_OVERTIME);
        }

        return userRepository.findByOpenid(openId);
    }

    @Override
    public String userJoinHome(UserJoinHomeDto userJoinHomeDto) {
        if (CacheUtil.getOpenidByToken(userJoinHomeDto.getToken()) == null) {
            throw new BusinessException(ErrorEnum.WE_CHAT_LOGIN_OVERTIME);
        }

        Long homeId = CacheUtil.getHomeId(userJoinHomeDto.getHomeCode());
        if (homeId == null) {
            throw new BusinessException(ErrorEnum.HOME_CODE_OVERTIME);
        }

        UserEntity user = userRepository.findByOpenid(CacheUtil.getOpenidByToken(userJoinHomeDto.getToken()));

        if (user == null) {
            throw new BusinessException(ErrorEnum.USER_NOT_EXIST);
        }

        BeanUtils.copyProperties(userJoinHomeDto, user);

        user.setHomeId(homeId);
        user.setUserAvatarBase64(userJoinHomeDto.getAvatarBase64());
        userRepository.save(user);

        return CacheUtil.getTokenAndRenew(userJoinHomeDto.getToken());
    }
}

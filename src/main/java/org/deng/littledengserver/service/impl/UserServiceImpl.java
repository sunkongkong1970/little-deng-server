package org.deng.littledengserver.service.impl;

import org.deng.littledengserver.config.BusinessException;
import org.deng.littledengserver.constant.ErrorEnum;
import org.deng.littledengserver.model.entity.UserEntity;
import org.deng.littledengserver.model.vo.UserEditVo;
import org.deng.littledengserver.model.vo.UserJoinHomeVo;
import org.deng.littledengserver.model.vo.UserVo;
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
    public List<UserEntity> queryUsers(UserVo query) {
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
    public UserVo getUserDtoByToken(String token) {
        UserEntity user  = getByToken(token);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);

        return userVo;
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
    public String userJoinHome(UserJoinHomeVo userJoinHomeVo) {
        if (CacheUtil.getOpenidByToken(userJoinHomeVo.getToken()) == null) {
            throw new BusinessException(ErrorEnum.WE_CHAT_LOGIN_OVERTIME);
        }

        Long homeId = CacheUtil.getHomeId(userJoinHomeVo.getHomeCode());
        if (homeId == null) {
            throw new BusinessException(ErrorEnum.HOME_CODE_OVERTIME);
        }

        UserEntity user = userRepository.findByOpenid(CacheUtil.getOpenidByToken(userJoinHomeVo.getToken()));

        if (user == null) {
            throw new BusinessException(ErrorEnum.USER_NOT_EXIST);
        }

        BeanUtils.copyProperties(userJoinHomeVo, user);

        user.setHomeId(homeId);
        user.setUserAvatarBase64(userJoinHomeVo.getAvatarBase64());
        userRepository.save(user);

        return CacheUtil.getTokenAndRenew(userJoinHomeVo.getToken());
    }

    @Override
    public String getUserAvatar(String token) {
        if (token == null || token.isEmpty()) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR);
        }
        UserEntity user = getByToken(token);

        return user.getUserAvatarBase64();
    }

    @Override
    public String editUser(UserEditVo userEditVo) {
        if (userEditVo.getToken() == null || userEditVo.getToken().isEmpty()) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR);
        }

        UserEntity user = getByToken(userEditVo.getToken());

        BeanUtils.copyProperties(userEditVo, user);

        userRepository.save(user);

        return CacheUtil.getTokenAndRenew(userEditVo.getToken());
    }
}

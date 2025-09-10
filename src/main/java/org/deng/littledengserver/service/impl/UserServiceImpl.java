package org.deng.littledengserver.service.impl;

import org.deng.littledengserver.config.BusinessException;
import org.deng.littledengserver.constant.ErrorEnum;
import org.deng.littledengserver.model.dto.UserDto;
import org.deng.littledengserver.model.entity.UserEntity;
import org.deng.littledengserver.repository.UserRepository;
import org.deng.littledengserver.service.UserService;
import org.deng.littledengserver.util.WeChatUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Long createUser(UserDto user) {
        if (user == null) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR);
        }
        UserEntity entity = new UserEntity();
        BeanUtils.copyProperties(user, entity);
        UserEntity saved = userRepository.save(entity);
        return saved.getId();
    }

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
}

package org.deng.littledengserver.service;

import org.deng.littledengserver.model.dto.UserDto;
import org.deng.littledengserver.model.dto.UserUpdateDto;
import org.deng.littledengserver.model.entity.UserEntity;

import java.util.List;

public interface UserService {
    List<UserEntity> queryUsers(UserDto query);

    UserEntity getByCode(String openid);

    UserDto getUserDtoByToken(String token);

    UserEntity getByToken(String token);

    String updateUser(UserUpdateDto userUpdateDto);
}

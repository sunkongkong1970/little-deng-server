package org.deng.littledengserver.service;

import org.deng.littledengserver.model.dto.UserDto;
import org.deng.littledengserver.model.entity.UserEntity;

public interface UserService {

    Long createUser(UserDto user);

    java.util.List<UserEntity> queryUsers(UserDto query);

    java.util.Optional<UserEntity> getByCode(String openid);

}

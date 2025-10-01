package org.deng.littledengserver.service;

import org.deng.littledengserver.model.dto.UserJoinHomeVo;
import org.deng.littledengserver.model.dto.UserVo;
import org.deng.littledengserver.model.entity.UserEntity;

import java.util.List;

public interface UserService {
    List<UserEntity> queryUsers(UserVo query);

    UserEntity getByCode(String openid);

    UserVo getUserDtoByToken(String token);

    UserEntity getByToken(String token);

    String userJoinHome(UserJoinHomeVo userJoinHomeVo);
}

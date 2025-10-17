package org.deng.littledengserver.service;

import org.deng.littledengserver.model.entity.UserEntity;
import org.deng.littledengserver.model.vo.AvatarVo;
import org.deng.littledengserver.model.vo.UserEditVo;
import org.deng.littledengserver.model.vo.UserJoinHomeVo;
import org.deng.littledengserver.model.vo.UserVo;

import java.util.List;

public interface UserService {
    List<UserEntity> queryUsers(UserVo query);

    UserEntity getByCode(String openid);

    UserVo getUserDtoByToken(String token);

    UserEntity getByToken(String token);

    String userJoinHome(UserJoinHomeVo userJoinHomeVo);

    AvatarVo getUserAvatar(String token);

    String editUser(UserEditVo userEditVo);
}

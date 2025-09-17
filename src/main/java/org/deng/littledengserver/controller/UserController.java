package org.deng.littledengserver.controller;

import org.deng.littledengserver.config.BaseResult;
import org.deng.littledengserver.model.dto.UserDto;
import org.deng.littledengserver.model.dto.UserJoinHomeDto;
import org.deng.littledengserver.model.entity.UserEntity;
import org.deng.littledengserver.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/query")
    public BaseResult<List<UserEntity>> queryUsers(@RequestBody UserDto userDto) {
        return BaseResult.success(userService.queryUsers(userDto));
    }

    @PostMapping("/token")
    public BaseResult<UserDto> getByToken(@RequestParam("token") String token) {
        return BaseResult.success(userService.getUserDtoByToken(token));
    }

    @PostMapping("/joinHome")
    public BaseResult<String> updateUser(@RequestBody UserJoinHomeDto userJoinHomeDto) {
        return BaseResult.success(userService.userJoinHome(userJoinHomeDto));
    }

}

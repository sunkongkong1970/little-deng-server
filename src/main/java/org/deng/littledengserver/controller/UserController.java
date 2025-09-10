package org.deng.littledengserver.controller;

import org.deng.littledengserver.config.BaseResult;
import org.deng.littledengserver.model.dto.UserDto;
import org.deng.littledengserver.model.entity.UserEntity;
import org.deng.littledengserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/query")
    public BaseResult<List<UserEntity>> queryUsers(@RequestBody UserDto userDto) {
        return BaseResult.success(userService.queryUsers(userDto));
    }

    @PostMapping("/code")
    public BaseResult<UserEntity> getByOpenid(@RequestParam("code") String code) {
        return BaseResult.success(userService.getByCode(code));
    }

    @PostMapping
    public BaseResult<Long> createUser(@Validated @RequestBody UserDto userDto) {
        return BaseResult.success(userService.createUser(userDto));
    }
}

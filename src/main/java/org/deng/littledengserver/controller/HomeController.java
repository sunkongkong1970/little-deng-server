package org.deng.littledengserver.controller;

import org.deng.littledengserver.config.BaseResult;
import org.deng.littledengserver.service.HomeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    @Resource
    HomeService homeService;

    @PostMapping("/generateCode")
    public BaseResult<String> generateHomeCode(@RequestParam("token") String token){
        return BaseResult.success(homeService.generateHomeCode(token));
    }
}

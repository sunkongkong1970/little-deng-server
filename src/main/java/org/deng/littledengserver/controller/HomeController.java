package org.deng.littledengserver.controller;

import org.deng.littledengserver.config.BaseResult;
import org.deng.littledengserver.model.dto.CreateHomeVo;
import org.deng.littledengserver.service.HomeService;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/createHome")
    public BaseResult<Long> createHome(@RequestBody CreateHomeVo createHomeVo) {
        return BaseResult.success(homeService.createHome(createHomeVo));
    }
}

package org.deng.littledengserver.controller;

import org.deng.littledengserver.config.BaseResult;
import org.deng.littledengserver.constant.GlobalConstant;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dict")
public class DictController {

    @GetMapping("/role")
    public BaseResult<Map<String,String>> getRole(){
        return BaseResult.success(GlobalConstant.roleMap);
    }
}

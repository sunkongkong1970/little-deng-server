package org.deng.littledengserver.controller;

import org.deng.littledengserver.config.BaseResult;
import org.deng.littledengserver.service.DictService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/dict")
public class DictController {

    @Resource
    DictService dictService;

    @GetMapping("/dictList")
    public BaseResult<Map<String,String>> getDict(@RequestParam("type") String type){
        return BaseResult.success(dictService.getDictList(type));
    }

    @GetMapping("/dictValue")
    public BaseResult<String> getDict(@RequestParam("type") String type,@RequestParam("key") String key){
        return BaseResult.success(dictService.getDict(type,key));
    }
}

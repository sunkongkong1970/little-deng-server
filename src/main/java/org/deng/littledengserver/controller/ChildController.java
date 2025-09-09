package org.deng.littledengserver.controller;

import org.deng.littledengserver.config.BaseResult;
import org.deng.littledengserver.model.entity.ChildEntity;
import org.deng.littledengserver.service.ChildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/children")
public class ChildController {
    @Autowired
    private ChildService childService;

    @GetMapping
    public BaseResult<List<ChildEntity>> listByHomeId(@RequestParam("homeId") Long homeId) {
        return BaseResult.success(childService.listByHomeId(homeId));
    }

    //todo 需修改入参，建立dto 增加校验
    @PostMapping
    public BaseResult<Long> createChild(@RequestBody ChildEntity child) {
        return BaseResult.success(childService.createChild(child));
    }
}

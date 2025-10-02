package org.deng.littledengserver.controller;

import org.deng.littledengserver.config.BaseResult;
import org.deng.littledengserver.model.entity.ChildEntity;
import org.deng.littledengserver.model.vo.AddBabyVo;
import org.deng.littledengserver.service.ChildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/child")
public class ChildController {
    @Autowired
    private ChildService childService;

    @GetMapping("/getList")
    public BaseResult<List<ChildEntity>> listByHomeId(@RequestParam("homeId") Long homeId) {
        return BaseResult.success(childService.listByHomeId(homeId));
    }

    @GetMapping("/getById")
    public BaseResult<ChildEntity> getById(@RequestParam("id") Long id) {
        return BaseResult.success(childService.getById(id));
    }

    @PostMapping("/edit")
    public BaseResult<Long> createChild(@RequestBody AddBabyVo addBabyVo) {
        return BaseResult.success(childService.edit(addBabyVo));
    }
}

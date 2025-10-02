package org.deng.littledengserver.controller;

import org.deng.littledengserver.config.BaseResult;
import org.deng.littledengserver.model.vo.CreatePhotoWallVo;
import org.deng.littledengserver.service.PhotoWallService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/photo-wall")
public class PhotoWallController {

    @Resource
    private PhotoWallService photoWallService;

    /**
     * 创建照片墙
     * @param createPhotoWallVo 创建照片墙的请求数据
     * @return 创建的照片墙ID
     */
    @PostMapping("/create")
    public BaseResult<Long> createPhotoWall(@RequestBody CreatePhotoWallVo createPhotoWallVo) {
        Long photoWallId = photoWallService.createPhotoWall(createPhotoWallVo);
        return BaseResult.success(photoWallId);
    }

    /**
     * 删除照片墙
     * @param token 用户token
     * @param photoWallId 要删除的照片墙ID
     * @return 删除是否成功
     */
    @PostMapping("/delete")
    public BaseResult<Boolean> deletePhotoWall(
            @RequestParam("token") String token,
            @RequestParam("photoWallId") Long photoWallId) {
        boolean result = photoWallService.deletePhotoWall(token, photoWallId);
        return BaseResult.success(result);
    }
}
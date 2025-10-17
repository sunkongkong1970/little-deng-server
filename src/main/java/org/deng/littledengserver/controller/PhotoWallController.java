package org.deng.littledengserver.controller;

import org.deng.littledengserver.config.BaseResult;
import org.deng.littledengserver.model.vo.CreatePhotoWallVo;
import org.deng.littledengserver.model.vo.PhotoWallPageVo;
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

    /**
     * 获取照片墙列表（分页）
     * @param token 用户token
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页大小
     * @return 照片墙分页数据
     */
    @GetMapping("/list")
    public BaseResult<PhotoWallPageVo> getPhotoWallList(
            @RequestParam("token") String token,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        PhotoWallPageVo result = photoWallService.getPhotoWallList(token, pageNum, pageSize);
        return BaseResult.success(result);
    }

    /**
     * 点赞或取消点赞
     * @param token 用户token
     * @param photoWallId 照片墙ID
     * @return 点赞后的状态（true-已点赞，false-已取消）
     */
    @PostMapping("/like")
    public BaseResult<Boolean> toggleLike(
            @RequestParam("token") String token,
            @RequestParam("photoWallId") Long photoWallId) {
        Boolean liked = photoWallService.toggleLike(token, photoWallId);
        return BaseResult.success(liked);
    }
}
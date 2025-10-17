package org.deng.littledengserver.service;

import org.deng.littledengserver.model.vo.CreatePhotoWallVo;
import org.deng.littledengserver.model.vo.PhotoWallPageVo;

public interface PhotoWallService {
    /**
     * 创建PhotoWall并同时插入相关的图片数据
     * @param createPhotoWallVo 创建PhotoWall的请求数据
     * @return 创建的PhotoWall的ID
     */
    Long createPhotoWall(CreatePhotoWallVo createPhotoWallVo);

    /**
     * 删除PhotoWall及其相关的图片数据
     * @param token 用户token
     * @param photoWallId 要删除的PhotoWall的ID
     * @return 删除是否成功
     */
    boolean deletePhotoWall(String token, Long photoWallId);

    /**
     * 分页获取照片墙列表
     * @param token 用户token
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页大小
     * @return 照片墙分页数据
     */
    PhotoWallPageVo getPhotoWallList(String token, Integer pageNum, Integer pageSize);

    /**
     * 点赞或取消点赞
     * @param token 用户token
     * @param photoWallId 照片墙ID
     * @return 点赞后的状态（true-已点赞，false-已取消）
     */
    Boolean toggleLike(String token, Long photoWallId);
}
package org.deng.littledengserver.service;

import org.deng.littledengserver.model.vo.CreatePhotoWallVo;

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
}
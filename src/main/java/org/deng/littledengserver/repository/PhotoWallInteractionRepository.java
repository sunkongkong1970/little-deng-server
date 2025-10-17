package org.deng.littledengserver.repository;

import org.deng.littledengserver.model.entity.PhotoWallInteractionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoWallInteractionRepository extends JpaRepository<PhotoWallInteractionEntity, Long> {
    /**
     * 根据照片墙ID和互动类型查询互动列表
     * @param photoWallId 照片墙ID
     * @param interactionType 互动类型（1-点赞，2-评论）
     * @param isDeleted 是否已删除
     * @return 互动列表
     */
    List<PhotoWallInteractionEntity> findByPhotoWallIdAndInteractionTypeAndIsDeleted(
            Long photoWallId, Integer interactionType, Boolean isDeleted);

    /**
     * 根据照片墙ID、用户ID和互动类型查询互动记录
     * @param photoWallId 照片墙ID
     * @param userId 用户ID
     * @param interactionType 互动类型
     * @param isDeleted 是否已删除
     * @return 互动记录
     */
    PhotoWallInteractionEntity findByPhotoWallIdAndUserIdAndInteractionTypeAndIsDeleted(
            Long photoWallId, Long userId, Integer interactionType, Boolean isDeleted);

    /**
     * 根据多个照片墙ID查询点赞信息
     * @param photoWallIds 照片墙ID列表
     * @param interactionType 互动类型
     * @param isDeleted 是否已删除
     * @return 互动列表
     */
    List<PhotoWallInteractionEntity> findByPhotoWallIdInAndInteractionTypeAndIsDeleted(
            List<Long> photoWallIds, Integer interactionType, Boolean isDeleted);
}


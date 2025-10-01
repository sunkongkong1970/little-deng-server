package org.deng.littledengserver.repository;

import org.deng.littledengserver.model.entity.photoWallDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoWallDataRepository extends JpaRepository<photoWallDataEntity, Long> {
    // 根据photoWallId查询所有相关的图片数据
    List<photoWallDataEntity> findByPhotoWallId(Long photoWallId);

    // 根据photoWallId删除所有相关的图片数据
    void deleteByPhotoWallId(Long photoWallId);
}
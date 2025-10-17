package org.deng.littledengserver.repository;

import org.deng.littledengserver.model.entity.PhotoWallEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoWallRepository extends JpaRepository<PhotoWallEntity, Long> {
    /**
     * 根据homeId分页查询照片墙列表（未删除的）
     * @param homeId 家庭ID
     * @param isDeleted 是否已删除
     * @param pageable 分页参数
     * @return 照片墙列表
     */
    Page<PhotoWallEntity> findByHomeIdAndIsDeletedOrderByPostTimeDesc(Long homeId, Boolean isDeleted, Pageable pageable);
}
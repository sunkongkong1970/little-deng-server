package org.deng.littledengserver.repository;

import org.deng.littledengserver.model.entity.PhotoWallEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoWallRepository extends JpaRepository<PhotoWallEntity, Long> {
    // 可以根据需要添加其他查询方法
}
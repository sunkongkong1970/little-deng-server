package org.deng.littledengserver.repository;

import org.deng.littledengserver.model.entity.ChildEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildRepository extends JpaRepository<ChildEntity, Long> {
    List<ChildEntity> findByHomeId(Long homeId);
}




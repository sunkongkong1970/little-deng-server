package org.deng.littledengserver.repository;

import org.deng.littledengserver.model.entity.HomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeRepository extends JpaRepository<HomeEntity, Long> {
}

package org.deng.littledengserver.repository;

import org.deng.littledengserver.model.entity.SysDictEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DictRepository extends JpaRepository<SysDictEntity, Long> {

}

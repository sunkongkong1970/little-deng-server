package org.deng.littledengserver.service;

import org.deng.littledengserver.model.dto.AddBabyVo;
import org.deng.littledengserver.model.entity.ChildEntity;

import java.util.List;

public interface ChildService {
    List<ChildEntity> listByHomeId(Long homeId);

    Long edit(AddBabyVo addBabyVo);

    ChildEntity getById(Long id);
}




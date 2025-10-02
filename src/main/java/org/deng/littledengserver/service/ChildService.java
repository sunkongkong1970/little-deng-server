package org.deng.littledengserver.service;

import org.deng.littledengserver.model.entity.ChildEntity;
import org.deng.littledengserver.model.vo.AddBabyVo;

import java.util.List;

public interface ChildService {
    List<ChildEntity> listByHomeId(Long homeId);

    Long edit(AddBabyVo addBabyVo);

    ChildEntity getById(Long id);
}




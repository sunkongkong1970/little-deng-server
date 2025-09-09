package org.deng.littledengserver.service;

import org.deng.littledengserver.model.entity.ChildEntity;

import java.util.List;

public interface ChildService {
    List<ChildEntity> listByHomeId(Long homeId);

    Long createChild(ChildEntity child);
}




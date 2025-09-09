package org.deng.littledengserver.service.impl;

import org.deng.littledengserver.constant.ErrorEnum;
import org.deng.littledengserver.config.BusinessException;
import org.deng.littledengserver.model.entity.ChildEntity;
import org.deng.littledengserver.repository.ChildRepository;
import org.deng.littledengserver.service.ChildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChildServiceImpl implements ChildService {

    @Autowired
    private ChildRepository childRepository;

    @Override
    public List<ChildEntity> listByHomeId(Long homeId) {
        if (homeId == null) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR);
        }
        return childRepository.findByHomeId(homeId);
    }

    @Override
    public Long createChild(ChildEntity child) {
        if (child == null || child.getHomeId() == null) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR);
        }
        ChildEntity saved = childRepository.save(child);
        return saved.getId();
    }
}




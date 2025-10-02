package org.deng.littledengserver.service.impl;

import org.deng.littledengserver.config.BusinessException;
import org.deng.littledengserver.constant.ErrorEnum;
import org.deng.littledengserver.model.entity.ChildEntity;
import org.deng.littledengserver.model.entity.UserEntity;
import org.deng.littledengserver.model.vo.AddBabyVo;
import org.deng.littledengserver.repository.ChildRepository;
import org.deng.littledengserver.service.ChildService;
import org.deng.littledengserver.service.UserService;
import org.deng.littledengserver.util.BirthDateCalculator;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ChildServiceImpl implements ChildService {

    @Resource
    private ChildRepository childRepository;
    @Resource
    private UserService userService;

    @Override
    public List<ChildEntity> listByHomeId(Long homeId) {
        if (homeId == null) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR);
        }
        return childRepository.findByHomeId(homeId);
    }

    @Override
    public Long edit(AddBabyVo addBabyVo) {
        if (addBabyVo == null || addBabyVo.getBabyInfo() == null) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR);
        }
        // 验证token
        UserEntity user = userService.getByToken(addBabyVo.getToken());
        if (user == null) {
            throw new BusinessException(ErrorEnum.USER_NOT_EXIST);
        }

        ChildEntity saved;
        if (addBabyVo.getBabyInfo().getId() == null) {
            saved = new ChildEntity();
        } else {
            saved = childRepository.getById(addBabyVo.getBabyInfo().getId());
        }

        BeanUtils.copyProperties(addBabyVo.getBabyInfo(), saved);
        saved.setHomeId(user.getHomeId());
        saved.setChildCoverCroppedImg(addBabyVo.getBabyInfo().getAvatarCropped());
        saved.setChildCoverImg(addBabyVo.getBabyInfo().getAvatarOriginal());
        saved.setChildChineseZodiac(BirthDateCalculator.getChineseZodiac(addBabyVo.getBabyInfo().getChildBirthday().toLocalDate()));
        saved.setChildZodiac(BirthDateCalculator.getZodiacSign(addBabyVo.getBabyInfo().getChildBirthday().toLocalDate()));

        childRepository.save(saved);
        return saved.getId();
    }

    @Override
    public ChildEntity getById(Long id) {
        if (id == null) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR);
        }
        return childRepository.getById(id);
    }
}




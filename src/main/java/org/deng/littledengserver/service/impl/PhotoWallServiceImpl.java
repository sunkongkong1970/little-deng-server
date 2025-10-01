package org.deng.littledengserver.service.impl;

import org.deng.littledengserver.config.BusinessException;
import org.deng.littledengserver.constant.DictConstant;
import org.deng.littledengserver.constant.ErrorEnum;
import org.deng.littledengserver.model.dto.CreatePhotoWallVo;
import org.deng.littledengserver.model.entity.PhotoWallEntity;
import org.deng.littledengserver.model.entity.UserEntity;
import org.deng.littledengserver.model.entity.photoWallDataEntity;
import org.deng.littledengserver.repository.PhotoWallDataRepository;
import org.deng.littledengserver.repository.PhotoWallRepository;
import org.deng.littledengserver.service.PhotoWallService;
import org.deng.littledengserver.service.UserService;
import org.deng.littledengserver.util.DictUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PhotoWallServiceImpl implements PhotoWallService {

    @Resource
    private UserService userService;

    @Resource
    private PhotoWallRepository photoWallRepository;

    @Resource
    private PhotoWallDataRepository photoWallDataRepository;

    @Override
    @Transactional
    public Long createPhotoWall(CreatePhotoWallVo createPhotoWallVo) {
        // 验证token
        UserEntity user = userService.getByToken(createPhotoWallVo.getToken());
        if (user == null) {
            throw new BusinessException(ErrorEnum.USER_NOT_EXIST);
        }

        // 验证homeId
        Long homeId = createPhotoWallVo.getHomeId();
        if (homeId == null || !homeId.equals(user.getHomeId())) {
            throw new BusinessException(ErrorEnum.USER_PERMISSION_DENIED);
        }

        // 创建PhotoWallEntity对象
        PhotoWallEntity photoWallEntity = new PhotoWallEntity();
        photoWallEntity.setHomeId(homeId);
        photoWallEntity.setCreateUserId(user.getId());
        photoWallEntity.setCreateUserRoleName(DictUtil.getDict(DictConstant.ROLE, user.getUserRole()));
        photoWallEntity.setChildIds(createPhotoWallVo.getChildIds());
        photoWallEntity.setContent(createPhotoWallVo.getContent());
        photoWallEntity.setPostTime(createPhotoWallVo.getPostTime());
        photoWallEntity.setLocation(createPhotoWallVo.getLocation());
        photoWallEntity.setIsDeleted(false);

        // 保存PhotoWallEntity
        PhotoWallEntity savedPhotoWall = photoWallRepository.save(photoWallEntity);
        Long photoWallId = savedPhotoWall.getId();

        // 保存相关的图片数据
        List<String> imgUrls = createPhotoWallVo.getImgUrls();
        if (imgUrls != null && !imgUrls.isEmpty()) {
            List<photoWallDataEntity> photoWallDataEntities = new ArrayList<>();
            for (String imgUrl : imgUrls) {
                photoWallDataEntity photoWallData = new photoWallDataEntity();
                photoWallData.setPhotoWallId(photoWallId);
                photoWallData.setImgUrl(imgUrl);
                photoWallDataEntities.add(photoWallData);
            }
            photoWallDataRepository.saveAll(photoWallDataEntities);
        }

        return photoWallId;
    }

    @Override
    @Transactional
    public boolean deletePhotoWall(String token, Long photoWallId) {
        // 验证token
        UserEntity user = userService.getByToken(token);
        if (user == null) {
            throw new BusinessException(ErrorEnum.USER_NOT_EXIST);
        }

        // 验证权限
        PhotoWallEntity photoWall = photoWallRepository.findById(photoWallId)
                .orElseThrow(() -> new BusinessException(ErrorEnum.RESOURCE_NOT_FOUND));

        // 检查是否是创建者或管理员
        if (!photoWall.getCreateUserId().equals(user.getId())
                || !photoWall.getHomeId().equals(user.getHomeId())
                || !user.getIsHouseholder()) {
            throw new BusinessException(ErrorEnum.USER_PERMISSION_DENIED);
        }

        // 删除相关的图片数据
        photoWallDataRepository.deleteByPhotoWallId(photoWallId);

        // 删除PhotoWall
        photoWallRepository.deleteById(photoWallId);

        return true;
    }
}
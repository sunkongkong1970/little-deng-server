package org.deng.littledengserver.service.impl;

import org.deng.littledengserver.config.BusinessException;
import org.deng.littledengserver.constant.DictConstant;
import org.deng.littledengserver.constant.ErrorEnum;
import org.deng.littledengserver.model.entity.PhotoWallEntity;
import org.deng.littledengserver.model.entity.PhotoWallInteractionEntity;
import org.deng.littledengserver.model.entity.UserEntity;
import org.deng.littledengserver.model.entity.photoWallDataEntity;
import org.deng.littledengserver.model.vo.CreatePhotoWallVo;
import org.deng.littledengserver.model.vo.PhotoWallPageVo;
import org.deng.littledengserver.model.vo.PhotoWallVo;
import org.deng.littledengserver.repository.PhotoWallDataRepository;
import org.deng.littledengserver.repository.PhotoWallInteractionRepository;
import org.deng.littledengserver.repository.PhotoWallRepository;
import org.deng.littledengserver.service.PhotoWallService;
import org.deng.littledengserver.service.UserService;
import org.deng.littledengserver.util.DictUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PhotoWallServiceImpl implements PhotoWallService {

    @Resource
    private UserService userService;

    @Resource
    private PhotoWallRepository photoWallRepository;

    @Resource
    private PhotoWallDataRepository photoWallDataRepository;

    @Resource
    private PhotoWallInteractionRepository photoWallInteractionRepository;

    @Resource
    private org.deng.littledengserver.repository.UserRepository userRepository;

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

    @Override
    public PhotoWallPageVo getPhotoWallList(String token, Integer pageNum, Integer pageSize) {
        // 验证token
        UserEntity user = userService.getByToken(token);
        if (user == null) {
            throw new BusinessException(ErrorEnum.USER_NOT_EXIST);
        }

        // 设置默认值
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }

        // 创建分页参数
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        // 查询照片墙列表
        Page<PhotoWallEntity> page = photoWallRepository.findByHomeIdAndIsDeletedOrderByPostTimeDesc(
                user.getHomeId(), false, pageable);

        List<PhotoWallEntity> photoWalls = page.getContent();
        List<Long> photoWallIds = photoWalls.stream().map(PhotoWallEntity::getId).collect(Collectors.toList());

        // 获取所有创建者的用户ID
        List<Long> creatorUserIds = photoWalls.stream()
                .map(PhotoWallEntity::getCreateUserId)
                .distinct()
                .collect(Collectors.toList());

        // 批量查询用户头像
        Map<Long, String> userAvatarMap = new java.util.HashMap<>();
        if (!creatorUserIds.isEmpty()) {
            List<UserEntity> users = userRepository.findAllById(creatorUserIds);
            System.out.println("查询到的用户数量: " + users.size());
            for (UserEntity u : users) {
                System.out.println("用户ID: " + u.getId() + ", 头像URL: " + u.getUserAvatarUrl());
                userAvatarMap.put(u.getId(), u.getUserAvatarUrl() != null ? u.getUserAvatarUrl() : "");
            }
        }

        // 批量查询图片数据
        Map<Long, List<String>> imageMap = new java.util.HashMap<>();
        if (!photoWallIds.isEmpty()) {
            for (Long photoWallId : photoWallIds) {
                List<photoWallDataEntity> imageData = photoWallDataRepository.findByPhotoWallId(photoWallId);
                List<String> urls = imageData.stream()
                        .map(photoWallDataEntity::getImgUrl)
                        .collect(Collectors.toList());
                System.out.println("照片墙ID: " + photoWallId + ", 图片数量: " + urls.size() + ", 图片URLs: " + urls);
                imageMap.put(photoWallId, urls);
            }
        }

        // 批量查询点赞数据
        List<PhotoWallInteractionEntity> allLikes = photoWallIds.isEmpty() ? List.of() :
                photoWallInteractionRepository.findByPhotoWallIdInAndInteractionTypeAndIsDeleted(
                        photoWallIds, 1, false);

        Map<Long, Long> likeCountMap = allLikes.stream()
                .collect(Collectors.groupingBy(PhotoWallInteractionEntity::getPhotoWallId, Collectors.counting()));

        Map<Long, Boolean> userLikedMap = allLikes.stream()
                .filter(like -> like.getUserId().equals(user.getId()))
                .collect(Collectors.toMap(PhotoWallInteractionEntity::getPhotoWallId, like -> true));

        // 转换为VO
        List<PhotoWallVo> voList = photoWalls.stream().map(entity -> {
            PhotoWallVo vo = new PhotoWallVo();
            vo.setId(entity.getId());
            vo.setHomeId(entity.getHomeId());
            vo.setCreateUserId(entity.getCreateUserId());
            vo.setCreateUserRoleName(entity.getCreateUserRoleName());

            String avatar = userAvatarMap.getOrDefault(entity.getCreateUserId(), "");
            vo.setCreateUserAvatar(avatar);
            System.out.println("设置头像 - 照片墙ID: " + entity.getId() + ", 用户ID: " + entity.getCreateUserId() + ", 头像: " + avatar);

            vo.setChildIds(entity.getChildIds());
            vo.setContent(entity.getContent());
            vo.setPostTime(entity.getPostTime());
            vo.setLocation(entity.getLocation());

            List<String> images = imageMap.getOrDefault(entity.getId(), new ArrayList<>());
            vo.setImages(images);
            System.out.println("设置图片 - 照片墙ID: " + entity.getId() + ", 图片列表: " + images);

            vo.setLikeCount(likeCountMap.getOrDefault(entity.getId(), 0L).intValue());
            vo.setLiked(userLikedMap.getOrDefault(entity.getId(), false));
            vo.setCreateTime(entity.getCreateTime());
            return vo;
        }).collect(Collectors.toList());

        // 构建返回结果
        PhotoWallPageVo result = new PhotoWallPageVo();
        result.setList(voList);
        result.setTotal(page.getTotalElements());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setHasMore(page.hasNext());

        return result;
    }

    @Override
    @Transactional
    public Boolean toggleLike(String token, Long photoWallId) {
        // 验证token
        UserEntity user = userService.getByToken(token);
        if (user == null) {
            throw new BusinessException(ErrorEnum.USER_NOT_EXIST);
        }

        // 验证照片墙是否存在
        PhotoWallEntity photoWall = photoWallRepository.findById(photoWallId)
                .orElseThrow(() -> new BusinessException(ErrorEnum.RESOURCE_NOT_FOUND));

        // 验证是否属于同一个家庭
        if (!photoWall.getHomeId().equals(user.getHomeId())) {
            throw new BusinessException(ErrorEnum.USER_PERMISSION_DENIED);
        }

        // 查询是否已点赞
        PhotoWallInteractionEntity existingLike = photoWallInteractionRepository
                .findByPhotoWallIdAndUserIdAndInteractionTypeAndIsDeleted(
                        photoWallId, user.getId(), 1, false);

        if (existingLike != null) {
            // 已点赞，取消点赞
            existingLike.setIsDeleted(true);
            photoWallInteractionRepository.save(existingLike);
            return false;
        } else {
            // 未点赞，添加点赞
            PhotoWallInteractionEntity newLike = new PhotoWallInteractionEntity();
            newLike.setPhotoWallId(photoWallId);
            newLike.setUserId(user.getId());
            newLike.setUserRoleName(DictUtil.getDict(DictConstant.ROLE, user.getUserRole()));
            newLike.setInteractionType(1); // 1表示点赞
            newLike.setIsDeleted(false);
            photoWallInteractionRepository.save(newLike);
            return true;
        }
    }
}
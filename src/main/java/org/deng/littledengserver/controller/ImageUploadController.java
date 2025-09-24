package org.deng.littledengserver.controller;

import org.deng.littledengserver.config.BaseResult;
import org.deng.littledengserver.config.BusinessException;
import org.deng.littledengserver.constant.ErrorEnum;
import org.deng.littledengserver.constant.ImageTypeEnum;
import org.deng.littledengserver.model.entity.UserEntity;
import org.deng.littledengserver.service.ImageService;
import org.deng.littledengserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/image")
public class ImageUploadController {


    @Autowired
    private ImageService imageService;

    /**
     * 上传图片接口
     *
     * @param token 用户token
     * @param image 图片文件
     * @return 包含图片唯一ID的响应
     */
    @PostMapping("/upload")
    public BaseResult<String> upload(
            @RequestParam String token,
            @RequestParam MultipartFile image,
            @RequestParam ImageTypeEnum typeEnum) {
        return BaseResult.success(imageService.imageUpload(token, image,typeEnum));
    }
}
package org.deng.littledengserver.controller;

import org.deng.littledengserver.config.BaseResult;
import org.deng.littledengserver.constant.ImageTypeEnum;
import org.deng.littledengserver.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
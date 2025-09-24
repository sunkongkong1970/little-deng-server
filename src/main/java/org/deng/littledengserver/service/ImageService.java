package org.deng.littledengserver.service;

import org.deng.littledengserver.constant.ImageTypeEnum;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片服务接口
 */
public interface ImageService {
    String imageUpload(String token, MultipartFile image,ImageTypeEnum typeEnum);
}
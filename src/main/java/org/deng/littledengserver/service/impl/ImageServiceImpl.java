package org.deng.littledengserver.service.impl;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.deng.littledengserver.config.BaseResult;
import org.deng.littledengserver.config.BusinessException;
import org.deng.littledengserver.constant.ErrorEnum;
import org.deng.littledengserver.constant.ImageTypeEnum;
import org.deng.littledengserver.model.entity.User;
import org.deng.littledengserver.model.entity.UserEntity;
import org.deng.littledengserver.service.ImageService;
import org.deng.littledengserver.service.UserService;
import org.deng.littledengserver.util.CosUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    @Resource
    private UserService userService;
    @Resource
    private CosUtil cosUtil;

    // 压缩后的图片质量，0-1之间，1表示最高质量
    @Value("${image.compress.quality:0.8}")
    private double compressQuality;

    // 压缩后的图片最大宽度
    @Value("${image.compress.max-width:1920}")
    private int maxWidth;

    // 压缩后的图片最大高度
    @Value("${image.compress.max-height:1080}")
    private int maxHeight;

    @Override
    public String imageUpload(String token, MultipartFile image,ImageTypeEnum typeEnum) {
        if (image == null || image.isEmpty()) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR);
        }

        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException(ErrorEnum.NEED_IMAGE);
        }

        UserEntity user = validUser(token);
        try {
            // 调用图片服务上传图片
            // 返回图片唯一ID
            return uploadImage(image, false, buildSavePath(user.getHomeId(),user.getOpenid(), image,typeEnum));
        } catch (Exception e) {
            throw new BusinessException(ErrorEnum.IMAGE_UPLOAD_ERROR.getCode(), ErrorEnum.IMAGE_UPLOAD_ERROR.getMessage() + ":" + e.getMessage());
        }
    }

    private UserEntity validUser(String token) {
        if (StringUtils.isBlank(token)) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR);
        }

        // 通过token获取用户信息
        UserEntity user = userService.getByToken(token);
        if (user == null) {
            throw new BusinessException(ErrorEnum.USER_NOT_EXIST);
        }

        if (user.getHomeId() == null) {
            throw new BusinessException(ErrorEnum.HOME_NOT_EXIST);
        }
        return user;
    }

    public String uploadImage(MultipartFile image, boolean needCompress, String savePath) {
        try {
            String url;
            if (needCompress) {
                // 压缩图片并保存
                ByteArrayOutputStream byteArrayOutputStream = compressAndSaveImage(image, savePath);
                url = cosUtil.uploadFile(byteArrayOutputStream.size(),new ByteArrayInputStream(byteArrayOutputStream.toByteArray()),savePath);
            } else {
                // 直接保存原图
                url = cosUtil.uploadFile(image,savePath);
            }

            // 返回图片唯一ID（这里返回文件名作为唯一标识）
            return url;
        } catch (IOException e) {
            throw new BusinessException(ErrorEnum.IMAGE_UPLOAD_ERROR.getCode(), ErrorEnum.IMAGE_UPLOAD_ERROR.getMessage() + ":" + e.getMessage());
        }
    }

    private String buildSavePath(Long homeId, String userOpenid,MultipartFile image,ImageTypeEnum typeEnum) {
        // rootPath/homeId
        String dirPath = String.valueOf(homeId);

        // 生成唯一文件名
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String originalFilename = image.getOriginalFilename();
        String fileExtension = originalFilename != null ? "." + getFileExtension(originalFilename) : ".jpg";

        String fileName;
        if (typeEnum.equals(ImageTypeEnum.DAILY)){
            fileName = "img_" + homeId + "_" + timestamp + "_" + fileExtension;
            return dirPath + "/" + fileName;
        }

        switch (typeEnum) {
            case BABY:
                fileName = "img_" + homeId + "_" +  userOpenid + "_" +timestamp + "_" + fileExtension;
                break;
            default:
                fileName = "img_" + homeId + "_" + timestamp + "_" + fileExtension;
        }

        // 保存图片
        return dirPath + "/" + fileName;
    }
    /**
     * 使用Thumbnailator压缩图片并保存
     *
     * @param image     原始图片文件
     * @param imagePath 保存路径
     * @throws IOException 图片处理异常
     */
    private ByteArrayOutputStream compressAndSaveImage(MultipartFile image, String imagePath) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(image.getInputStream())
                .size(maxWidth, maxHeight)
                .outputQuality(compressQuality)
                .toOutputStream(outputStream);
        return outputStream;
    }

    /**
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @return 文件扩展名（不含点）
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "jpg";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1).toLowerCase();
        }
        return "jpg";
    }


}
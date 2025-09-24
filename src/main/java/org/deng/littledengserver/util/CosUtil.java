package org.deng.littledengserver.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
public class CosUtil {
    @Autowired
    private COSClient cosClient;

    @Value("${tencent.cos.bucket-name}")
    private String bucketName;

    @Value("${tencent.cos.app-id}")
    private String appId;

    @Value("${tencent.cos.region}")
    private String region;

    @Value("${tencent.cos.custom-domain:}")
    private String customDomain;

    // 存储桶全称 (bucketName-APPID)
    private String getFullBucketName() {
        return bucketName + "-" + appId;
    }

    /**
     * 上传文件到COS
     * @param file 待上传的文件
     * @param savePath 存储路径
     * @return 访问URL
     */
    public String uploadFile(MultipartFile file, String savePath) throws IOException {
        // 设置文件元数据
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // 上传文件
        PutObjectRequest putObjectRequest = new PutObjectRequest(
                getFullBucketName(),
                savePath,
                file.getInputStream(),
                metadata
        );
        cosClient.putObject(putObjectRequest);

        // 生成访问URL
        return generateUrl(savePath);
    }

    public String uploadFile(int size,ByteArrayInputStream inputStream, String savePath) throws IOException {
        // 设置文件元数据
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(size);

        /// 上传文件到COS
        PutObjectRequest putObjectRequest = new PutObjectRequest(getFullBucketName(), savePath, inputStream, metadata);
        cosClient.putObject(putObjectRequest);

        // 生成访问URL
        return generateUrl(savePath);
    }

    /**
     * 生成文件访问URL
     */
    private String generateUrl(String key) {
        // 如果配置了自定义域名，则使用自定义域名
        if (customDomain != null && !customDomain.isEmpty()) {
            return customDomain + "/" + key;
        }

        // 否则使用默认域名
        return String.format("https://%s.cos.%s.myqcloud.com/%s",
                getFullBucketName(),
                region,
                key);
    }

    /**
     * 关闭COS客户端（一般在应用关闭时调用）
     */
    public void shutdown() {
        cosClient.shutdown();
    }
}

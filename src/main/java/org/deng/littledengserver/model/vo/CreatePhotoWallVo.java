package org.deng.littledengserver.model.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreatePhotoWallVo {
    private String token;
    private Long homeId;
    private String childIds;
    private String content;
    private LocalDateTime postTime;
    private String location;
    private List<String> imgUrls; // 图片URL列表
}
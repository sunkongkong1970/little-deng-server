package org.deng.littledengserver.model.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PhotoWallVo {
    private Long id;
    private Long homeId;
    private Long createUserId;
    private String createUserRoleName;
    private String createUserAvatar;
    private String childIds;
    private String content;
    private LocalDateTime postTime;
    private String location;
    private List<String> images;
    private Integer likeCount;
    private Boolean liked; // 当前用户是否点赞
    private LocalDateTime createTime;
}


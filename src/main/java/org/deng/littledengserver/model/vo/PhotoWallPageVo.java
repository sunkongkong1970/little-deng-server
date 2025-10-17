package org.deng.littledengserver.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class PhotoWallPageVo {
    private List<PhotoWallVo> list;
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private Boolean hasMore;
}


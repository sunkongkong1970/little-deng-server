package org.deng.littledengserver.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "photo_wall_interaction")
@EqualsAndHashCode(callSuper = true)
@Data
public class PhotoWallInteractionEntity extends BaseEntity{
    @Column
    private Long photoWallId;
    @Column
    private Long userId;
    @Column
    private String userRoleName;
    @Column
    private Integer interactionType;
    @Column(length = 1024)
    private String commentContent;
    @Column
    private Long parentId;
    @Column
    private Boolean isDeleted;
}

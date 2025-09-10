package org.deng.littledengserver.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "photo_wall")
@EqualsAndHashCode(callSuper = true)
@Data
public class PhotoWallEntity extends BaseEntity{
    @Column
    private Long homeId;
    @Column
    private Long createUserId;
    @Column
    private String createUserRoleName;
    @Column
    private String childIds;
    @Column(length = 2048)
    private String content;
    @Column
    private LocalDateTime postTime;
    @Column
    private String location;
    @Column
    private Boolean isDeleted;
}

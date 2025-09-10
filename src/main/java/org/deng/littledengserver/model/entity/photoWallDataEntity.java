package org.deng.littledengserver.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "photo_wall_data")
@EqualsAndHashCode(callSuper = true)
@Data
public class photoWallDataEntity extends BaseEntity{
    @Column
    private Long photoWallId;
    @Column
    private String imgUrl;
}

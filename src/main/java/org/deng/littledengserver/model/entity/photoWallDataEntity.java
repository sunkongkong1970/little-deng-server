package org.deng.littledengserver.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

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

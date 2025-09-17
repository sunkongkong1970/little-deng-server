package org.deng.littledengserver.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "home")
@EqualsAndHashCode(callSuper = true)
@Data
public class HomeEntity extends BaseEntity{
    @Column
    private String homeName;
    @Column
    private Long householderUserId;
}

package org.deng.littledengserver.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "user")
@Data
public class UserEntity extends BaseEntity{
    @Column(length = 255)
    private String openid;

    @Column(length = 255)
    private String unionid;

    @Column(length = 255)
    private String userName ;

    @Column(length = 255)
    private String userAvatarUrl ;

    @Column(length = 255)
    private String userRoleName ;

    @Column(length = 255)
    private Long home_id;

    @Column(length = 255)
    private Boolean isHouseholder ;

}

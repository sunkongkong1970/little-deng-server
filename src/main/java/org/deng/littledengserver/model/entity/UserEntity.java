package org.deng.littledengserver.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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
    private String phoneNum;

    @Column(length = 255)
    private String userName ;

    @Column(length = 255)
    private String userAvatarUrl ;

    @Column(length = 255)
    private String userRoleName ;

    @Column(length = 255)
    private Long homeId;

    @Column(length = 255)
    private Boolean isHouseholder ;

}

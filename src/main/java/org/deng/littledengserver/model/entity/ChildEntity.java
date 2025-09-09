package org.deng.littledengserver.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "child")
@EqualsAndHashCode(callSuper = true)
@Data
public class ChildEntity extends BaseEntity{
    @Column(nullable = false)
    private Long homeId;
    @Column
    private String childName;
    @Column
    private String childGender;
    @Column
    private String childNickname;
    @Column
    private LocalDateTime childBirthday;
    @Column
    private String childZodiac;
    @Column
    private String childCoverImg;
    @Column(length = 2048)
    private String childContent;
}

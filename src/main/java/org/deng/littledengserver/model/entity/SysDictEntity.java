package org.deng.littledengserver.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "dict")
public class SysDictEntity {
    @Id
    Long id;

    @Column
    private String code;

    @Column
    private String text;

    @Column
    private String name;
}

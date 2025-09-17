package org.deng.littledengserver.config.dict;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Dict {
    /**
     * 字典类型
     */
    String type();

    /**
     * 对应的名称字段后缀，默认为"Name"
     */
    String suffix() default "Name";
}

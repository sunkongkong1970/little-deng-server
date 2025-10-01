package org.deng.littledengserver.config.dict;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 支持字典翻译的基类
 * 子类继承此类后，可以通过 @Dict 注解自动添加字典翻译字段
 */
public class DictTranslatable {
    
    /**
     * 存储动态添加的字典翻译字段
     * 使用 @JsonAnyGetter 注解，使这些字段能够在 JSON 序列化时输出
     */
    @Getter(onMethod_ = @JsonAnyGetter)
    private Map<String, Object> dictFields = new HashMap<>();
    
    /**
     * 添加字典翻译字段
     * @param fieldName 字段名
     * @param value 字段值
     */
    public void addDictField(String fieldName, Object value) {
        this.dictFields.put(fieldName, value);
    }
    
    /**
     * 获取字典翻译字段
     * @param fieldName 字段名
     * @return 字段值
     */
    public Object getDictField(String fieldName) {
        return this.dictFields.get(fieldName);
    }
    
    /**
     * 清空字典翻译字段
     */
    public void clearDictFields() {
        this.dictFields.clear();
    }
}


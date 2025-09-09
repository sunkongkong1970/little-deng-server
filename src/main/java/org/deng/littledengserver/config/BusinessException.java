package org.deng.littledengserver.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.deng.littledengserver.constant.ErrorEnum;

/**
 * 自定义业务异常类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException{
    private int code;
    private String message;

    public BusinessException(ErrorEnum errorEnum) {
        this.code = errorEnum.getCode();
        this.message = errorEnum.getMessage();
    }

    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

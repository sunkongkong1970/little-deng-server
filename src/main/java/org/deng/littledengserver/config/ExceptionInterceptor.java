package org.deng.littledengserver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ExceptionInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionInterceptor.class);

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResult<Void> handleBusinessException(BusinessException e) {
        // 记录异常信息
        logger.error("业务异常: {}", e.getMessage(), e);
        // 返回统一格式的错误响应
        return BaseResult.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理其他未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public BaseResult<Void> handleOtherExceptions(Exception e) {
        // 记录异常信息
        logger.error("系统异常: {}", e.getMessage(), e);
        // 返回统一格式的错误响应
        return BaseResult.error(500, "系统异常，请联系管理员");
    }

}

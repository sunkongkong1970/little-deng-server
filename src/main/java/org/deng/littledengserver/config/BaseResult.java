package org.deng.littledengserver.config;

import lombok.Data;
import org.deng.littledengserver.constant.ErrorEnum;

import java.io.Serializable;

@Data
public class BaseResult<T> implements Serializable {
    /**
     * 状态码：0表示成功，其他值表示失败
     */
    private int code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    // 私有构造方法，防止直接实例化
    private BaseResult() {}

    /**
     * 成功返回结果
     * @param data 返回数据
     * @param <T> 数据类型
     * @return 统一响应对象
     */
    public static <T> BaseResult<T> success(T data) {
        BaseResult<T> BaseResult = new BaseResult<>();
        BaseResult.code = 0;
        BaseResult.message = "成功";
        BaseResult.data = data;
        return BaseResult;
    }

    /**
     * 成功返回结果，带自定义消息
     * @param data 返回数据
     * @param message 自定义消息
     * @param <T> 数据类型
     * @return 统一响应对象
     */
    public static <T> BaseResult<T> success(T data, String message) {
        BaseResult<T> BaseResult = new BaseResult<>();
        BaseResult.code = 0;
        BaseResult.message = message;
        BaseResult.data = data;
        return BaseResult;
    }

    /**
     * 失败返回结果
     * @param code 错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 统一响应对象
     */
    public static <T> BaseResult<T> error(int code, String message) {
        BaseResult<T> BaseResult = new BaseResult<>();
        BaseResult.code = code;
        BaseResult.message = message;
        BaseResult.data = null;
        return BaseResult;
    }

    // 错误响应，基于枚举
    public static <T> BaseResult<T> error(ErrorEnum errorEnum) {
        return error(errorEnum.getCode(), errorEnum.getMessage());
    }

    /**
     * 失败返回结果，使用默认错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 统一响应对象
     */
    public static <T> BaseResult<T> error(String message) {
        return error(1, message);
    }
}

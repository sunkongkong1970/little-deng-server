package org.deng.littledengserver.constant;

import lombok.Getter;

@Getter
public enum ErrorEnum {
    // 通用错误
    SYSTEM_ERROR(1, "系统异常"),
    PARAM_ERROR(2, "参数错误"),
    RESOURCE_NOT_FOUND(3, "资源不存在"),

    //微信小程序接口
    JS_CODE_2_SESSION_ERROR(500,"jscode2session返回异常"),
    JS_CODE_2_SESSION_ANALYSIS_ERROR(501,"jscode2session数据解析异常"),
    JS_CODE_2_SESSION_RETURN_ERROR(502,"jscode2session返回错误"),
    WE_CHAT_LOGIN_FAIL(503,"微信登陆失败"),
    WE_CHAT_LOGIN_OVERTIME(504,"登录过期，请重新登陆"),

    // 用户相关错误
    USER_NOT_EXIST(2001, "用户不存在"),
    USER_NOT_LOGIN(2002, "用户未登录"),
    USER_AUTH_FAILED(2003, "用户认证失败"),
    USER_PERMISSION_DENIED(2004, "用户权限不足"),

    // 家庭相关错误
    HOME_CODE_OVERTIME(2100, "邀请码已过期"),
    HOME_NOT_EXIST(2101, "家庭不存在"),

    //图片上传
    IMAGE_UPLOAD_ERROR(2200,"图片上传失败"),
    MKDIR_FAILED(2201,"创建文件夹失败"),
    NEED_IMAGE(2202,"请上传图片"),
    GET_IMAGE_NAME_FAIL(2203,"获取图片文件名失败")
    ;


    private final int code;
    private final String message;

    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}

package org.deng.littledengserver.constant;

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
    ;


    private final int code;
    private final String message;

    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

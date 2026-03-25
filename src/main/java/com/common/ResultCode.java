package com.common;

/**
 * 统一响应状态码
 */
public enum ResultCode {

    SUCCESS(0, "成功"),
    ERROR(500, "服务器内部错误"),
    UNAUTHORIZED(511, "未登录或登录已过期");

    private final int code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

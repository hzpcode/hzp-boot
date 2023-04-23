package com.hzp.web.exception.constant;

/**
 * 基础响应码
 *
 * @author yxy
 * @date 2020/5/19 10:35
 */

public enum ReturnCode implements IReturnCode {
    FORCE_UPDATE("40000", "网站功能升级，请刷新页面获取最新功能"),
    SUCCESS("40001", "操作成功"),
    EXCEPTION("40002", "系统异常或其他需要报警的错误"),
    LACK_PARAMETER("40003", "缺少参数：{%s}"),
    PARAM_ERROR("40004", "参数错误：{%s}"),
    NOT_LOGIN("40005", "该操作需要登录"),
    NO_PRIVILEGE("40006", "无权限"),
    TOKEN_EXPIRED("40007", "无权限或者token已经过期"),
    TOO_FREQUENT_REQUEST("40008", "操作太快，休息一会！"),
    SYSTEM_BUSY("40009", "系统繁忙，请稍后重试"),
    OPERATE_ERROR("40010", "操作错误"),
    COMMON_ERROR("4001", "一般错误：{%s}"),


    ERROR("40012", "一般错误（无需报警的错误）"),
    VERSION_ERROR("400013", "版本太低，请刷新页面！"),

    ACCOUNT_LOGIN_OR_PWD_ERROR("40027", "用户名或者密码错误"),
    AUTH_TYPE_UN_SUPPORT("40031", "不支持的认证类型"),
    MS_ACCESS_TOKEN_ERROR("40032", "非法请求！"),;

    private String code;
    private String msg;

    ReturnCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}

package com.hzp.web.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hzp.web.exception.constant.IReturnCode;
import com.hzp.web.exception.constant.ReturnCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class JsonResult<T> {

    @ApiModelProperty(value = "状态码", example = "40001", required = true, position = -2)
    private String code;

    @ApiModelProperty(value = "返回的消息", example = "恭喜，成功！", required = true, position = -1)
    private String message;

    @ApiModelProperty(value = "具体的数据")
    private T data;

    public static <D> JsonResult<D> build(IReturnCode code) {
        return build(code.getCode(), code.getMsg());
    }

    public static <D> JsonResult<D> build(IReturnCode code, String msg) {
        return build(code.getCode(), msg);
    }

    public static <D> JsonResult<D> build(IReturnCode code, String msg, D data) {
        return build(code.getCode(), msg, data);
    }

    public static <D> JsonResult<D> build(String code, String msg) {
        JsonResult<D> jr = new JsonResult<>();
        jr.setCode(code);
        jr.setMessage(msg);
        return jr;
    }

    public static <D> JsonResult<D> build(String code, String msg, D data) {
        JsonResult<D> jr = new JsonResult<>();
        jr.setCode(code);
        jr.setMessage(msg);
        jr.setData(data);
        return jr;
    }

    /**
     * 成功的返回
     */
    public static <D> JsonResult<D> success() {
        return JsonResult.build(ReturnCode.SUCCESS);
    }

    /**
     * 成功的返回
     */
    public static <D> JsonResult<D> success(String msg) {
        return JsonResult.build(ReturnCode.SUCCESS, msg);
    }

    /**
     * 成功的返回
     */
    public static <D> JsonResult<D> success(D data) {
        return JsonResult.build(ReturnCode.SUCCESS, ReturnCode.SUCCESS.getMsg(), data);
    }

    /**
     * 成功的返回
     */
    public static <D> JsonResult<D> success(String msg, D data) {
        return JsonResult.build(ReturnCode.SUCCESS, msg, data);
    }

    /**
     * 系统异常或其他需要报警的错误
     */
    public static <D> JsonResult<D> exception() {
        return JsonResult.build(ReturnCode.EXCEPTION);
    }

    /**
     * 系统异常或其他需要报警的错误
     */
    public static <D> JsonResult<D> exception(String msg) {
        return JsonResult.build(ReturnCode.EXCEPTION, msg);
    }

    /**
     * 一般错误（无需报警的错误）
     */
    public static <D> JsonResult<D> error(String msg) {
        return JsonResult.build(ReturnCode.ERROR, msg);
    }

    /**
     * 一般错误（无需报警的错误）
     */
    public static <D> JsonResult<D> error(String msg, D data) {
        return JsonResult.build(ReturnCode.ERROR, msg, data);
    }

    /**
     * 参数错误
     */
    public static <D> JsonResult<D> paramError(String msg) {
        return JsonResult.build(ReturnCode.PARAM_ERROR, msg);
    }

    /**
     * 参数错误
     */
    public static <D> JsonResult<D> paramError(String msg, D data) {
        return JsonResult.build(ReturnCode.PARAM_ERROR, msg, data);
    }

    public static <D> JsonResult<D> notLogin() {
        return JsonResult.build(ReturnCode.NOT_LOGIN);
    }

    /**
     * 无权限
     */
    public static <D> JsonResult<D> noPrivilege(String msg, D data) {
        return JsonResult.build(ReturnCode.NO_PRIVILEGE, msg, data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @JsonIgnore
    public boolean isOk() {
        return ReturnCode.SUCCESS.getCode().equals(this.code);
    }

}
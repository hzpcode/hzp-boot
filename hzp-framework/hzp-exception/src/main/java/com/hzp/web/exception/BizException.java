package com.hzp.web.exception;


import com.hzp.web.exception.constant.IReturnCode;
import com.hzp.web.exception.constant.ReturnCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 业务统一异常
 *
 * @author yxy
 * @date 2019/10/15 20:30
 **/

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BizException extends RuntimeException {

    private static final long serialVersionUID = -6676961152270438174L;

    /**
     * 异常编码
     */
    private String code;

    /**
     * 异常信息
     */
    private String msg;

    public BizException() {
        super();
    }

    public BizException(String message) {
        this(ReturnCode.COMMON_ERROR, message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(String code, String msg) {
        super(String.format("ErrCode:[%s], ErrMsg:[%s]", code, msg));
        this.code = code;
        this.msg = msg;
    }

    public BizException(IReturnCode code) {
        this(code.getCode(), code.getMsg());
    }

    public BizException(IReturnCode code, Object... msg) {
        this(code.getCode(), String.format(code.getMsg(), msg));
    }

}

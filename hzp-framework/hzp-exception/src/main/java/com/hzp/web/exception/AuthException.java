package com.hzp.web.exception;

import com.hzp.web.exception.constant.IReturnCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 认证统一异常
 *
 * @author yxy
 * @date 2019/10/15 20:30
 **/

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AuthException extends BizException {

    private static final long serialVersionUID = -6676961152270438174L;

    public AuthException() {
        super();
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthException(Throwable cause) {
        super(cause);
    }

    public AuthException(String code, String msg) {
        super(code, msg);
    }

    public AuthException(IReturnCode code) {
        this(code.getCode(), code.getMsg());
    }

    public AuthException(IReturnCode code, Object... msg) {
        this(code.getCode(), String.format(code.getMsg(), msg));
    }

}

package com.hzp.web.exception;

import com.hzp.web.exception.constant.IReturnCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 参数异常
 *
 * @author yxy
 * @date 2019/10/24 13:47
 **/

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ParameterException extends BizException{

    private static final long serialVersionUID = -6676961152270438174L;

    public ParameterException() {
        super();
    }

    public ParameterException(String message) {
        super(message);
    }

    public ParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterException(Throwable cause) {
        super(cause);
    }

    public ParameterException(String code, String msg) {
        super(code, msg);
    }

    public ParameterException(IReturnCode code, Object... msg) {
        this(code.getCode(), String.format(code.getMsg(), msg));
    }

    public ParameterException(IReturnCode err) {
        this(err.getCode(), err.getMsg());
    }

}

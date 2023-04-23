package com.hzp.web.util;

import com.hzp.web.exception.BizException;
import com.hzp.web.exception.ParameterException;
import com.hzp.web.exception.constant.IReturnCode;
import com.hzp.web.exception.constant.ReturnCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 断言工具类
 *
 * @author yxy
 * @date 2019/12/13 16:51
 **/
public class CheckUtils {

    private CheckUtils() { }

    public static CheckUtils build() {
        return new CheckUtils();
    }

    /**
     * 校验是否为非空
     *
     * param o          对象
     * @param errCode   异常码
     */
    public CheckUtils nonNull(Object o, IReturnCode errCode) {
        if(Objects.isNull(o)) {
            thrEx(errCode);
        }
        return this;
    }

    public CheckUtils nonNull(Object o, IReturnCode errCode, Object... formatErrMsg) {
        if(Objects.isNull(o)) {
            thrEx(errCode, formatErrMsg);
        }
        return this;
    }

    /**
     * 校验是否为非空
     *
     * param o          对象
     * @param errMsg   异常码
     */
    public CheckUtils nonNull(Object o, String errMsg) {
        if(Objects.isNull(o)) {
            thrEx(errMsg);
        }
        return this;
    }

    /**
     * 校验是否是null
     *
     * param o          对象
     * @param errCode   异常码
     */
    public CheckUtils isNull(Object o, IReturnCode errCode) {
        if(Objects.nonNull(o)) {
            thrEx(errCode);
        }
        return this;
    }

    public CheckUtils isNull(Object o, IReturnCode errCode, Object... formatErrMsg) {
        if(Objects.nonNull(o)) {
            thrEx(errCode, formatErrMsg);
        }
        return this;
    }

    /**
     * 校验是否是null
     *
     * param o          对象
     * @param errMsg   异常码
     */
    public CheckUtils isNull(Object o, String errMsg) {
        if(Objects.nonNull(o)) {
            thrEx(errMsg);
        }
        return this;
    }

    public CheckUtils isEmpty(Collection<?> collection, IReturnCode errCode) {
        if(!CollectionUtils.isEmpty(collection)) {
            thrEx(errCode);
        }
        return this;
    }

    public CheckUtils isEmpty(Collection<?> collection, IReturnCode errCode, Object... formatErrMsg) {
        if(!CollectionUtils.isEmpty(collection)) {
            thrEx(errCode, formatErrMsg);
        }
        return this;
    }

    public CheckUtils isEmpty(Collection<?> collection, String errMsg) {
        if(!CollectionUtils.isEmpty(collection)) {
            thrEx(errMsg);
        }
        return this;
    }

    public CheckUtils isEmpty(Map<?, ?> collection, IReturnCode errCode) {
        if(!CollectionUtils.isEmpty(collection)) {
            thrEx(errCode);
        }
        return this;
    }

    public CheckUtils isEmpty(Map<?, ?> collection, IReturnCode errCode, Object... formatErrMsg) {
        if(!CollectionUtils.isEmpty(collection)) {
            thrEx(errCode, formatErrMsg);
        }
        return this;
    }

    public CheckUtils isEmpty(Map<?, ?> collection, String errMsg) {
        if(!CollectionUtils.isEmpty(collection)) {
            thrEx(errMsg);
        }
        return this;
    }

    public CheckUtils isEmpty(String string, IReturnCode errCode) {
        if(StringUtils.isNotEmpty(string)) {
            thrEx(errCode);
        }
        return this;
    }

    public CheckUtils isEmpty(String string, IReturnCode errCode, Object... formatErrMsg) {
        if(StringUtils.isNotEmpty(string)) {
            thrEx(errCode, formatErrMsg);
        }
        return this;
    }

    public CheckUtils isEmpty(String string, String errMsg) {
        if(StringUtils.isNotEmpty(string)) {
            thrEx(errMsg);
        }
        return this;
    }

    public CheckUtils isNotEmpty(Collection<?> collection, IReturnCode errCode) {
        if(CollectionUtils.isEmpty(collection)) {
            thrEx(errCode);
        }
        return this;
    }

    public CheckUtils isNotEmpty(Collection<?> collection, IReturnCode errCode, Object... formatErrMsg) {
        if(CollectionUtils.isEmpty(collection)) {
            thrEx(errCode, formatErrMsg);
        }
        return this;
    }

    public CheckUtils isNotEmpty(Collection<?> collection, String errMsg) {
        if(CollectionUtils.isEmpty(collection)) {
            thrEx(errMsg);
        }
        return this;
    }

    public CheckUtils isNotEmpty(Map<?, ?> collection, IReturnCode errCode) {
        if(CollectionUtils.isEmpty(collection)) {
            thrEx(errCode);
        }
        return this;
    }

    public CheckUtils isNotEmpty(Map<?, ?> collection, IReturnCode errCode, Object... formatErrMsg) {
        if(CollectionUtils.isEmpty(collection)) {
            thrEx(errCode, formatErrMsg);
        }
        return this;
    }

    public CheckUtils isNotEmpty(Map<?, ?> collection, String errMsg) {
        if(CollectionUtils.isEmpty(collection)) {
            thrEx(errMsg);
        }
        return this;
    }

    public CheckUtils isBlank(String str, IReturnCode errCode) {
        if (StringUtils.isNotBlank(str)) {
            thrEx(errCode);
        }
        return this;
    }

    public CheckUtils isBlank(String str, IReturnCode errCode, Object... formatErrMsg) {
        if (StringUtils.isNotBlank(str)) {
            thrEx(errCode, formatErrMsg);
        }
        return this;
    }

    public CheckUtils isBlank(String str, String errMsg) {
        if (StringUtils.isNotBlank(str)) {
            thrEx(errMsg);
        }
        return this;
    }

    public CheckUtils isNotBlank(String str, IReturnCode errCode) {
        if(StringUtils.isBlank(str)) {
            thrEx(errCode);
        }
        return this;
    }

    public CheckUtils isNotBlank(String str, IReturnCode errCode, Object... formatErrMsg) {
        if(StringUtils.isBlank(str)) {
            thrEx(errCode, formatErrMsg);
        }
        return this;
    }

    public CheckUtils isNotBlank(String str, String errMsg) {
        if(StringUtils.isBlank(str)) {
            thrEx(errMsg);
        }
        return this;
    }

    public CheckUtils isTrue(Boolean flag, IReturnCode errCode)  {
        if(Objects.isNull(flag) || Boolean.FALSE.equals(flag)) {
            thrEx(errCode);
        }
        return this;
    }

    public CheckUtils isTrue(Boolean flag, IReturnCode errCode, Object... formatErrMsg)  {
        if(Objects.isNull(flag) || Boolean.FALSE.equals(flag)) {
            thrEx(errCode, formatErrMsg);
        }
        return this;
    }

    public CheckUtils isTrue(Boolean flag, String errMsg)  {
        if(Objects.isNull(flag) || Boolean.FALSE.equals(flag)) {
            thrEx(errMsg);
        }
        return this;
    }

    public CheckUtils isFalse(Boolean flag, IReturnCode errCode) {
        if(Objects.nonNull(flag) && Boolean.TRUE.equals(flag)) {
            thrEx(errCode);
        }
        return this;
    }

    public CheckUtils isFalse(Boolean flag, IReturnCode errCode, Object... formatErrMsg) {
        if(Objects.nonNull(flag) && Boolean.TRUE.equals(flag)) {
            thrEx(errCode, formatErrMsg);
        }
        return this;
    }

    public CheckUtils isFalse(Boolean flag, String errMsg) {
        if(Objects.nonNull(flag) && Boolean.TRUE.equals(flag)) {
            thrEx(errMsg);
        }
        return this;
    }

    public CheckUtils customerCheck(CustomerChecker customerChecker, IReturnCode errCode) {
        if (customerChecker.verify()) {
            thrEx(errCode);
        }
        return this;
    }

    public CheckUtils customerCheck(CustomerChecker customerChecker, IReturnCode errCode, Object... formatErrMsg) {
        if (customerChecker.verify()) {
            thrEx(errCode, formatErrMsg);
        }
        return this;
    }

    public void customerCheck(CustomerChecker customerChecker, String errMsg) {
        if (customerChecker.verify()) {
            thrEx(errMsg);
        }
    }


    private void thrEx(String message) {
        throw new ParameterException(
                ReturnCode.PARAM_ERROR.getCode(), message);

    }

    private CheckUtils thrEx(IReturnCode errCode) {
        throw new BizException(errCode);
    }

    private CheckUtils thrEx(IReturnCode errCode, Object... formatErrMsg) {
        throw new BizException(errCode, formatErrMsg);
    }

    @FunctionalInterface
    public interface CustomerChecker {

        boolean verify();

    }

}

package com.hzp.web.util;

import com.hzp.web.exception.ParameterException;
import com.hzp.web.exception.constant.ReturnCode;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;


/**
 *  参数校验工具类
 *
 *  @author Yu
 *  @date 2020/5/24 0:18
 */
public class ValidatedUtils {

    private ValidatedUtils() { }

    private static Validator validator;
    static {
        validator = Validation.byDefaultProvider().configure()
                .messageInterpolator(new ResourceBundleMessageInterpolator(
                        new PlatformResourceBundleLocator("validationMessages")))
                .buildValidatorFactory().getValidator();
    }

    public static Validator getValidator() {
        return validator;
    }

    public static void validate(Object object, Class<?>... groups) {
        resolveErrMsg(validator.validate(object, groups));
    }

    public static void resolveErrMsg(Set<ConstraintViolation<Object>> constraintViolations) {
        String messages = constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));
        if(StringUtils.isNotBlank(messages)){
            throw new ParameterException(ReturnCode.PARAM_ERROR, messages);
        }
    }

}

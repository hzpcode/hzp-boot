package com.hzp.web.annotation;

import com.hzp.web.util.IdCardNoUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *  身份证号码验证
 *
 *  @author yxy
 *  @date 2020/4/3 10:32
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {IdCard.IdCardValidator.class})
public @interface IdCard {
    String message() default "身份证号码有误，请检查";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class IdCardValidator implements ConstraintValidator<IdCard, String> {

        @Override
        public void initialize(IdCard constraintAnnotation) {

        }

        /**
         * 校验身份证号码
         * 如果为空,则返回true,如果校验非空请使用@NotEmpty组合注解
         *
         * @param value     值
         * @param context   校验器上下文
         * @return          是否校验成功
         */
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (StringUtils.isEmpty(value)) {
                return true;
            }
            return IdCardNoUtils.validateCard(value);
        }
    }
}

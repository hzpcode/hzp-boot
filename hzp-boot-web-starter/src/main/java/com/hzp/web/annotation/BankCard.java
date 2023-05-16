package com.hzp.web.annotation;

import com.hzp.web.util.BankCardUtils;
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
 *  银行卡验证
 *
 *  @author yxy
 *  @date 2020/4/3 10:32
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {BankCard.BankCardValidator.class})
public @interface BankCard {
    String message() default "银行卡号码有误，请检查";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class BankCardValidator implements ConstraintValidator<BankCard, String> {

        @Override
        public void initialize(BankCard constraintAnnotation) {

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
            return BankCardUtils.isValidCardNo(value);
        }
    }
}

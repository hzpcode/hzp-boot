package com.hzp.web.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hzp.web.constant.MaskTypeEnum;
import com.hzp.web.convert.DynamicMaskSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 动态脱敏
 *
 * @author yxy
 * @date 2020/08/24 14:09
 **/


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = DynamicMaskSerializer.class)
public @interface DynamicMask {

    /**
     * 优先级L3, 前缀长度
     */
    int prefixLen() default 3;

    /**
     * 优先级L3, 后缀长度
     */
    int suffixLen() default 4;

    /**
     * 优先级L1, 蒙版字符
     */
    String maskString() default "*";

    /**
     * 优先级L2, L2以下配置失效
     */
    String regex() default "";

    /**
     * 优先级L1，L1以下配置失效
     */
    MaskTypeEnum maskTypeEnum() default MaskTypeEnum.NULL;

}

package com.hzp.web.annotation;

import java.lang.annotation.*;

/**
 * 校验参数
 *
 * @author yxy
 * @date 2019/11/22 10:55
 **/

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
public @interface ValidParam {

    /**
     * 分组校验
     */
    Class<?>[] value() default {};

}

package com.hzp.web.annotation;


import java.lang.annotation.*;

/**
 * 异常邮件提醒
 *
 * @author han
 * @date 2020-11-30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface SendEmailForException {
    /**
     * 是否需要发送邮件，缺省为需要
     */
    boolean sendEmail() default true;

    /**
     * 接收地址 默认空（不发送）
     */
    String[] receiveAddress() default {};

    /**
     * 邮件发送地址，默认空
     */
    String sendAddress() default "";

    /**
     * 需要发送邮件的环境  prod正式服默认为需要发送，默认空
     */
    String profiles() default "";

    /**
     * 打印请求参数，用于排除请求数据过大情况
     * @return
     */
    boolean printParams() default true;

    /**
     * 忽略小时起始 包含
     */
    int excludeHourStart() default 0;

    /**
     * 忽略小时结束 不包含
     */
    int excludeHourEnd() default 0;

    /**
     * 每天通知次数 最小为1,小于以1计算
     */
    int times() default 5;
}
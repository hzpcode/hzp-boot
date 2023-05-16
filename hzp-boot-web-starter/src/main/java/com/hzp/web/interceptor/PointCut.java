package com.hzp.web.interceptor;

import org.aspectj.lang.annotation.Pointcut;

/**
 * @author wangchun
 * @date 2020/8/12
 */
public class PointCut {
    @Pointcut("execution(* com.hzp.*.controller..*Controller.*(..))")
    public void controllerPointcut() {
    }


    @Pointcut("execution(* com.hzp.mscontroller..*(..))")
    public void msControllerPointcut() {
    }

    /**
     * 定义拦截规则
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void mappingPointcut() {
    }
}
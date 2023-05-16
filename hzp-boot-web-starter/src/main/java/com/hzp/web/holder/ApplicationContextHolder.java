package com.hzp.web.holder;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * spring容器上下文持有者
 *
 * @author yxy
 * @date 2020/03/11 14:06
 **/

@Component
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static <T> T getBean(Class<T> clz) {
        return context.getBean(clz);
    }

    public static void publishEvent(ApplicationEvent event) {
        context.publishEvent(event);
    }

}

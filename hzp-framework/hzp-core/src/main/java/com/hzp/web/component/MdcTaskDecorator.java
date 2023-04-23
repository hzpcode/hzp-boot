package com.hzp.web.component;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

/**
 * Mdc任务解析器
 *
 * @author yxy
 * @date 2020/09/17 10:33
 **/
public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        return new MdcRunnable(runnable, MDC.getCopyOfContextMap(), ThreadLocalHelper.getAll(), ThreadLocalHelper.getCopyThreadLocalMap());
    }
}

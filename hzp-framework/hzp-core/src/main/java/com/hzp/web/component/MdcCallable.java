package com.hzp.web.component;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Mdc扩展Callable
 *
 * @author yxy
 * @date 2020/09/17 11:10
 **/
public class MdcCallable<T> implements Callable<T> {

    private Callable<T> callable;
    private Map<String, String> mdcContext;
    private Map<String, Object> threadLocalContext;
    private Map<ThreadLocal<?>, ?> threadLocalMap;

    public MdcCallable(Callable<T> callable, Map<String, String> mdcContext,
                       Map<String, Object> threadLocalContext, Map<ThreadLocal<?>, ?> threadLocalMap) {
        this.callable = callable;
        this.mdcContext = mdcContext;
        this.threadLocalContext = threadLocalContext;
        this.threadLocalMap = threadLocalMap;
    }

    @Override
    @SuppressWarnings("all")
    public T call() throws Exception {
        Map<String, String> previous = MDC.getCopyOfContextMap();
        if (mdcContext == null) {
            MDC.clear();
        } else {
            MDC.setContextMap(mdcContext);
        }

        for (Map.Entry<ThreadLocal<?>, ?> entry : threadLocalMap.entrySet()) {
            ThreadLocal key = entry.getKey();
            key.set(entry.getValue());
        }

        ThreadLocalHelper.putAll(threadLocalContext);
        try {
            return callable.call();
        } finally {
            if (previous == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(previous);
            }

            ThreadLocalHelper.clear();
            threadLocalMap.keySet().forEach(ThreadLocal::remove);
        }
    }
}

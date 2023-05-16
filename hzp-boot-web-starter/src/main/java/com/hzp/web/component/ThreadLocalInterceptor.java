package com.hzp.web.component;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ThreadLocal临时变量拦截器
 * <p>可以将ThreadLocal值在此处注入或者自动将从http进入的线程关联threadLocal自动清理</p>
 *
 * @author yxy
 * @date 2019/10/16 23:23
 **/
public class ThreadLocalInterceptor extends HandlerInterceptorAdapter {

    /**
     * 是否是从http进入的线程
     */
    private static final ThreadLocal<Boolean> IS_HTTP_REQUEST = new ThreadLocal<>();

    static {
        ThreadLocalCleaner.register(IS_HTTP_REQUEST);
    }

    public static boolean isHttpReqThread() {
        return Boolean.TRUE.equals(IS_HTTP_REQUEST.get());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //thread local set
        IS_HTTP_REQUEST.set(Boolean.TRUE);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //thread local remove
        ThreadLocalCleaner.clear();
        super.afterCompletion(request, response, handler, ex);
    }

}

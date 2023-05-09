package com.hzp.trace.interceptor;

import com.hzp.trace.constant.TraceConstant;
import org.slf4j.MDC;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class LogTraceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String traceId = request.getHeader(TraceConstant.TRACE_HEADER_NAME);
        if (ObjectUtils.isEmpty(traceId)) {
            traceId = UUID.randomUUID().toString().replaceAll("-", "");
        }
        MDC.put(TraceConstant.TRACE_ID_NAME, traceId);
        return true;


    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //调用结束后删除
        MDC.remove(TraceConstant.TRACE_ID_NAME);
    }
}

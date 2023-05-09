package com.hzp.trace.interceptor;

import com.hzp.trace.constant.TraceConstant;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 链路拦截器
 *
 * @author Yu
 * @date 2020/04/20 22:41
 **/
// todo 这里用HandlerInterceptorAdapter为什么有坑
    // 慎用Spring HandlerInterceptorAdapter HandlerInterceptor 拦截器
@Deprecated
public class TraceRouterInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = request.getHeader(TraceConstant.TRACE_HEADER_NAME);
        if(StringUtils.isEmpty(traceId)) {
            traceId = UUID.randomUUID().toString().replaceAll("-", "");
        }
        MDC.put(TraceConstant.TRACE_ID_NAME, traceId);

        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.remove(TraceConstant.TRACE_ID_NAME);
        super.afterCompletion(request, response, handler, ex);
    }

    public String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip!=null && ip.length()!=0) {
            //如果有多次代理（如"1.1.1.1, 2.2.2.2, 3.3.3.3"），只取第一个：
            if(ip.contains(",")){
                ip=ip.substring(0, ip.indexOf(","));
            }
        }
        if (ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

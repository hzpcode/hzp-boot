package com.hzp.trace.constant;

/**
 * 链路常量
 *
 * @author Yu
 * @date 2020/04/21 21:26
 **/


public class TraceConstant {

    private TraceConstant() {
    }

    public static final String TRACE_HEADER_NAME = "x-trace-id";

    public static final String CLIENT_IP_HEADER = "client-ip-forward";

    public static final String TRACE_ID_NAME = "traceId";

    public static final String SERVER_IP = "server-ip";

}

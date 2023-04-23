package com.hzp.mq.base.constant;

/**
 * 消息常量
 *
 * @author Yu
 * @date 2021/03/17 23:01
 **/
public class MessageConstant {

    public static final String RETRY_TASK_LOCK_KEY = ":lock:transactionalMessageCompensationTask";

    public static final String CLEAR_SEND_RECORD_TASK_LOCK_KEY = ":lock:messageRecordClearTask";

    public static final String MESSAGE_GC_TASK_LOCK_KEY = ":lock:messageGarbageCollectTask";

    public static final String BIZ_MODULE_HEADER_NAME = "x-biz-module";

    public static final String BIZ_KEY_HEADER_NAME = "x-biz-key";

    public static final String TRACE_HEADER_NAME = "x-trace-id";

    public static final long DEFAULT_INIT_BACKOFF = 10L;
    public static final int DEFAULT_BACKOFF_FACTOR = 2;
    public static final int DEFAULT_MAX_RETRY_TIMES = 5;
    public static final int RETRY_LIMIT = 100;

}

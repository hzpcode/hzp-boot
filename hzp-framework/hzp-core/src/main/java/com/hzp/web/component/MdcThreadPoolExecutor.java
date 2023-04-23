package com.hzp.web.component;

import org.slf4j.MDC;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * 封装MDC传递线程池，亦可以传递ThreadLocalHelper中的参数
 *
 * @author Yu
 * @date 2019/09/23 22:37
 **/
public class MdcThreadPoolExecutor extends ThreadPoolExecutor {

    private final boolean useFixedContext;
    private final Map<String, String> fixedContext;

    /**
     * 继承父线程 mdcContext
     */
    public static MdcThreadPoolExecutor newWithInheritedMdc(int corePoolSize, int maximumPoolSize,
                                                            long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,
                                                            ThreadFactory threadFactory) {
        return new MdcThreadPoolExecutor(null, corePoolSize, maximumPoolSize, keepAliveTime, unit,
                workQueue, threadFactory);
    }

    /**
     * 新建 mdcContext
     */
    public static MdcThreadPoolExecutor newWithFixedMdc(Map<String, String> fixedContext,
                                                        int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                                        BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        return new MdcThreadPoolExecutor(fixedContext, corePoolSize, maximumPoolSize, keepAliveTime,
                unit, workQueue, threadFactory);
    }

    private MdcThreadPoolExecutor(Map<String, String> fixedContext, int corePoolSize,
                                  int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,
                                  ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.fixedContext = fixedContext;
        this.useFixedContext = Objects.nonNull(fixedContext);
    }

    private Map<String, String> getContextForTask() {
        return useFixedContext ? fixedContext : MDC.getCopyOfContextMap();
    }


    @Override
    public void execute(Runnable command) {
        super.execute(wrapExecute(command, getContextForTask(), ThreadLocalHelper.getAll(), ThreadLocalHelper.getCopyThreadLocalMap()));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(wrapSubmit(task, getContextForTask(), ThreadLocalHelper.getAll(), ThreadLocalHelper.getCopyThreadLocalMap()));
    }

    private <T> Callable<T> wrapSubmit(Callable<T> task, final Map<String, String> context,
                                       final Map<String, Object> threadLocalContext,
                                       final Map<ThreadLocal<?>, ?> threadLocalMap) {
        return new MdcCallable<>(task, context, threadLocalContext, threadLocalMap);
    }

    private Runnable wrapExecute(final Runnable runnable, final Map<String, String> context,
                                 final Map<String, Object> threadLocalContext,
                                 final Map<ThreadLocal<?>, ?> threadLocalMap) {
        return new MdcRunnable(runnable, context, threadLocalContext, threadLocalMap);
    }
}


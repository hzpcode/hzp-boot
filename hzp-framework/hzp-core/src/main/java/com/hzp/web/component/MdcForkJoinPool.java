package com.hzp.web.component;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 支持Mdc的forkJoinPool
 * @link https://stackoverflow.com/questions/36026402/how-to-use-mdc-with-forkjoinpool
 *
 * @author Yu
 * @date 2021/07/18 15:31
 **/
public class MdcForkJoinPool extends ForkJoinPool {
    /**
     * Creates a new MdcForkJoinPool.
     *
     * @param parallelism the parallelism level. For default value, use {@link Runtime#availableProcessors}.
     * @param factory     the factory for creating new threads. For default value, use
     *                    {@link #defaultForkJoinWorkerThreadFactory}.
     * @param handler     the handler for internal worker threads that terminate due to unrecoverable errors encountered
     *                    while executing tasks. For default value, use {@code null}.
     * @param asyncMode   if true, establishes local first-in-first-out scheduling mode for forked tasks that are never
     *                    joined. This mode may be more appropriate than default locally stack-based mode in applications
     *                    in which worker threads only process event-style asynchronous tasks. For default value, use
     *                    {@code false}.
     * @throws IllegalArgumentException if parallelism less than or equal to zero, or greater than implementation limit
     * @throws NullPointerException     if the factory is null
     * @throws SecurityException        if a security manager exists and the caller is not permitted to modify threads
     *                                  because it does not hold
     *                                  {@link RuntimePermission}{@code ("modifyThread")}
     */
    public MdcForkJoinPool(int parallelism, ForkJoinWorkerThreadFactory factory, Thread.UncaughtExceptionHandler handler,
                           boolean asyncMode) {
        super(parallelism, factory, handler, asyncMode);
    }

    public MdcForkJoinPool(int parallelism) {
        this(parallelism, defaultForkJoinWorkerThreadFactory, null, false);
    }

    public MdcForkJoinPool() {
        super();
    }

    @Override
    public void execute(ForkJoinTask<?> task) {
        super.execute(wrap(task, MDC.getCopyOfContextMap(), ThreadLocalHelper.getAll(), ThreadLocalHelper.getCopyThreadLocalMap()));
    }

    @Override
    public void execute(Runnable task) {
        super.execute(new MdcRunnable(task, MDC.getCopyOfContextMap(), ThreadLocalHelper.getAll(), ThreadLocalHelper.getCopyThreadLocalMap()));
    }

    private <T> ForkJoinTask<T> wrap(ForkJoinTask<T> task, Map<String, String> newContext,
                                     Map<String, Object> threadLocalContext, Map<ThreadLocal<?>, ?> threadLocalMap) {
        return new ForkJoinTask<T>() {
            private static final long serialVersionUID = 1L;
            /**
             * If non-null, overrides the value returned by the underlying task.
             */
            private final AtomicReference<T> override = new AtomicReference<>();

            @Override
            public T getRawResult() {
                T result = override.get();
                if (result != null) {
                    return result;
                }
                return task.getRawResult();
            }

            @Override
            protected void setRawResult(T value) {
                override.set(value);
            }

            @Override
            protected boolean exec() {
                Map<String, String> previous = MDC.getCopyOfContextMap();
                if (newContext == null) {
                    MDC.clear();
                } else {
                    MDC.setContextMap(newContext);
                }

                for (Map.Entry<ThreadLocal<?>, ?> entry : threadLocalMap.entrySet()) {
                    ThreadLocal key = entry.getKey();
                    key.set(entry.getValue());
                }

                ThreadLocalHelper.putAll(threadLocalContext);

                try {
                    task.invoke();
                    return true;
                }finally {
                    if (previous == null) {
                        MDC.clear();
                    } else {
                        MDC.setContextMap(previous);
                    }

                    ThreadLocalHelper.clear();
                    threadLocalMap.keySet().forEach(ThreadLocal::remove);
                }
            }
        };
    }

}

package com.hzp.web.component;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * ThreadLocal清理注册
 * <p>注意：仅支持从http入口进入的线程自动清理，定时任务不支持</p>
 *
 * @author yxy
 * @date 2019/10/26 22:10
 **/

public class ThreadLocalCleaner {

    private ThreadLocalCleaner() { }

    private static final List<ThreadLocal<?>> CONTAINER = Lists.newArrayList();

    public static void register(ThreadLocal<?>... threadLocal) {
        if(ArrayUtils.isNotEmpty(threadLocal)) {
            CONTAINER.addAll(Arrays.asList(threadLocal));
        }
    }

    public static void register(List<ThreadLocal<?>> threadLocal) {
        if(!CollectionUtils.isEmpty(threadLocal)) {
            CONTAINER.addAll(threadLocal);
        }
    }

    public static void clear() {
        CONTAINER.forEach(ThreadLocal::remove);
    }

}

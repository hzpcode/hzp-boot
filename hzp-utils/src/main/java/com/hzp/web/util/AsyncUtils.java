package com.hzp.web.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 异步工具类
 *
 * @author yxy
 * @date 2020/08/04 11:26
 **/

public abstract class AsyncUtils {

    private AsyncUtils() { }

    public static  <T> T getValueByFuture(Future<T> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}

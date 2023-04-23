package com.hzp.web.component;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 *  ThreadLocal 工具类,通过在ThreadLocal存储map信息,来实现在ThreadLocal中维护多个信息。，为ThreadLocal统一管理，方便异步线程池复制
 *  1.支持线程池传递
 *  2.支持threadLocal自动清理
 *
 *  @author Yu
 *  @date 2020/9/10 23:19
 */
@SuppressWarnings("unchecked")
public final class ThreadLocalHelper {

    private ThreadLocalHelper() { }

    private static final List<ThreadLocal<?>> THREAD_LOCALS = Lists.newArrayList();
    private static final ThreadLocal<Map<String, Object>> CONTAINER = ThreadLocal.withInitial(HashMap::new);

    static {
        ThreadLocalCleaner.register(CONTAINER);
        ThreadLocalCleaner.register(THREAD_LOCALS);
    }

    /**
     * @return threadLocal中的全部值
     */
    public static Map<String, Object> getAll() {
        return new HashMap<>(CONTAINER.get());
    }

    /**
     * 将ThreadLocal纳入ThreadLocalHelper管理
     *
     * @param threadLocalContext threadLocal对象
     */
    public static void register(ThreadLocal<?> threadLocalContext) {
        THREAD_LOCALS.add(threadLocalContext);
    }

    /**
     * 获取threadLocal键值集合
     *
     * @return  threadLocal键值集合
     */
    public static Map<ThreadLocal<?>, ?> getCopyThreadLocalMap() {
        return THREAD_LOCALS.stream().collect(Collectors.toMap(o -> o, ThreadLocal::get));
    }

    /**
     * 设置一个值到ThreadLocal
     * 注意：最好加上业务前缀保证key不会重复！
     *
     * @param key   键
     * @param value 值
     * @param <T>   值的类型
     * @return 被放入的值
     * @see Map#put(Object, Object)
     */
    public static <T> T put(String key, T value) {
        CONTAINER.get().put(key, value);
        return value;
    }

    /**
     * 设置所有制到ThreadLocal
     * 注意：最好加上业务前缀保证key不会重复！
     *
     * @param kv 键值
     */
    public static void putAll(Map<String, Object> kv) {
        CONTAINER.get().putAll(kv);
    }

    /**
     * 删除参数对应的值
     *
     * @param key
     * @see Map#remove(Object)
     */
    public static void remove(String key) {
        CONTAINER.get().remove(key);
    }

    /**
     * 清空ThreadLocal
     *
     * @see Map#clear()
     */
    public static void clear() {
        CONTAINER.remove();
    }

    /**
     * 从ThreadLocal中获取值
     *
     * @param key 键
     * @param <T> 值泛型
     * @return 值, 不存在则返回null, 如果类型与泛型不一致, 可能抛出{@link ClassCastException}
     * @see Map#get(Object)
     * @see ClassCastException
     */
    public static <T> T get(String key) {
        return ((T) CONTAINER.get().get(key));
    }

    /**
     * 从ThreadLocal中获取值,并指定一个当值不存在的提供者
     *
     * @see Supplier
     */
    public static <T> T get(String key, Supplier<T> supplierOnNull) {
        return ((T) CONTAINER.get().computeIfAbsent(key, k -> supplierOnNull.get()));
    }

    /**
     * 获取一个值后然后删除掉
     *
     * @param key 键
     * @param <T> 值类型
     * @return 值, 不存在则返回null
     * @see this#get(String)
     * @see this#remove(String)
     */
    public static <T> T getAndRemove(String key) {
        try {
            return get(key);
        } finally {
            remove(key);
        }
    }

}

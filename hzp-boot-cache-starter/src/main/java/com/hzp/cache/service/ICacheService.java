package com.hzp.cache.service;

import com.hzp.cache.constant.ICacheKeyEnum;
import com.hzp.web.exception.BizException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Optional;

/**
 * 缓存接口
 *
 * @author wangchun
 * @date 2022/4/24
 */
public interface ICacheService {

    CacheManager getCacheManager();

    default Cache getCache(ICacheKeyEnum cacheName) {
        return Optional.ofNullable(getCacheManager().getCache(cacheName.key()))
                .orElseThrow(() -> new BizException(String.format("未初始化%s缓存", cacheName.key())));
    }

    default <T> T get(ICacheKeyEnum cacheName, Object key, Class<T> type) {
        return getCache(cacheName).get(key, type);
    }

    default void put(ICacheKeyEnum cacheName, Object key, Object value) {
        getCache(cacheName).put(key, value);
    }

    default void putIfAbsent(ICacheKeyEnum cacheName, Object key, Object value) {
        getCache(cacheName).putIfAbsent(key, value);
    }

    default void evict(ICacheKeyEnum cacheName, Object key) {
        getCache(cacheName).evict(key);
    }

    default boolean evictIfPresent(ICacheKeyEnum cacheName, Object key) {
        return getCache(cacheName).evictIfPresent(key);
    }

    default void clear(ICacheKeyEnum cacheName) {
        getCache(cacheName).clear();
    }
}
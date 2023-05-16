package com.hzp.cache.service;

import com.hzp.cache.config.MultiCacheProperties;
import com.hzp.cache.constant.CacheConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author wangchun
 * @date 2020/7/3
 */
@Service
public class LocalCacheService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Resource(name = CacheConstant.CAFFEINE_CACHE_MANAGER)
    private final CacheManager cacheManager;
    private final MultiCacheProperties cacheProperties;

    public LocalCacheService(CacheManager cacheManager, MultiCacheProperties cacheProperties) {
        this.cacheManager = cacheManager;
        this.cacheProperties = cacheProperties;
    }

    /**
     * 获取所有缓存的名字
     */
    public Collection<String> getAllCacheNames() {
        return cacheManager.getCacheNames();
    }

    /** todo
     * 获取某个缓存的统计信息
     */
//    public LCacheStats getCacheStats(String cacheName) {
//        CaffeineCache cache = (CaffeineCache) cacheManager.getCache(cacheName);
//        if (cache == null) {
//            return null;
//        }
//        com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache = cache.getNativeCache();
//
//        LCacheStats stats = new LCacheStats(cacheName, nativeCache.stats());
//        stats.setSize(nativeCache.estimatedSize());
//
//        MultiCacheBucketProperties c = cacheProperties.findCacheByName(cacheName);
//        stats.setMaxSize(c.getMaxSize());
//        stats.setTtl(c.getTtlSeconds());
//        stats.setTtlDuration(c.getTtlDuration());
//        return stats;
//    }

    /**
     * 获取某个缓存真正存储数据的Map
     */
    public Map<Object, Object> getCacheMap(String cacheName) {
        CaffeineCache cache = (CaffeineCache) cacheManager.getCache(cacheName);

        if (cache == null) {
            return null;
        }

        return cache.getNativeCache().asMap();
    }

    /**
     * 清空某个指定名字的缓存
     */
    public boolean clearCache(String cacheName) {
        log.info("cache to be cleared: {}", cacheName);
        Optional<Cache> cache = Optional.ofNullable(cacheManager.getCache(cacheName));
        return cache.map(c -> {
            c.clear();
            return true;
        }).orElse(false);
    }

}
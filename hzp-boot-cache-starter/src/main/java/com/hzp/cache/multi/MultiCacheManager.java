package com.hzp.cache.multi;

import com.hzp.cache.config.MultiCacheProperties;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 多级缓存管理器
 *
 * @author Yu
 * @date 2020/06/01 23:18
 **/
public class MultiCacheManager implements CacheManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private MultiCacheProperties multiCacheProperties;

    private ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private CacheManager redisCacheManager;

    @Resource
    private CacheManager caffeineCacheManager;

    @Resource
    private RedissonClient redissonClient;

    public MultiCacheManager(MultiCacheProperties multiCacheProperties,
                             RedisTemplate<String, Object> redisTemplate) {

        this.multiCacheProperties = multiCacheProperties;
        this.redisTemplate = redisTemplate;
    }

    @Nullable
    @Override
    public Cache getCache(String name) {
        Cache cache = cacheMap.get(name);
        if (cache != null) {
            return cache;
        }

        cache = new MultiCache(name, (RedisCache) redisCacheManager.getCache(name), redisTemplate,
                redissonClient, (CaffeineCache) caffeineCacheManager.getCache(name), multiCacheProperties);

        Cache oldCache = cacheMap.putIfAbsent(name, cache);
        logger.debug("create multi cache instance, the cache name is : {}", name);
        return oldCache == null ? cache : oldCache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return caffeineCacheManager.getCacheNames();
    }

    public void clearFirstLevelCache(String cacheName, Object key) {
        Cache cache = cacheMap.get(cacheName);
        if (cache == null) {
            return;
        }

        MultiCache multiCache = (MultiCache) cache;
        multiCache.clearFirstLevelCache(key);
    }
}

package com.hzp.cache.multi;

import com.hzp.cache.config.MultiCacheProperties;
import com.hzp.cache.message.CacheRefreshMessage;
import com.hzp.cache.redis.MultiRedisCacheAdapter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.cache.support.NullValue;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 多级缓存
 *
 * @author Yu
 * @date 2020/06/01 23:05
 **/
public class MultiCache extends AbstractValueAdaptingCache {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String name;
    private MultiRedisCacheAdapter redisCache;
    private CaffeineCache caffeineCache;
    private RedisTemplate<String, Object> redisTemplate;
    private RedissonClient redissonClient;
    private String topic;
    private static final String LOCK_KEY = "MULTI_VALUE_LOADER:";

    public MultiCache(String name, RedisCache redisCache, RedisTemplate<String, Object> redisTemplate, RedissonClient redissonClient,
                      CaffeineCache caffeineCache, MultiCacheProperties multiCacheProperties) {

        super(multiCacheProperties.isAllowNullValue());
        this.name = name;
        this.caffeineCache = caffeineCache;
        this.redisCache = new MultiRedisCacheAdapter(redisCache);
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
        this.topic = multiCacheProperties.getClearTopic();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {

        Object value = lookup(key);
        if (Objects.nonNull(value) && value == NullValue.INSTANCE ) {
            if (isAllowNullValues()) {
                return null;
            }
            value = null;
        }

        if(value != null) {
            return (T) value;
        }

        RLock lock = redissonClient.getLock(LOCK_KEY + key);

        try {
            boolean result = lock.tryLock(2000L, 3000L, TimeUnit.MILLISECONDS);
            if (!result) {
                throw new RuntimeException("multi cache get and valueLoader try lock time out, the key is " + key);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("multi cache get and valueLoader try lock time out, the key is " + key);
        }

        try {
            value = lookup(key);
            if (Objects.nonNull(value) && value == NullValue.INSTANCE ) {
                if (isAllowNullValues()) {
                    return null;
                }
                value = null;
            }

            if(value != null) {
                return (T) value;
            }

            value = valueLoader.call();
            //TODO: duplicate call, clone maybe better
            Object storeValue = toStoreValue(valueLoader.call());
            put(key, storeValue);
            return (T) value;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(Object key, Object value) {
        if (!super.isAllowNullValues() && value == null) {
            this.evict(key);
            return;
        }

        redisCache.put(key, value);

        publishEvent(new CacheRefreshMessage(this.name, key));
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {

        ValueWrapper valueWrapper = redisCache.putIfAbsent(key, value);
        if (Objects.isNull(valueWrapper)) {
            publishEvent(new CacheRefreshMessage(this.name, key));
        }
        return toValueWrapper(valueWrapper.get());
    }

    @Override
    public void evict(Object key) {
        redisCache.evict(key);

        publishEvent(new CacheRefreshMessage(this.name, key));
    }

    @Override
    public void clear() {
        redisCache.clear();
        publishEvent(new CacheRefreshMessage(this.name, null));
    }

    @Override
    protected Object lookup(Object key) {
        Object value = caffeineCache.getNativeCache().getIfPresent(key);

        if(value != null) {
            log.debug("get cache from caffeine, the key is : {}", key);
            return value;
        }

        value = redisCache.search(key);

        if(value != null) {
            log.debug("get cache from redis and put in caffeine, the key is : {}", key);
            caffeineCache.put(key, value);
        }
        return value;
    }

    /**
     * 发布事件
     *
     * @param message payload
     */
    private void publishEvent(CacheRefreshMessage message) {
        redisTemplate.convertAndSend(topic, message);
    }

    /**
     * 清除一级缓存
     *
     * @param key 缓存键
     */
    public void clearFirstLevelCache(Object key) {
        log.debug("clear local cache, the key is : {}", key);
        if(key == null) {
            caffeineCache.clear();
        } else {
            caffeineCache.evict(key);
        }
    }
}

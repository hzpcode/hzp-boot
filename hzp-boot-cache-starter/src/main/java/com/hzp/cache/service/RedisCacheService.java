package com.hzp.cache.service;

import com.leyo.cache.constant.CacheConstant;
import com.leyo.cache.constant.IRedisCacheKeyPrefix;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wangchun
 * @date 2022/4/28
 */
public class RedisCacheService implements ICacheService {

    @Resource(name = CacheConstant.REDIS_CACHE_MANAGER)
    protected CacheManager cacheManager;
    @Resource
    protected RedisTemplate redisTemplate;

    /**
     * ==========string==========
     */

    public Object opsGet(IRedisCacheKeyPrefix prefix, String key) {
        return redisTemplate.opsForValue().get(prefix.opsKey(key));
    }

    public void opsSet(IRedisCacheKeyPrefix prefix, String key, Object value) {
        redisTemplate.opsForValue().set(prefix.opsKey(key), value, prefix.ttl(), TimeUnit.SECONDS);
    }

    public long opsGetExpire(IRedisCacheKeyPrefix prefix, String key) {
        return redisTemplate.opsForValue().getOperations().getExpire(prefix.opsKey(key));
    }

    /**
     * ==========hash==========
     */

    public Object opsHget(IRedisCacheKeyPrefix prefix, String key, Object hashKey) {
        return redisTemplate.opsForHash().get(prefix.opsKey(key), hashKey);
    }

    public Map<String, Object> opsEntries(IRedisCacheKeyPrefix prefix, String key) {
        return redisTemplate.opsForHash().entries(prefix.opsKey(key));
    }

    public void opsHput(IRedisCacheKeyPrefix prefix, String key, Object hashKey, Object value) {
        redisTemplate.opsForHash().put(prefix.opsKey(key), hashKey, value);
    }

    public void opsHputAll(IRedisCacheKeyPrefix prefix, String key, Map m) {
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(prefix.opsKey(key));
        hashOps.putAll(m);
        hashOps.expire(prefix.ttl(), TimeUnit.SECONDS);
    }

    public void opsDelete(IRedisCacheKeyPrefix prefix, String key) {
        redisTemplate.delete(prefix.opsKey(key));
    }

    @Override
    public CacheManager getCacheManager() {
        return cacheManager;
    }
}
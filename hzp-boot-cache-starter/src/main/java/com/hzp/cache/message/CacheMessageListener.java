package com.hzp.cache.message;

import com.hzp.cache.multi.MultiCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 缓存刷新监听
 *
 * @author Yu
 * @date 2020/06/01 23:15
 **/
public class CacheMessageListener implements MessageListener {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private RedisTemplate<String, Object> redisTemplate;
    private MultiCacheManager multiCacheManager;

    public CacheMessageListener(RedisTemplate<String, Object> redisTemplate, MultiCacheManager multiCacheManager) {
        this.redisTemplate = redisTemplate;
        this.multiCacheManager = multiCacheManager;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CacheRefreshMessage cacheRefreshMessage = (CacheRefreshMessage) redisTemplate.getValueSerializer().deserialize(message.getBody());
        log.debug("recevice a redis topic message, clear local cache, cacheName : {}, key : {}", cacheRefreshMessage.getCacheName(), cacheRefreshMessage.getKey());
        multiCacheManager.clearFirstLevelCache(cacheRefreshMessage.getCacheName(), cacheRefreshMessage.getKey());
    }
}

package com.hzp.cache.message;

import java.io.Serializable;

/**
 * 缓存刷新消息
 *
 * @author Yu
 * @date 2020/06/01 23:11
 **/
public class CacheRefreshMessage implements Serializable {

    /**
     * 缓存集合名称
     */
    private String cacheName;

    /**
     * 缓存key
     */
    private Object key;

    public CacheRefreshMessage() {
    }

    public CacheRefreshMessage(String cacheName, Object key) {
        super();
        this.cacheName = cacheName;
        this.key = key;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }
}

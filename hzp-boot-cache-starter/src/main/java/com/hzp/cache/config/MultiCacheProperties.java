package com.hzp.cache.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedList;
import java.util.List;


@ConfigurationProperties(prefix = "leyo.boot.cache")
public class MultiCacheProperties {
    private boolean annotationEnabled = false;
    /**
     * 需要登录的profile，多个的话使用“,”分割
     */
    private String securityEnv = "prod";
    private String clearTopic = "multi:cache:clear:topic";

    private boolean allowNullValue = true;
    private long defaultMaxSize = 65535;
    private long defaultTtlSeconds = 10;
    /**
     * 私有空间前缀
     */
    private String priCachePrefix = "";
    private List<MultiCacheBucketProperties> selfCaches = new LinkedList<>();
    private List<MultiCacheBucketProperties> pubCaches = new LinkedList<>();

    public MultiCacheBucketProperties findCacheByName(String cacheName) {
        if (cacheName.startsWith(priCachePrefix)) {
            for (MultiCacheBucketProperties c : selfCaches) {
                if ((priCachePrefix + c.getName()).equals(cacheName)) {
                    return c;
                }
            }
        } else {
            for (MultiCacheBucketProperties c : pubCaches) {
                if (c.getName().equals(cacheName)) {
                    return c;
                }
            }
        }
        return null;
    }

    public boolean isAnnotationEnabled() {
        return annotationEnabled;
    }

    public void setAnnotationEnabled(boolean annotationEnabled) {
        this.annotationEnabled = annotationEnabled;
    }

    public String getSecurityEnv() {
        return securityEnv;
    }

    public void setSecurityEnv(String securityEnv) {
        this.securityEnv = securityEnv;
    }

    public String getClearTopic() {
        return clearTopic;
    }

    public void setClearTopic(String clearTopic) {
        this.clearTopic = clearTopic;
    }

    public boolean isAllowNullValue() {
        return allowNullValue;
    }

    public void setAllowNullValue(boolean allowNullValue) {
        this.allowNullValue = allowNullValue;
    }

    public long getDefaultMaxSize() {
        return defaultMaxSize;
    }

    public void setDefaultMaxSize(long defaultMaxSize) {
        this.defaultMaxSize = defaultMaxSize;
    }

    public long getDefaultTtlSeconds() {
        return defaultTtlSeconds;
    }

    public void setDefaultTtlSeconds(long defaultTtlSeconds) {
        this.defaultTtlSeconds = defaultTtlSeconds;
    }

    public String getPriCachePrefix() {
        return priCachePrefix;
    }

    public void setPriCachePrefix(String priCachePrefix) {
        this.priCachePrefix = priCachePrefix;
    }

    public List<MultiCacheBucketProperties> getSelfCaches() {
        return selfCaches;
    }

    public void setSelfCaches(List<MultiCacheBucketProperties> selfCaches) {
        this.selfCaches = selfCaches;
    }

    public List<MultiCacheBucketProperties> getPubCaches() {
        return pubCaches;
    }

    public void setPubCaches(List<MultiCacheBucketProperties> pubCaches) {
        this.pubCaches = pubCaches;
    }
}

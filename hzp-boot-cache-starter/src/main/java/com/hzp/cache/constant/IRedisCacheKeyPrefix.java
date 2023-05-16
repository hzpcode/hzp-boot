package com.hzp.cache.constant;

import org.springframework.data.redis.connection.DataType;

/**
 * @author wangchun
 * @date 2022/4/28
 */
public interface IRedisCacheKeyPrefix extends ICacheKeyEnum {
    String SEPARATOR = "::";

    long ttl();

    DataType type();

    default String opsKey(String key) {
        return key() + SEPARATOR + key;
    }
}
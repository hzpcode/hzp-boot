package com.hzp.cache.config;

import java.time.Duration;
import java.util.Objects;

/**
 *  缓存桶
 *
 *  @author Yu
 *  @date 2020/5/28 20:55
 */
public class MultiCacheBucketProperties {
	private String name;
	private long maxSize = 0L;
	private long ttlSeconds = 0L;
	private Duration ttlDuration;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}

	public long getTtlSeconds() {
		if(ttlSeconds == 0L && Objects.nonNull(ttlDuration)){
			return ttlDuration.getSeconds();
		}
		return ttlSeconds;
	}

	public void setTtlSeconds(long ttlSeconds) {
		this.ttlSeconds = ttlSeconds;
	}

	public Duration getTtlDuration() {
		return ttlDuration;
	}

	public void setTtlDuration(Duration ttlDuration) {
		this.ttlDuration = ttlDuration;
	}

	@Override
	public String toString() {
		return "CacheProp{" +
				"name='" + name + '\'' +
				", maxSize=" + maxSize +
				", ttlSeconds=" + ttlSeconds +
				'}';
	}

	public static void main(String[] args) {
		Duration d = Duration.parse("PT10s");
		System.out.println(d);
	}
}

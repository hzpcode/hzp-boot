package com.hzp.cache.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hzp.cache.constant.CacheConstant;
import com.hzp.cache.message.CacheMessageListener;
import com.hzp.cache.multi.MultiCacheManager;
import com.hzp.web.convert.DateDeserializer;
import com.hzp.web.convert.DateSerializer;
import com.hzp.web.convert.LocalDateDeserializer;
import com.hzp.web.convert.LocalDateTimeDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 缓存自动配置类
 *
 * @author yxy
 * @date 2020/05/27 18:23
 **/

@EnableCaching
@Configuration
@EnableConfigurationProperties(MultiCacheProperties.class)
public class MultiCacheAutoConfiguration {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String dateTimeFormat;

    @Bean
    @ConditionalOnProperty("spring.redis.host")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        ObjectMapper om = new ObjectMapper();

        // 禁止遇到空原始类型时抛出异常，用默认值代替。
        om.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
//        om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 禁止遇到未知（新）属性时报错，支持兼容扩展
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //使用{com.xxx.className, {fieldName1: fieldValue1, ...}}表示类元数据
        //om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        //使用{@class:xxx, fieldName1: fieldValue1, ...}表示类元信息
        om.activateDefaultTyping(om.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        //注册null值标记类，防止缓存击穿
        //org.springframework.data.redis.cache.RedisCache.BINARY_NULL_VALUE序列化时JDK二进制序列化，会乱码
        GenericJackson2JsonRedisSerializer.registerNullValueSerializer(om, null);

        om.registerModule(dateSimpleModule());
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(om);

        RedisSerializer<String> stringSerializer = new StringRedisSerializer();

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.setDefaultSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    private SimpleModule dateSimpleModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        simpleModule.addSerializer(Date.class, new DateSerializer());
        simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        simpleModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        simpleModule.addDeserializer(Date.class, new DateDeserializer());
        return simpleModule;
    }

    @Bean(CacheConstant.REDIS_CACHE_MANAGER)
    @ConditionalOnProperty("spring.redis.host")
    public CacheManager redisCacheManager(MultiCacheProperties properties, RedisTemplate redisTemplate) {
        // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        // 设置缓存的默认过期时间，也是使用Duration设置
        config = config.entryTtl(Duration.ofSeconds(properties.getDefaultTtlSeconds()))
                //不能使用该配置,默认会覆盖掉所有cacheManager的cacheName，
//                .prefixKeysWith(properties.getCachePrefix())
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getKeySerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()));
                // 不缓存空值
                //org.springframework.data.redis.cache.RedisCache.BINARY_NULL_VALUE序列化时JDK二进制序列化，会乱码
//                .disableCachingNullValues();
        // 设置一个初始化的缓存空间set集合
        Set<String> cacheNames = new HashSet<>();
        // 对每个缓存空间应用不同的配置
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();

        // 私有缓存，需要加前缀
        for (MultiCacheBucketProperties p : properties.getSelfCaches()) {
            String cacheName = properties.getPriCachePrefix() + p.getName();
            cacheNames.add(cacheName);
            //没有配置ttl，使用defaultTtlSeconds
            if (p.getTtlSeconds() == 0) {
                configMap.put(cacheName, config.entryTtl(Duration.ofSeconds(properties.getDefaultTtlSeconds())));
            } else {
                configMap.put(cacheName, config.entryTtl(Duration.ofSeconds(p.getTtlSeconds())));
            }
        }

        //cacheManager建议不要使用公共缓存，因为会缓存@class信息，必须保证包名+类名一模一样，或者重写RedisCache类和配置值序列化器
        // 公共缓存
        for (MultiCacheBucketProperties p : properties.getPubCaches()) {

            String cacheName = p.getName();
            cacheNames.add(cacheName);
            //没有配置ttl，使用defaultTtlSeconds
            if (p.getTtlSeconds() == 0) {
                configMap.put(cacheName, config.entryTtl(Duration.ofSeconds(properties.getDefaultTtlSeconds())));
            } else {
                configMap.put(cacheName, config.entryTtl(Duration.ofSeconds(p.getTtlSeconds())));
            }
        }

        // 使用自定义的缓存配置初始化一个cacheManager
        return RedisCacheManager.builder(redisTemplate.getConnectionFactory())
                // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
                .initialCacheNames(cacheNames)
                .cacheDefaults(config)
                .withInitialCacheConfigurations(configMap)
                .build();
    }

    @Primary
    @Bean(CacheConstant.CAFFEINE_CACHE_MANAGER)
    public CacheManager caffeineCacheManager(MultiCacheProperties properties) {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        List<CaffeineCache> caches = new ArrayList<>();
        addCache2List(caches, properties.getSelfCaches(), properties.getDefaultMaxSize(), properties.getDefaultTtlSeconds(), properties.getPriCachePrefix());
        addCache2List(caches, properties.getPubCaches(), properties.getDefaultMaxSize(), properties.getDefaultTtlSeconds(), "");

        log.info("local cache list: {}", properties.getSelfCaches());
        cacheManager.setCaches(caches);

        return cacheManager;
    }

    private void addCache2List(List<CaffeineCache> caches, List<MultiCacheBucketProperties> cacheBucketProperties,
                               long defaultMaxSize, long defaultTtlSeconds, String keyPrefix) {
        for(MultiCacheBucketProperties p : cacheBucketProperties){
            if(p.getMaxSize() == 0L){
                p.setMaxSize(defaultMaxSize);
            }
            if(p.getTtlSeconds() == 0L){
                p.setTtlSeconds(defaultTtlSeconds);
            }

            caches.add(createLocalCache(p, keyPrefix));
        }
    }

    private CaffeineCache createLocalCache(MultiCacheBucketProperties p, String keyPrefix){
        return new CaffeineCache(keyPrefix + p.getName(),
                Caffeine.newBuilder()
                        .expireAfterWrite(p.getTtlSeconds(), TimeUnit.SECONDS)
                        .maximumSize(p.getMaxSize())
                        .recordStats()
                        .build());
    }

    @Configuration
    @AutoConfigureAfter(MultiCacheAutoConfiguration.class)
    @ConditionalOnProperty("spring.redis.host")
    public static class MultiCacheManagerAutoConfiguration {

        @Bean(CacheConstant.MULTI_CACHE_MANAGER)
        @ConditionalOnMissingBean
        public CacheManager multiCacheManager(RedisTemplate<String, Object> redisTemplate,
                                              MultiCacheProperties properties) {

            return new MultiCacheManager(properties, redisTemplate);
        }

        @Bean
        @ConditionalOnMissingBean
        public RedisMessageListenerContainer redisMessageListenerContainer(RedisTemplate<String, Object> redisTemplate,
                                                                           MultiCacheProperties multiCacheProperties) {

            RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
            redisMessageListenerContainer.setConnectionFactory(redisTemplate.getConnectionFactory());
            CacheMessageListener cacheMessageListener = new CacheMessageListener(redisTemplate, (MultiCacheManager) multiCacheManager(redisTemplate, multiCacheProperties));
            redisMessageListenerContainer.addMessageListener(cacheMessageListener, new ChannelTopic(multiCacheProperties.getClearTopic()));
            return redisMessageListenerContainer;
        }

    }

}

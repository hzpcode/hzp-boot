package com.hzp.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.hzp.web.component.ThreadLocalInterceptor;
import com.hzp.web.convert.*;
import com.hzp.web.interceptor.ControllerMethodInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Web配置
 *
 * @author yxy
 * @date 2019/10/15 20:40
 **/

@Slf4j
@EnableConfigurationProperties(HzpBootProperties.class)
@RequiredArgsConstructor
public class WebAppConfiguration implements WebMvcConfigurer {


    private final HzpBootProperties hzpBootProperties;

    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String dateTimeFormat;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new String2DateConverter());
        registry.addConverter(new String2LocalDateTimeConverter());
        registry.addConverter(new String2LocalDateConverter());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ThreadLocalInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //TODO: 开发环境要修改origins为指定ip或者域名
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("*")
                .maxAge(3600L)
                .allowedHeaders("*");
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {

        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.deserializers(
                    new DateDeserializer(),
                    new LocalDateTimeDeserializer(),
                    new LocalDateDeserializer(),
                    new StringTrimJacksonDeserializer());

            jacksonObjectMapperBuilder.serializerByType(
                    LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
            jacksonObjectMapperBuilder.serializerByType(
                    LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            jacksonObjectMapperBuilder.serializerByType(
                    String.class, new StringTrimSerializer());
        };
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper,
                                                                                   NullBeanSerializerModifier nullBeanSerializerModifier) {
        if (hzpBootProperties.isJsonNullProcess()) {
            SerializerFactory serializerFactory = objectMapper.getSerializerFactory().withSerializerModifier(nullBeanSerializerModifier);
            objectMapper.setSerializerFactory(serializerFactory);
        }
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    @Bean
    public ControllerMethodInterceptor controllerMethodInterceptor() {
        log.info(">>>>>>>>>注入ControllerMethodInterceptor");
        return new ControllerMethodInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {

        return restTemplateBuilder
                .setConnectTimeout(Duration.ofMinutes(1))
                .setReadTimeout(Duration.ofMinutes(2))
                .build();
//        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean
    public NullBeanSerializerModifier nullBeanSerializerModifier() {
        return new NullBeanSerializerModifier();
    }

}

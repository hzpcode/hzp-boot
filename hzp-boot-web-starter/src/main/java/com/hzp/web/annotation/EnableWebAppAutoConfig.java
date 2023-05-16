package com.hzp.web.annotation;

import com.hzp.web.config.WebAppConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.*;

/**
 * 自定义Web应用配置类
 *
 * @author yxy
 * @date 2019/08/14 15:02
 **/

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ImportAutoConfiguration(WebAppConfiguration.class)
public @interface EnableWebAppAutoConfig {


}

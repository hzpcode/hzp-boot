package com.hzp.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * boot配置属性类
 *
 * @author Yu
 * @date 2019/12/22 00:39
 **/

@Data
@ConfigurationProperties(prefix = "hzp.boot")
public class HzpBootProperties {

    /**返回值Null处理器开关*/
    private boolean jsonNullProcess = false;

}

package com.hzp.web.convert;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 *  日期转换组件，解决Get、Post的url?date=xxx日期参数问题
 *
 *  @author yxy
 *  @date 2019/8/14 13:56
 */

public class String2LocalDateTimeConverter implements Converter<String, LocalDateTime> {

    /**
     * @see Converter#convert(Object)
     */
    @Override
    public LocalDateTime convert(String originDate) {
        Date parse = String2DateCommonConvert.parse(originDate);
        return Objects.isNull(parse) ? null : LocalDateTime.ofInstant(parse.toInstant(), ZoneId.systemDefault());
    }

}

package com.hzp.web.convert;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 *  日期转换组件，解决Get、Post的url?date=xxx日期参数问题
 *
 *  @author yxy
 *  @date 2019/8/14 13:56
 */

public class String2LocalDateConverter implements Converter<String, LocalDate> {

    /**
     * @see Converter#convert(Object)
     */
    @Override
    public LocalDate convert(String originDate) {
        Date parse = String2DateCommonConvert.parse(originDate);
        return Objects.isNull(parse) ? null : parse.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}

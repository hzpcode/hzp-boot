package com.hzp.web.convert;

import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 *  日期转换组件，解决Get、Post的url?date=xxx日期参数问题
 *
 *  @author yxy
 *  @date 2019/8/14 13:56
 */

public class String2DateConverter implements Converter<String, Date> {

    /**
     * @see org.springframework.core.convert.converter.Converter#convert(Object)
     */
    @Override
    public Date convert(String originDate) {
        return String2DateCommonConvert.parse(originDate);
    }

}

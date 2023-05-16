package com.hzp.web.convert;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Date;

/**
 * Jackson反序列化日期转换器，解决@RequestBody日期转换问题
 *
 * @author yxy
 * @date 2019/08/14 14:25
 **/
public class DateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String originDate = p.getText();

        if (StringUtils.isBlank(originDate)) {
            return null;
        }
        return String2DateCommonConvert.parse(originDate.trim());
    }

    @Override
    public Class<?> handledType() {
        return Date.class;
    }
}

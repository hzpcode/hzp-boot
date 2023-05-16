package com.hzp.web.convert;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 * Jackson反序列化日期转换器，解决@RequestBody日期转换问题
 *
 * @author yxy
 * @date 2019/08/14 14:25
 **/
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String originDate = p.getText();
        Date parse = String2DateCommonConvert.parse(originDate.trim());
        return Objects.isNull(parse) ? null : parse.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Override
    public Class<?> handledType() {
        return LocalDate.class;
    }
}

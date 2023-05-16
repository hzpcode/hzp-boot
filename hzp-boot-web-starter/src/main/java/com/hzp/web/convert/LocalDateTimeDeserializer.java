package com.hzp.web.convert;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 * Jackson反序列化日期转换器，解决@RequestBody日期转换问题
 *
 * @author yxy
 * @date 2019/08/14 14:25
 **/
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String originDate = p.getText();
        Date parse = String2DateCommonConvert.parse(originDate.trim());
        return Objects.isNull(parse) ? null : LocalDateTime.ofInstant(parse.toInstant(), ZoneId.systemDefault());
    }

    @Override
    public Class<?> handledType() {
        return LocalDateTime.class;
    }
}

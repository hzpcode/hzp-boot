package com.hzp.web.convert;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Objects;

/**
 * Jackson去除空格转换器，解决@RequestBody去空格问题
 *
 * @author yxy
 * @date 2019/08/14 14:25
 **/
public class StringTrimJacksonDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String originStr = p.getText();

        if(Objects.isNull(originStr)) {
            return null;
        }
        return originStr.trim();
    }

    @Override
    public Class<?> handledType() {
        return String.class;
    }
}

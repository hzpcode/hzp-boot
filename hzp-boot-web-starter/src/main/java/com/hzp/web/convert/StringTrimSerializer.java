package com.hzp.web.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Objects;

/**
 * 字符串去除前后空格序列化器
 *
 * @author Yu
 * @date 2020/02/10 22:28
 **/
public class StringTrimSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String str, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (Objects.isNull(str)) {
            jsonGenerator.writeString(str);
        }
        jsonGenerator.writeString(str.trim());
    }
}

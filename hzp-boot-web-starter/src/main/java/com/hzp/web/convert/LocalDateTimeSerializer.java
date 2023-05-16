package com.hzp.web.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hzp.sys.util.SDateUtils;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 日期序列化
 *
 * @author Yu
 * @date 2020/02/10 22:28
 **/
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(SDateUtils.localDateTimeToString(localDateTime, "yyyy-MM-dd HH:mm:ss"));
    }
}

package com.hzp.web.convert;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Objects;

/**
 * Jackson全角括号转半角转换器
 *
 *  @author yxy
 *  @date 2020/11/25 17:44
 */
public class ReplaceBracketsDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String originStr = p.getText();

        if(Objects.isNull(originStr)) {
            return null;
        }
        return originStr.trim()
                .replaceAll("【","[")
                .replaceAll("】","]")
                .replaceAll(" "," ")
                .replaceAll("！","!")
                .replaceAll("，",",")
                .replaceAll("。",".")
                .replaceAll("“","\"")
                .replaceAll("’","\'")
                .replaceAll("？","?")
                .replaceAll("：",":")
                .replaceAll("；",";")
                .replaceAll("……","^")
                .replaceAll("（", "(")
                .replaceAll("）", ")");
    }

    @Override
    public Class<?> handledType() {
        return String.class;
    }
}

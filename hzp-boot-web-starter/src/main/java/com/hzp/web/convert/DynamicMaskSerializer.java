package com.hzp.web.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.hzp.web.annotation.DynamicMask;
import com.hzp.web.constant.MaskTypeEnum;
import com.hzp.web.holder.MaskSetting;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * 动态脱敏序列化器
 *
 * @author yxy
 * @date 2020/08/24 10:42
 **/

@NoArgsConstructor
@AllArgsConstructor
public class DynamicMaskSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private int prefixLen = 3;

    private int suffixLen = 4;

    private String maskChar = "*";

    private String regex;

    public final static String template = "(?<=\\w{%s})\\w(?=\\w{%s})";

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (StringUtils.isBlank(value)){
            gen.writeString(value);
            return;
        }

        if (MaskSetting.isEnabled()) {
            if (StringUtils.isNotBlank(regex)) {
                gen.writeString(value.trim().replaceAll(regex, maskChar));
                return;
            }
            gen.writeString(value.trim().replaceAll(String.format(template, prefixLen, suffixLen), maskChar));
            return;
        }

        gen.writeString(value);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {

        // 为空直接跳过
        if (Objects.nonNull(property)) {
            // 非 String 类直接跳过
            if (Objects.equals(property.getType().getRawClass(), String.class)) {
                DynamicMask dynamicMask = property.getAnnotation(DynamicMask.class);
                if (Objects.isNull(dynamicMask)) {
                    dynamicMask = property.getContextAnnotation(DynamicMask.class);
                }

                if (Objects.nonNull(dynamicMask)) {
                    if (!dynamicMask.maskTypeEnum().equals(MaskTypeEnum.NULL)) {
                        return new DynamicMaskSerializer(dynamicMask.maskTypeEnum().prefixLen,
                                dynamicMask.maskTypeEnum().suffixLen,
                                dynamicMask.maskString(),
                                null);
                    }
                    return new DynamicMaskSerializer(dynamicMask.prefixLen(),
                            dynamicMask.suffixLen(),
                            dynamicMask.maskString(),
                            dynamicMask.regex());
                }
            }
            return prov.findValueSerializer(property.getType(), property);
        } else {
            return prov.findNullValueSerializer(null);
        }
    }

    @Override
    public Class<String> handledType() {
        return String.class;
    }

    public static String mask(String value, int prefixLen, int suffixLen, String maskChar) {
        return value.trim().replaceAll(String.format(template, prefixLen, suffixLen), maskChar);
    }
}

package com.hzp.sys.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Json相关工具类
 *
 * Created by XuJijun on 18-12-20.
 */
public final class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static volatile ObjectMapper objectMapper;

    /**
     * 获取一个ObjectMapper
     * @return 一个实例化了的ObjectMapper对象
     */
    public static ObjectMapper getMapper() {
        if(objectMapper == null){
            synchronized (JsonUtils.class){
                if(objectMapper == null){
                    objectMapper = new ObjectMapper();
                    JavaTimeModule timeModule = new JavaTimeModule();
                    timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(SDateUtils.FMT_DATE_TIME)));
                    timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(SDateUtils.FMT_DATE_STANDARD)));
                    objectMapper.registerModule(timeModule);
                }
            }
        }

        return objectMapper;
    }

    /**
     * 把一个对象转换成JSON字符串
     */
    public static String toJsonString(Object obj){
        try {
            return getMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 把一个对象转换成漂亮的JSON字符串
     */
    public static String toPrettyJsonString(Object obj){
        try {
            return getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 把一个json字符串转换为一个对象
     * @param json JSON字符串
     * @param type 目标对象的类型，比如：Map.class
     * @param <T> 目标类，比如：Map
     * @return 类T的一个对象
     */
    public static <T> T toObject(String json, Class<T> type) {
        T result =  null;
        try {
            result = getMapper().readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  result;
    }

    /**
     * 把一个对象类型的json字符串转换为一个JsonNode对象
     * @param jsonStr JSON字符串
     * @return 一个JsonNode对象
     */
    public static JsonNode toJsonNode(String jsonStr){
        JsonNode jsonNode = null;
        try {
            jsonNode = getMapper().readTree(jsonStr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return jsonNode;
    }

    /**
     * json字符串转List
     */
    /**
     * json字符串转List
     *
     * @param jsonArray JSON数组字符串
     * @param clazz     目标对象的类型，比如：Map.class
     * @param <T>       目标类，比如：Map
     * @return List数组
     */
    public static <T> List<T> json2List(String jsonArray, Class<T> clazz) {
        try {
            List<T> list = getMapper().readValue(jsonArray, new TypeReference<List<T>>() {});
            List<T> result = Lists.newArrayListWithCapacity(list.size());
            for (T t : list) {
                result.add(getMapper().convertValue(t, clazz));
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 把Map类型的json字符串转换为一个Map对象
     * @param jsonStr JSON字符串
     * @param keyType Map中的key类型的class
     * @param valueType Map中的value类型的class
     * @param <K> Map中的key类型
     * @param <V> Map中的value类型
     * @return 元素类型为<keyType, valueType>的一个Map
     */
    public static <K, V> Map<K, V> toMap(String jsonStr, Class<K> keyType, Class<V> valueType){
        Map<K, V> map = null;
        try {
            JavaType t = getMapper().getTypeFactory().constructParametricType(Map.class, keyType, valueType);
            map = getMapper().readValue(jsonStr, t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    /**
     * 把Map类型的json字符串转换为一个Map对象
     * @param jsonStr JSON字符串
     * @return 元素类型为<keyType, valueType>的一个Map
     */
    public static Map toMap(String jsonStr){
        Map<?, ?> map = null;
        try {
            map = getMapper().readValue(jsonStr, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    public static void main(String[] args){
        Map<Object, Object> map = new HashMap<>();
        map.put("abc", 123);
        map.put(888, "eight");

        String s = JsonUtils.toJsonString(map);
        System.out.println(s);
        String s1 = JsonUtils.toPrettyJsonString(map);
        System.out.println(s1);

        System.out.println(toObject(s, Map.class));
        System.out.println(toObject(s1, Map.class));
    }
}

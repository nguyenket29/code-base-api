package com.java.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class JsonUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public JsonUtil() {
    }

    public static <O> String toJson(O o) {
        try {
            return MAPPER.writeValueAsString(o);
        } catch (Exception var2) {
            return var2.getMessage();
        }
    }

    public static <T> T toObject(String jsonStr, final Class<T> clazz) {
        try {
            return MAPPER.readValue(jsonStr, clazz);
        } catch (Exception var3) {
            return null;
        }
    }

    public static <T> T toObject(String jsonStr, final TypeReference<T> reference) {
        try {
            return MAPPER.readValue(jsonStr, reference);
        } catch (Exception var3) {
            return null;
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap();
        return (t) -> {
            return map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
        };
    }

    static {
        MAPPER.setSerializationInclusion(Include.NON_NULL);
    }
}

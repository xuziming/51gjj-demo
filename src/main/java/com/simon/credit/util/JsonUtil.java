package com.simon.credit.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.NameFilter;

/**
 * JSON工具类
 */
public class JsonUtil {

    private static final ParserConfig PARSER_CONFIG = new ParserConfig();

    static {
        PARSER_CONFIG.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
    }

    private static final Map<PropertyNamingStrategy, NameFilter> NAME_FILTERS = new HashMap<PropertyNamingStrategy, NameFilter>();

    static {
        NAME_FILTERS.put(PropertyNamingStrategy.SnakeCase, new SnakeNameFilter());
        NAME_FILTERS.put(PropertyNamingStrategy.CamelCase, new CamelNameFilter());
        NAME_FILTERS.put(PropertyNamingStrategy.PascalCase, new PascalNameFilter());
        NAME_FILTERS.put(PropertyNamingStrategy.KebabCase, new KebabNameFilter());
    }

    public static class SnakeNameFilter implements NameFilter {
        public String process(Object object, String name, Object value) {
            return PropertyNamingStrategy.SnakeCase.translate(name);
        }

    }

    public static class CamelNameFilter implements NameFilter {
        public String process(Object object, String name, Object value) {
            return PropertyNamingStrategy.CamelCase.translate(name);
        }

    }

    public static class PascalNameFilter implements NameFilter {
        public String process(Object object, String name, Object value) {
            return PropertyNamingStrategy.PascalCase.translate(name);
        }

    }

    public static class KebabNameFilter implements NameFilter {
        public String process(Object object, String name, Object value) {
            return PropertyNamingStrategy.KebabCase.translate(name);
        }

    }

    /**
     * object serialize to string
     * 
     * @param object
     * @param strategy
     * @return
     */
    public static String toJSONString(Object object, PropertyNamingStrategy strategy) {
        if (object == null) {
            return null;
        }
        if (strategy == null) {
            return JSON.toJSONString(object);
        }
        NameFilter nameFilter = NAME_FILTERS.get(strategy);
        if (nameFilter == null) {
            return JSON.toJSONString(object);
        } else {
            return JSON.toJSONString(object, nameFilter);
        }
    }

}

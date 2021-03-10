package jforgame.admin.utils;


import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * json序列号工具（使用jackson）
 */
public class JsonUtil {


    private static TypeFactory typeFactory = TypeFactory.defaultInstance();

    private static final ObjectMapper MAPPER = new ObjectMapper().configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

    public static String object2String(Object object) {
        StringWriter writer = new StringWriter();
        try {
            MAPPER.writeValue(writer, object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return writer.toString();
    }

    public static String object2String(Object object, TypeReference<?> ref) {
        StringWriter writer = new StringWriter();
        try {
            MAPPER.typedWriter(ref).writeValue(writer, object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return writer.toString();
    }

    @SuppressWarnings("unchecked")
    public static <T> T cloneObject(Object object) {
        String json = object2String(object);
        return (T) string2Object(json, object.getClass());
    }

    @SuppressWarnings("unchecked")
    public static <T> T string2Object(String json, Class<T> clazz) {
        JavaType type = typeFactory.constructType(clazz);
        try {
            return (T) MAPPER.readValue(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String map2String(Map<?, ?> map) {
        StringWriter writer = new StringWriter();
        try {
            MAPPER.writeValue(writer, map);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return writer.toString();
    }

    public static Map<String, Object> string2Map(String json) {
        JavaType type = typeFactory.constructMapType(HashMap.class, String.class, Object.class);
        try {
            return MAPPER.readValue(json, type);
        } catch (Exception e) {
            return null;
        }
    }

    public static <K, V> Map<K, V> string2Map(String json, Class<K> keyClazz, Class<V> valueClazz) {
        JavaType type = typeFactory.constructMapType(HashMap.class, keyClazz, valueClazz);
        try {
            return MAPPER.readValue(json, type);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] string2Array(String json, Class<T> clazz) {
        JavaType type = ArrayType.construct(typeFactory.constructType(clazz));
        try {
            return (T[]) MAPPER.readValue(json, type);
        } catch (Exception e) {
            return null;
        }
    }

    public static <C extends Collection<E>, E> C string2Collection(String json, Class<C> collectionType, Class<E> elemType) {
        JavaType type = typeFactory.constructCollectionType(collectionType, elemType);
        try {
            return MAPPER.readValue(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}

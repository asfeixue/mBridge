package com.feixue.mbridge.scan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.feixue.mbridge.meta.domain.HttpProtocol;
import com.feixue.mbridge.meta.domain.HttpProtocolParam;
import com.feixue.mbridge.meta.domain.HttpProtocolPath;
import javassist.Modifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by zxxiao on 2017/6/15.
 */
public class ProtocolScanBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ProtocolScanBuilder.class);

    /**
     * 构建请求参数
     * @param paramClazz
     * @param required
     * @param paramName
     * @param protocol
     * @throws Exception
     */
    public static void buildRequestParam(Class paramClazz, boolean required, String paramName, HttpProtocol protocol) throws Exception {
        if (paramClazz.isInterface()) { //参数为接口，不做具体实现
            return;
        }
        HttpProtocolParam protocolParam = new HttpProtocolParam(paramName, getDefaultValue(paramClazz, paramName), required);
        if (protocol.getParamList() == null) {
            protocol.setParamList(new ArrayList<HttpProtocolParam>());
        }
        protocol.getParamList().add(protocolParam);
    }

    /**
     * 构建路径参数
     * @param index
     * @param paramClazz
     * @param paramName
     * @param protocol
     */
    public static void buildPathVariable(int index, Class paramClazz, String paramName, HttpProtocol protocol) throws Exception {
        HttpProtocolPath protocolPath = new HttpProtocolPath(index, paramName, getDefaultValue(paramClazz, paramName));
        if (protocol.getPathList() == null) {
            protocol.setPathList(new ArrayList<HttpProtocolPath>());
        }
        protocol.getPathList().add(protocolPath);
    }

    /**
     * 构建表单
     * @param paramClass
     * @param paramName
     * @param protocol
     * @throws Exception
     */
    public static void buildFrom(Class paramClass, String paramName, HttpProtocol protocol) throws Exception {
        Object requestBody = protocol.getRequestBody();
        Map<String, Object> fromMap = new HashMap<>();
        List<Map<String, Object>> fromList = new ArrayList<>();
        if (requestBody == null) {
            protocol.setRequestBody(fromList);
        } else {
            fromList = (List<Map<String, Object>>) requestBody;
        }
        fromList.add(fromMap);
        setParamDefault(paramName, fromMap, paramClass);
    }

    /**
     * 构建JSON请求报文
     * @param paramClass
     * @param protocol
     * @throws Exception
     */
    public static void buildRequestBody(Class paramClass, HttpProtocol protocol) throws Exception {
        protocol.setRequestBody(setRequestDefault(paramClass));
    }

    /**
     * 基于无泛型的原始类型构建标准响应结构
     * @param protocol
     * @param method
     */
    public static void buildResponseBody(HttpProtocol protocol, Method method) {
        Class clazz = method.getReturnType();
        String clazzName = clazz.getSimpleName();
        if (clazz.isInterface()) {
            if (clazzName.equalsIgnoreCase("map")) {
                protocol.setResponseBody(new HashMap<>());
            } else if (clazzName.equalsIgnoreCase("set")) {
                protocol.setResponseBody(new HashSet<>());
            } else if (clazzName.equalsIgnoreCase("list")) {
                protocol.setResponseBody(new ArrayList<>());
            } else {    //默认填充对象
                protocol.setResponseBody(new HashMap<>());
            }
        } else {
            if (clazzName.equalsIgnoreCase("void")) {
                //ignore
                return;
            }
            Object clazzObj = null;
            try {
                clazzObj = clazz.newInstance();
            } catch (Exception e) {
                logger.warn(e.getMessage() + ", class " + clazz.getName() + " don't have Constructor method!");
            }

            if (clazzObj instanceof Map) {
                protocol.setResponseBody(new HashMap<>());
            } else if (clazzObj instanceof Set) {
                protocol.setResponseBody(new HashSet<>());
            } else if (clazzObj instanceof List) {
                protocol.setResponseBody(new ArrayList<>());
            } else {
                protocol.setResponseBody(clazzObj);
            }
        }
    }

    /**
     * 填充参数默认值
     * @param param
     * @throws Exception
     */
    private static Object setRequestDefault(Class param) throws Exception {
        if (param.isInterface()) {
            String paramName = param.getSimpleName();
            if (paramName.equalsIgnoreCase("map")) {
                return new HashMap<>();
            } else if (paramName.equalsIgnoreCase("set")) {
                return new HashSet<>();
            } else if (paramName.equalsIgnoreCase("list")) {
                return new ArrayList<>();
            }
        } else {
            try {
                buildStructure(param);
                return param.newInstance();
            } catch (Exception e) {
                logger.error("instance " + param + " error! no default Constructor method for " + param);
            }
        }

        return new Object();
    }

    private static Map<String, Object> buildStructure(Class clazz) {
        Map<String, Object> structureMap = new HashMap<>();
        for(Method method : clazz.getMethods()) {
            String methodName = method.getName();
            if (methodName.startsWith("get") && Modifier.isPublic(method.getModifiers()) && !Modifier.isNative(method.getModifiers())) {
                String fieldName = methodName.substring(3);
                String fieldHeader = fieldName.substring(0, 1).toLowerCase();
                fieldName = fieldHeader + fieldName.substring(1);

                structureMap.put(fieldName, "");
            }
        }

        return null;
    }

    /**
     * 填充参数默认值
     * @param name
     * @param from
     * @param param
     * @throws Exception
     */
    private static void setParamDefault(String name, Map<String, Object> from, Class param) throws Exception {
        String paramName = param.getSimpleName();
        if (paramName.equalsIgnoreCase("int")
                || paramName.equalsIgnoreCase(Integer.class.getSimpleName())) {
            from.put("name", name);
            from.put("value", 0);
        } else if (paramName.equalsIgnoreCase("long")
                || paramName.equalsIgnoreCase(Long.class.getSimpleName())) {
            from.put("name", name);
            from.put("value", 0);
        } else if (paramName.equalsIgnoreCase(String.class.getSimpleName())) {
            from.put("name", name);
            from.put("value", "");
        } else if (paramName.equalsIgnoreCase("float")
                || paramName.equalsIgnoreCase(Float.class.getSimpleName())) {
            from.put("name", name);
            from.put("value", 0);
        } else if (paramName.equalsIgnoreCase("double")
                || paramName.equalsIgnoreCase(Double.class.getSimpleName())) {
            from.put("name", name);
            from.put("value", 0);
        } else if (paramName.equalsIgnoreCase("char")) {
            from.put("name", name);
            from.put("value", "");
        } else if (paramName.equalsIgnoreCase("map")
                || paramName.equalsIgnoreCase("set")) {
            //ignore
        } else if (paramName.equalsIgnoreCase("list")) {
            //ignore
        } else {
            from.putAll(JSON.parseObject(
                    JSON.toJSONString(param.newInstance(),
                            SerializerFeature.WriteNullListAsEmpty,
                            SerializerFeature.WriteNullBooleanAsFalse,
                            SerializerFeature.WriteNullNumberAsZero,
                            SerializerFeature.WriteNullStringAsEmpty,
                            SerializerFeature.WriteMapNullValue), Map.class));
        }
    }

    private static Object getDefaultValue(Class param, String name) throws Exception {
        String paramName = param.getSimpleName();
        if (paramName.equalsIgnoreCase("int")
                || paramName.equalsIgnoreCase(Integer.class.getSimpleName())) {
            return 0;
        } else if (paramName.equalsIgnoreCase("long")
                || paramName.equalsIgnoreCase(Long.class.getSimpleName())) {
            return 0;
        } else if (paramName.equalsIgnoreCase(String.class.getSimpleName())) {
            return name;
        } else if (paramName.equalsIgnoreCase("float")
                || paramName.equalsIgnoreCase(Float.class.getSimpleName())) {
            return 0;
        } else if (paramName.equalsIgnoreCase("double")
                || paramName.equalsIgnoreCase(Double.class.getSimpleName())) {
            return 0;
        } else if (paramName.equalsIgnoreCase("char")) {
            return "";
        } else if (paramName.equalsIgnoreCase("map")) {
            return new HashMap<>();
        } else if (paramName.equalsIgnoreCase("set")) {
            return new HashSet<>();
        } else {
            return JSON.parseObject(
                    JSON.toJSONString(param.newInstance(),
                            SerializerFeature.WriteNullListAsEmpty,
                            SerializerFeature.WriteNullBooleanAsFalse,
                            SerializerFeature.WriteNullNumberAsZero,
                            SerializerFeature.WriteNullStringAsEmpty,
                            SerializerFeature.WriteMapNullValue), Map.class);
        }
    }
}

package com.feixue.mbridge.scan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.feixue.mbridge.meta.annotation.RESTfulDoc;
import com.feixue.mbridge.meta.domain.HttpProtocol;
import com.feixue.mbridge.meta.domain.HttpProtocolContentType;
import com.feixue.mbridge.meta.domain.HttpProtocolParam;
import com.feixue.mbridge.meta.domain.HttpProtocolPath;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 扫描注解，生成元数据，传输到mbridge master
 */
public class MbridgeScan implements InitializingBean, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(MbridgeScan.class);

    private Environment environment;

    //扫描路径
    private String scanPath;

    //协议文件路径
    private String filePath;

    //系统识别码
    private String systemCode;

    //扫描开关
    private boolean scanSwitch = false;

    private ClassPathScanningCandidateComponentProvider classPathScanningCandidateComponentProvider;

    private Set<Class> classSet = new HashSet<>();

    private List<HttpProtocol> protocolList = new ArrayList<>();

    private ClassPool pool = ClassPool.getDefault();

    /**
     * 提取需要处理的协议集合
     */
    public void extractDoc() throws Exception {
        for(Class clazz : classSet) {
            RequestMapping clazzAnnotation = AnnotationUtils.findAnnotation(clazz, RequestMapping.class);
            String clazzUrl = "";
            if (clazzAnnotation != null) {
                    clazzUrl = clazzAnnotation.value()[0];
            }
            for (Method method : clazz.getMethods()) {
                RequestMapping methodAnnotation = AnnotationUtils.findAnnotation(method, RequestMapping.class);
                if (methodAnnotation == null) {
                    continue;
                }
                HttpProtocol protocol = new HttpProtocol();
                //系统识别码
                protocol.setSystemCode(systemCode);

                //URL
                String methodUrl = methodAnnotation.value()[0];
                String requestUrl = clazzUrl + methodUrl;
                protocol.setUrlPath(requestUrl);

                //doc
                setRESTFulDoc(method, protocol);

                //requestType
                setRequestType(methodAnnotation, protocol);

                //request param
                setRequestParam(method, protocol);

                //response
                ResponseBody responseBody = AnnotationUtils.findAnnotation(method, ResponseBody.class);
                if (responseBody != null) {
                    setResponseDefault(protocol, method);
                }

                protocolList.add(protocol);
            }
        }
    }

    /**
     * 设置接口描述
     * @param method
     * @param protocol
     */
    private void setRESTFulDoc(Method method, HttpProtocol protocol) {
        RESTfulDoc methodDoc = AnnotationUtils.findAnnotation(method, RESTfulDoc.class);
        if (methodDoc != null) {
            protocol.setUrlDesc(methodDoc.value());
        } else {
            protocol.setUrlDesc("未定义协议描述~");
        }
    }

    /**
     * 设置请求方法集合
     * @param methodAnnotation
     * @param protocol
     */
    private void setRequestType(RequestMapping methodAnnotation, HttpProtocol protocol) {
        RequestMethod [] requestMethods = methodAnnotation.method();
        Set<String> requestTypeSet = new HashSet<>();
        if (requestMethods == null) {
            requestMethods = new RequestMethod[] {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE};
        }
        for(RequestMethod requestMethod : requestMethods) {
            requestTypeSet.add(requestMethod.name());
        }
        protocol.setRequestTypeSet(requestTypeSet);
    }

    private void setRequestParam(Method method, HttpProtocol protocol) throws Exception {
        String[] params = getParamName(method);
        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] annotations = method.getParameterAnnotations();

        for(int i = 0; i < params.length; i++) {
            //1.判断参数注解类型
            Annotation paramterAnnotations[] = annotations[i];
            if (paramterAnnotations.length == 0) {
                //get下不支持表单
                if (protocol.getRequestTypeSet().size() <= 1 && protocol.getRequestTypeSet().contains(RequestMethod.GET.name())) {
                    logger.warn("get request type not support from data!");
                } else {
                    //语义存在争议
                    if (!StringUtils.isEmpty(protocol.getContentType())) {
                        protocol.setDispute(true);
                        continue;
                    }
                    protocol.setContentType(HttpProtocolContentType.form);
                    buildFrom(parameterTypes[i], params[i], protocol);
                }
            } else {
                for(Annotation annotation : paramterAnnotations) {
                    if (annotation instanceof RequestParam) {
                        buildRequestParam(parameterTypes[i],
                                ((RequestParam) annotation).required(), params[i], protocol);
                        break;
                    } else if (annotation instanceof PathVariable) {
                        buildPathVariable(i, parameterTypes[i], params[i], protocol);
                        break;
                    } else if (annotation instanceof RequestBody) {
                        //语义存在争议
                        if (!StringUtils.isEmpty(protocol.getContentType())) {
                            protocol.setDispute(true);
                            continue;
                        }
                        protocol.setContentType(HttpProtocolContentType.json);
                        buildRequestBody(parameterTypes[i], protocol);
                        break;
                    } else {
                        //TODO 其他注解需要能够协同工作才会支持
                        logger.warn("其它注解：" + annotation.toString());
                    }
                }
            }
        }
    }

    /**
     * 设置响应默认值
     * @param protocol
     * @param method
     * @throws Exception
     */
    private void setResponseDefault(HttpProtocol protocol, Method method) throws Exception {
        Type type = method.getGenericReturnType();
        if (type instanceof Class) {
            buildResponseWithClass(protocol, method);
        } else if (type instanceof ParameterizedTypeImpl) {
            ParameterizedTypeImpl returnType = (ParameterizedTypeImpl)type;

            Object instance = buildInstance(returnType);
            protocol.setResponseBody(instance);
        }
    }

    private Object buildInstance(ParameterizedTypeImpl returnType) throws Exception {
        Class<?> rawType = returnType.getRawType();
        if (rawType.getName().equalsIgnoreCase(Map.class.getName())) {  //返回类型为Map，则忽略真实类型
            return new HashMap<>();
        } else if(rawType.getName().equalsIgnoreCase(List.class.getName())) {   //返回类型为List
            Type typeArgument = returnType.getActualTypeArguments()[0];
            Object object = null;
            if (typeArgument instanceof Class) {
                object = ((Class) typeArgument).newInstance();
            } else if (typeArgument instanceof TypeVariableImpl) {
                object = buildInstance((ParameterizedTypeImpl) returnType.getActualTypeArguments()[0]);
            }

            return Arrays.asList(object);
        } else {
            try {
                Object objInstance = rawType.newInstance();
                for (Method clazzMethod : rawType.getMethods()) {
                    String methodName = clazzMethod.getName();
                    if (methodName.startsWith("set")) {
                        Type methodParameterType = clazzMethod.getGenericParameterTypes()[0];
                        if (methodParameterType instanceof Class) {    //常规类型
                            paramInstance(objInstance, clazzMethod, methodParameterType);
                        } else if (methodParameterType instanceof TypeVariableImpl) {  //泛型类型
                            Type actualType = returnType.getActualTypeArguments()[0];
                            if (actualType instanceof Class) {
                                paramInstance(objInstance, clazzMethod, actualType);
                            } else {
                                Object object = buildInstance((ParameterizedTypeImpl) returnType.getActualTypeArguments()[0]);
                                clazzMethod.invoke(objInstance, object);
                            }
                        }
                    }
                }
                return objInstance;
            } catch (Exception e) {
                logger.warn(e.getMessage() + ", class " + rawType.getName() + " don't have Constructor method!");
                return "";
            }
        }
    }

    private void paramInstance(Object objInstance, Method clazzMethod, Type methodParameterType) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        String paramName = ((Class) methodParameterType).getSimpleName();
        if (paramName.equalsIgnoreCase(long.class.getSimpleName())
                || paramName.equalsIgnoreCase(boolean.class.getSimpleName())
                || paramName.equalsIgnoreCase(int.class.getSimpleName())
                || paramName.equalsIgnoreCase(float.class.getSimpleName())
                || paramName.equalsIgnoreCase(double.class.getSimpleName())
                || paramName.equalsIgnoreCase(char.class.getSimpleName())) {
            //忽略，自动填充默认值
        } else if (paramName.equalsIgnoreCase(Boolean.class.getSimpleName())) {
            clazzMethod.invoke(objInstance, Boolean.FALSE);
        } else {
            try {
                clazzMethod.invoke(objInstance, ((Class) methodParameterType).newInstance());
            } catch (Exception e) {
                logger.warn("type " + methodParameterType + " not have default Constructor method!");
            }
        }
    }

    /**
     * 基于无泛型的原始类型构建标准响应结构
     * @param protocol
     * @param method
     */
    private void buildResponseWithClass(HttpProtocol protocol, Method method) {
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
     * 构建表单
     * @param paramClass
     * @param paramName
     * @param protocol
     * @throws Exception
     */
    private void buildFrom(Class paramClass, String paramName, HttpProtocol protocol) throws Exception {
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
    private void buildRequestBody(Class paramClass, HttpProtocol protocol) throws Exception {
        protocol.setRequestBody(setRequestDefault(paramClass));
    }

    /**
     * 构建路径参数
     * @param index
     * @param paramClazz
     * @param paramName
     * @param protocol
     */
    private void buildPathVariable(int index, Class paramClazz, String paramName, HttpProtocol protocol) throws Exception {
        HttpProtocolPath protocolPath = new HttpProtocolPath(index, paramName, getDefaultValue(paramClazz, paramName));
        if (protocol.getPathList() == null) {
            protocol.setPathList(new ArrayList<HttpProtocolPath>());
        }
        protocol.getPathList().add(protocolPath);
    }

    /**
     * 构建请求参数
     * @param paramClazz
     * @param required
     * @param paramName
     * @param protocol
     * @throws Exception
     */
    private void buildRequestParam(Class paramClazz, boolean required, String paramName, HttpProtocol protocol) throws Exception {
        if (paramClazz.isInterface()) { //参数为接口，不做具体实现
            return;
        }
        HttpProtocolParam protocolParam = new HttpProtocolParam(paramName, getDefaultValue(paramClazz, paramName), required);
        if (protocol.getParamList() == null) {
            protocol.setParamList(new ArrayList<HttpProtocolParam>());
        }
        protocol.getParamList().add(protocolParam);
    }

    private Object getDefaultValue(Class param, String name) throws Exception {
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

    /**
     * 填充参数默认值
     * @param name
     * @param from
     * @param param
     * @throws Exception
     */
    private void setParamDefault(String name, Map<String, Object> from, Class param) throws Exception {
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

    /**
     * 填充参数默认值
     * @param param
     * @throws Exception
     */
    private Object setRequestDefault(Class param) throws Exception {
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

    private Map<String, Object> buildStructure(Class clazz) {
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
     * 获取参数名称
     * @param method
     * @return
     * @throws Exception
     */
    private String[] getParamName(Method method) throws Exception {
        CtClass cc = pool.get(method.getDeclaringClass().getName());

        CtClass[] params = new CtClass[method.getParameterTypes().length];
        for(int i = 0; i < method.getParameterTypes().length; i++) {
            params[i] = pool.getCtClass(method.getParameterTypes()[i].getName());
        }

        CtMethod cm = cc.getDeclaredMethod(method.getName(), params);

        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();

        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        String[] paramNames = new String[cm.getParameterTypes().length];

        TreeMap<Integer, Integer> map = new TreeMap<>();
        for(int i = 0; i < attr.tableLength(); i++) {
            map.put(attr.index(i), i);
        }
        int index = 0;
        boolean flag = false;
        for(Integer key : map.keySet()) {
            if (!flag) {
                flag = true;
                continue;
            }

            if (index < paramNames.length) {
                paramNames[index++] = attr.variableName(map.get(key));
            } else {
                break;
            }
        }

        return paramNames;
    }

    private void writeFile() {
        BufferedWriter bufferedWriter = null;
        try {
            File fileDir = new File(filePath);
            if (fileDir.isDirectory()) {
                File file = new File(filePath, systemCode + "-protocol.txt");
                bufferedWriter = new BufferedWriter(new FileWriter(file));
                bufferedWriter.write(
                        JSON.toJSONString(protocolList,
                                SerializerFeature.IgnoreErrorGetter,
                                SerializerFeature.WriteNullListAsEmpty,
                                SerializerFeature.WriteNullBooleanAsFalse,
                                SerializerFeature.WriteNullNumberAsZero,
                                SerializerFeature.WriteNullStringAsEmpty,
                                SerializerFeature.WriteMapNullValue,
                                SerializerFeature.WriteNonStringKeyAsString));
                bufferedWriter.close();

                logger.info("write protocol file for:" + file.getName() + " success!");
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public void setScanPath(String scanPath) {
        this.scanPath = scanPath.replace('.', '/');
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setScanSwitch(boolean scanSwitch) {
        this.scanSwitch = scanSwitch;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!scanSwitch || StringUtils.isEmpty(systemCode)) {  //不开启扫描，或者systemCode未指定，则无视
            return;
        }
        //加入完整的classpath
        pool.appendClassPath(new ClassClassPath(this.getClass()));
        //初始化
        initScan();
        separateClass();
        extractDoc();
        //协议持久化到文件
        writeFile();
    }

    /**
     * 分离需要进一步处理的class
     */
    private void separateClass() {
        try {
            Set<BeanDefinition> set = classPathScanningCandidateComponentProvider.findCandidateComponents(scanPath);
            for(BeanDefinition beanDefinition : set) {
                classSet.add(Class.forName(beanDefinition.getBeanClassName()));
            }
        } catch (Exception e) {
            logger.error("Separate class error！", e);
        }
    }

    /**
     * 初始化扫描工具，并定义需要扫描的注解限定
     */
    private void initScan() {
        classPathScanningCandidateComponentProvider =
                new ClassPathScanningCandidateComponentProvider(false, environment);
        classPathScanningCandidateComponentProvider.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
    }
}

package com.feixue.mbridge.scan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.feixue.mbridge.meta.annotation.RESTfulDoc;
import com.feixue.mbridge.meta.domain.HttpProtocol;
import com.feixue.mbridge.meta.domain.HttpProtocolContentType;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
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
                //模糊化处理
                protocol.setQueryUrlPath(requestUrl.replaceAll("\\{([a-zA-Z0-9_])+}", "{*}"));

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
                    ProtocolScanBuilder.buildFrom(parameterTypes[i], params[i], protocol);
                }
            } else {
                for(Annotation annotation : paramterAnnotations) {
                    if (annotation instanceof RequestParam) {
                        ProtocolScanBuilder.buildRequestParam(parameterTypes[i],
                                ((RequestParam) annotation).required(), params[i], protocol);
                        break;
                    } else if (annotation instanceof PathVariable) {
                        ProtocolScanBuilder.buildPathVariable(i, parameterTypes[i], params[i], protocol);
                        break;
                    } else if (annotation instanceof RequestBody) {
                        //语义存在争议
                        if (!StringUtils.isEmpty(protocol.getContentType())) {
                            protocol.setDispute(true);
                            continue;
                        }
                        protocol.setContentType(HttpProtocolContentType.json);
                        ProtocolScanBuilder.buildRequestBody(parameterTypes[i], protocol);
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
            ProtocolScanBuilder.buildResponseBody(protocol, method);
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

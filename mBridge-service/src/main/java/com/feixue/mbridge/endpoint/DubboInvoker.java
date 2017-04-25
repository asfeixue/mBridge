package com.feixue.mbridge.endpoint;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.feixue.mbridge.domain.BusinessWrapper;
import com.feixue.mbridge.domain.ErrorCode;
import com.feixue.mbridge.domain.dubbo.DubboInvokerDO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxxiao on 16/9/13.
 */
@Service
public class DubboInvoker implements InitializingBean {

    private ApplicationConfig application;

    private Map<String, RegistryConfig> registryConfigMap = new HashMap<>();

    private BusinessWrapper doInvoker(DubboInvokerDO invokerDO) {
        RegistryConfig registry;
        if (!registryConfigMap.containsKey(invokerDO.getRegisterAddress())) {
            registry = new RegistryConfig();
            registry.setProtocol("zookeeper");
            registry.setAddress(invokerDO.getRegisterAddress());

            registryConfigMap.put(invokerDO.getRegisterAddress(), registry);
        } else {
            registry = registryConfigMap.get(invokerDO.getRegisterAddress());
        }

        ReferenceConfig<GenericService> reference = new ReferenceConfig();
        reference.setApplication(application);
        reference.setRegistry(registry);
        reference.setInterface(invokerDO.getInterfaceName());
        reference.setTimeout(3600000);
        reference.setGeneric(true);

        GenericService genericService = reference.get();

        List<String> paramTypeList = new ArrayList<>();
        List<Object> paramValueList = new ArrayList<>();
        for(Map<String, Object> paramMap : invokerDO.getParamList()) {
            for(Map.Entry<String, Object> entry : paramMap.entrySet()) {
                paramTypeList.add(entry.getKey());
                paramValueList.add(entry.getValue());
            }
        }

        try {
            Object invokeObj = genericService.$invoke(invokerDO.getInvokeMethod(),
                    paramTypeList.toArray(new String[]{}), paramValueList.toArray());
            return new BusinessWrapper(invokeObj);
        } catch (Exception e) {
            return new BusinessWrapper(ErrorCode.serviceFailure.getCode(), e.getMessage());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        application = new ApplicationConfig();
        application.setName("mBridge");
        application.setId("mBridge");
    }
}

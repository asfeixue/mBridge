package com.feixue.mbridge.domain.dubbo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by zxxiao on 16/9/13.
 */
public class DubboInvokerDO implements Serializable {
    private static final long serialVersionUID = -1998321613010454814L;

    /*
    接口名称
     */
    private String interfaceName;

    /*
    版本
     */
    private String version;

    /*
    执行方法
     */
    private String invokeMethod;

    /*
    注册中心地址
     */
    private String registerAddress;

    /*
    参数集合
     */
    private List<Map<String, Object>> paramList;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInvokeMethod() {
        return invokeMethod;
    }

    public void setInvokeMethod(String invokeMethod) {
        this.invokeMethod = invokeMethod;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public List<Map<String, Object>> getParamList() {
        return paramList;
    }

    public void setParamList(List<Map<String, Object>> paramList) {
        this.paramList = paramList;
    }

    @Override
    public String toString() {
        return "DubboInvokerDO{" +
                "interfaceName='" + interfaceName + '\'' +
                ", version='" + version + '\'' +
                ", invokeMethod='" + invokeMethod + '\'' +
                ", registerAddress='" + registerAddress + '\'' +
                ", paramList=" + paramList +
                '}';
    }
}

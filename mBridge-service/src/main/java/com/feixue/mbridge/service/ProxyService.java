package com.feixue.mbridge.service;

/**
 * Created by zxxiao on 2017/7/19.
 */
public interface ProxyService {

    /**
     * 注册server instance
     * @param systemCode
     * @return
     */
    boolean registerServer(String systemCode);

    /**
     * 注销server instance
     * @param systemCode
     * @return
     */
    boolean unregisterServer(String systemCode);
}

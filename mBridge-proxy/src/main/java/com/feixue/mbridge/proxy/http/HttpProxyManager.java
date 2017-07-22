package com.feixue.mbridge.proxy.http;

import com.alibaba.fastjson.JSON;
import com.feixue.mbridge.proxy.CallbackNotify;
import com.feixue.mbridge.proxy.CallbackNotifyChain;
import com.feixue.mbridge.proxy.ProxyManager;
import com.feixue.mbridge.proxy.ServerProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zxxiao on 2017/7/14.
 */
public class HttpProxyManager<HttpProxyContent, SystemDO, BusinessWrapper> implements ProxyManager<HttpProxyContent, SystemDO, BusinessWrapper> {

    private static final Logger logger = LoggerFactory.getLogger(HttpProxyManager.class);

    //系统 code 映射关系
    private Map<Integer, Set<SystemDO>> systemMap = new ConcurrentHashMap<>();

    //server proxy & code 映射关系
    private Map<Integer, ServerProxy> serverProxyMap = new ConcurrentHashMap<>();

    private static ProxyManager proxyManager = new HttpProxyManager();

    private HttpProxyManager() {
    }

    public static ProxyManager getInstance() {
        return proxyManager;
    }

    @Override
    public boolean registerProxy(SystemDO proxyContent, CallbackNotify<HttpProxyContent, BusinessWrapper> callback) {
        try {
            addSystem(proxyContent);
            addServerProxy(proxyContent, callback);

            return true;
        } catch (Exception e) {
            logger.error("register proxy error with proxyContent={}", JSON.toJSONString(proxyContent), e);
            delSystem(proxyContent);
            return false;
        }
    }

    @Override
    public boolean unregisterProxy(SystemDO proxyContent) {
        try {
            delSystem(proxyContent);
            delServerProxy(proxyContent);
            return true;
        } catch (Exception e) {
            logger.error("unregister proxy error with proxyContent={}", JSON.toJSONString(proxyContent));
            return false;
        }
    }

    /**
     * 添加新的 server proxy 实例
     * @param proxyContent
     * @param callback
     * @throws Exception
     */
    private void addServerProxy(SystemDO proxyContent, CallbackNotify<HttpProxyContent, BusinessWrapper> callback) throws Exception {
        int port = ((com.feixue.mbridge.domain.system.SystemDO)proxyContent).getProcessPort();
        if (serverProxyMap.containsKey(port)) {
            serverProxyMap.get(port).getNotifyChain().addEnd(callback);
            return;
        }

        ServerProxy serverProxy = new HttpServerProxy();

        serverProxy.bind(port, new CallbackNotifyChain(callback));

        serverProxyMap.put(port, serverProxy);
    }

    /**
     * 删除并关闭 server proxy 实例
     * @param proxyContent
     */
    private void delServerProxy(SystemDO proxyContent) {
        int port = ((com.feixue.mbridge.domain.system.SystemDO)proxyContent).getProcessPort();
        if (!serverProxyMap.containsKey(port)) {
            return;
        }
        ServerProxy serverProxy = serverProxyMap.get(port);
        serverProxy.close();
        serverProxyMap.remove(port);
    }

    /**
     * 添加新系统
     * @param proxyContent
     */
    private void addSystem(SystemDO proxyContent) {
        int processPort = ((com.feixue.mbridge.domain.system.SystemDO)proxyContent).getProcessPort();
        Set<SystemDO> systemDOSet = systemMap.get(processPort);
        if (systemDOSet == null) {
            systemDOSet = new HashSet<>();
            systemMap.put(processPort, systemDOSet);
        }
        systemDOSet.add(proxyContent);
    }

    /**
     * 移除指定系统
     * @param proxyContent
     */
    private void delSystem(SystemDO proxyContent) {
        int processPort = ((com.feixue.mbridge.domain.system.SystemDO)proxyContent).getProcessPort();
        Set<SystemDO> systemDOSet = systemMap.get(processPort);
        if (systemDOSet != null) {
            systemDOSet.remove(proxyContent);
        }
    }
}

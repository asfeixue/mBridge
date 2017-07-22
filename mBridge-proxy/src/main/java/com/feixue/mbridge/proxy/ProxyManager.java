package com.feixue.mbridge.proxy;

/**
 * Created by zxxiao on 2017/7/14.
 */
public interface ProxyManager<A, B, C> {

    /**
     * 注册代理
     * @param proxyContent  代理上下文
     * @param callback      代理回调
     * @return
     */
    boolean registerProxy(B proxyContent, CallbackNotify<A, C> callback);

    /**
     * 注销代理
     * @param proxyContent
     */
    boolean unregisterProxy(B proxyContent);
}

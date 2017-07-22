package com.feixue.mbridge.proxy;

/**
 * client endpoint proxy
 * Created by zxxiao on 2017/7/12.
 */
public interface ClientProxy extends Proxy {

    /**
     * 和指定服务器地址建立链接
     * @param host
     * @param port
     * @throws Exception
     */
    void connect(String host, int port) throws Exception;
}

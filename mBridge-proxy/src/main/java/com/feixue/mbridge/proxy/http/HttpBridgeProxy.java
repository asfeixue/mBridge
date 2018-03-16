package com.feixue.mbridge.proxy.http;

import com.feixue.mbridge.proxy.BridgeProxy;
import com.feixue.mbridge.proxy.CallbackNotifyChain;

/**
 * 1.面向http client 读取请求数据
 * 2.持久化真实 client 的 request
 * 3.以mock client 代理身份请求真实 server
 * 4.持久化真实 server 的 response
 * 5.以mock server 代理身份响应真实 client
 */
public class HttpBridgeProxy implements BridgeProxy {

    private HttpServerProxy serverProxy = new HttpServerProxy();


    @Override
    public void connect(String host, int port) throws Exception {

    }

    @Override
    public void bind(int port, CallbackNotifyChain notifyChain) throws Exception {
        serverProxy.bind(port, notifyChain);
    }

    @Override
    public CallbackNotifyChain getNotifyChain() {
        return null;
    }

    @Override
    public void close() {

    }
}

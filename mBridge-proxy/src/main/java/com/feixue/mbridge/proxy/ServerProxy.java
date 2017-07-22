package com.feixue.mbridge.proxy;

/**
 * server endpoint proxy
 * Created by zxxiao on 2017/7/12.
 */
public interface ServerProxy<T, I> extends Proxy {

    /**
     * 绑定端口并设定指定回调通知
     * @param port
     * @param notifyChain
     * @throws Exception
     */
    void bind(int port, CallbackNotifyChain<T, I> notifyChain) throws Exception;

    /**
     * 获取chain
     * @return
     */
    CallbackNotifyChain<T, I> getNotifyChain();
}

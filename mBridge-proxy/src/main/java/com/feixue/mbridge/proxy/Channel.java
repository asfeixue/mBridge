package com.feixue.mbridge.proxy;

public interface Channel {

    /**
     * 入站
     * @param invoker
     * @param invocation
     */
    void inbound(Invoker invoker, Invocation invocation);

    /**
     * 出站
     * @param invoker
     * @param invocation
     */
    void outbound(Invoker invoker, Invocation invocation);
}

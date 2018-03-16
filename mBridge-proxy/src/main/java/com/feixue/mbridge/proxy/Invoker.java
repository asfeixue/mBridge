package com.feixue.mbridge.proxy;

public interface Invoker {

    /**
     * invoke.
     * @param invocation
     * @return
     */
    Result doInvoke(Invocation invocation);
}

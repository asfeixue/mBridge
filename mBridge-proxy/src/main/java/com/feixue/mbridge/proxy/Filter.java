package com.feixue.mbridge.proxy;

public interface Filter {

    /**
     * do invoke filter
     * @param invoker
     * @param invocation
     * @return
     */
    Result invoke(Invoker invoker, Invocation invocation);

    Filter getNextFilter();

    void setNextFilter(Filter nextFilter);

    Filter getHeadFilter();

    void setHeadFilter(Filter headFilter);
}

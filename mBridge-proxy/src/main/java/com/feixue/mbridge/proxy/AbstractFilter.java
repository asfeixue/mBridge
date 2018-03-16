package com.feixue.mbridge.proxy;

public abstract class AbstractFilter implements Filter {

    /**
     * 下一个filter
     */
    private Filter nextFilter;

    /**
     * 上一个filter
     */
    private Filter headFilter;

    @Override
    public Filter getNextFilter() {
        return nextFilter;
    }

    @Override
    public void setNextFilter(Filter nextFilter) {
        this.nextFilter = nextFilter;
    }

    @Override
    public Filter getHeadFilter() {
        return headFilter;
    }

    @Override
    public void setHeadFilter(Filter headFilter) {
        this.headFilter = headFilter;
    }
}

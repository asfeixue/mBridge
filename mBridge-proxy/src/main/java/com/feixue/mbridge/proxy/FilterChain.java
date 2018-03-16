package com.feixue.mbridge.proxy;

public class FilterChain extends AbstractFilter implements Filter {

    /**
     * 上一个filter
     */
    private Filter headFilter;

    /**
     * 下一个filter
     */
    private Filter nextFilter;

    @Override
    public Result invoke(Invoker invoker, Invocation invocation) {
        if (headFilter != null) {
            return headFilter.invoke(invoker, invocation);
        } else {
            if (nextFilter != null) {
                return nextFilter.invoke(invoker, invocation);
            }
        }
        return null;
    }

    /**
     * 链表尾部添加
     * @param filter
     */
    public void addLast(Filter filter) {
        if (nextFilter == null) {
            nextFilter = filter;
            if (headFilter != null) {
                filter.setHeadFilter(headFilter);
                headFilter.setNextFilter(filter);
            } else {
                headFilter = filter;
            }
        } else {
            nextFilter.setNextFilter(filter);
            filter.setHeadFilter(nextFilter);

            if (headFilter == null) {
                headFilter = nextFilter;
            }

            nextFilter = filter;
        }
    }

    /**
     * 链表头部添加
     * @param filter
     */
    public void addHead(Filter filter) {
        if (headFilter == null) {
            headFilter = filter;
            if (nextFilter != null) {
                filter.setNextFilter(nextFilter);
                nextFilter.setHeadFilter(filter);
            } else {
                nextFilter = filter;
            }
        } else {
            headFilter.setHeadFilter(filter);
            filter.setNextFilter(headFilter);

            if (nextFilter == null) {
                nextFilter = headFilter;
            }

            headFilter = filter;
        }
    }
}

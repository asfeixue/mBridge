package com.feixue.mbridge.proxy.http;

import com.feixue.mbridge.proxy.FilterChain;
import com.feixue.mbridge.proxy.Invocation;
import com.feixue.mbridge.proxy.Invoker;
import com.feixue.mbridge.proxy.Result;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http invoker。
 * 1.有整个filter链路的执行环境
 * 2.支持多种处理模式，mock client & proxy & mock server
 * 3.协议层面的完整支持
 */
public class HttpInvoker implements Invoker {

    private static final Logger logger = LoggerFactory.getLogger(HttpInvoker.class);

    /**
     * 请求 or 响应句柄
     */
    private ChannelHandlerContext ctx;

    private FilterChain filterChain;

    public HttpInvoker(ChannelHandlerContext ctx, FilterChain filterChain) {
        this.ctx = ctx;
        this.filterChain = filterChain;
    }

    @Override
    public Result doInvoke(Invocation invocation) {
        return filterChain.invoke(this, invocation);
    }

    public final ChannelHandlerContext getCtx() {
        return ctx;
    }

    public final FilterChain getFilterChain() {
        return filterChain;
    }
}

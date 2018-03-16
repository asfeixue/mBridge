package com.feixue.mbridge.proxy.http.filter;

import com.feixue.mbridge.proxy.*;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 持久化请求 & 响应上下文
 */
public class StoreFilter extends AbstractFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(StoreFilter.class);

    @Override
    public Result invoke(Invoker invoker, Invocation invocation) {
        return null;
    }
}

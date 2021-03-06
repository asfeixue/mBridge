package com.feixue.mbridge.proxy.http;

import com.alibaba.fastjson.JSON;
import com.feixue.mbridge.domain.proxy.HttpProxyContent;
import com.feixue.mbridge.proxy.CallbackNotify;
import com.feixue.mbridge.proxy.CallbackNotifyChain;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by zxxiao on 2017/7/12.
 */
public class HttpServerProxyTest {

    private HttpServerProxy serverProxy;

    @Before
    public void init() throws Exception {
        serverProxy = new HttpServerProxy();
    }

    @Test
    public void test() throws Exception {
        serverProxy.bind(8888, new CallbackNotifyChain(new CallbackNotify() {
            @Override
            public boolean doNotify(Object content, Object response) {
                return false;
            }
        }));
    }
}

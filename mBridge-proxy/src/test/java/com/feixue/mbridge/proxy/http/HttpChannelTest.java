package com.feixue.mbridge.proxy.http;

import com.feixue.mbridge.proxy.http.network.HttpChannel;
import org.junit.Test;

public class HttpChannelTest {

    @Test
    public void doTest() throws InterruptedException {
        HttpChannel httpChannel = new HttpChannel(HttpChannel.SERVER_MODEL, 8888);
    }
}

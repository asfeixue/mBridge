package com.feixue.mbridge.service.impl;

import com.feixue.mbridge.service.ProxyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by zxxiao on 2017/7/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-service/*.xml")
public class TestProxyServiceImpl {

    @Resource
    private ProxyService proxyService;

    @Test
    public void testregisterServer() throws InterruptedException {
        proxyService.registerServer("mBridge");
        while (true) {
            Thread.sleep(100);
        }
    }
}

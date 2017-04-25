package com.feixue.mbridge.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 获取部署机器的ip
 * Created by zxxiao on 16/7/12.
 */
public class IPUtil {

    private static final Logger logger = LoggerFactory.getLogger(IPUtil.class);

    /**
     * 获取部署机器的ip信息
     * @return
     */
    public static String getLocalHostIP() {
        String ip;
        try {
            InetAddress address = InetAddress.getLocalHost();
            ip = address.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error(e.getMessage(), e);
            ip = "";
        }
        return ip;
    }
}

package com.feixue.mbridge.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zxxiao on 2017/7/20.
 */
public class CallbackNotifyChain<T, I> {

    private static final Logger logger = LoggerFactory.getLogger(CallbackNotifyChain.class);

    private CallbackNotify<T, I> header;
    private CallbackNotify<T, I> ender;

    public CallbackNotifyChain(CallbackNotify<T, I> header) {
        this.header = header;
    }

    /**
     * 链表头部添加节点
     * @param callbackNotify
     */
    public void addHeader(CallbackNotify<T, I> callbackNotify) {
        if (header == null) {
            header = callbackNotify;
            if (ender != null) {
                ender.setHeader(callbackNotify);
                callbackNotify.setNext(ender);
            }
        } else {
            header.setHeader(callbackNotify);
            callbackNotify.setNext(header);
        }
    }

    /**
     * 链表尾部添加节点
     * @param callbackNotify
     */
    public void addEnd(CallbackNotify<T, I> callbackNotify) {
        if (ender == null) {
            ender = callbackNotify;
            if (header != null) {
                header.setNext(ender);
                ender.setHeader(header);
            }
        } else {
            ender.setNext(callbackNotify);
            callbackNotify.setHeader(ender);
        }
    }

    /**
     * 遍历执行所有通知
     * @param content
     * @param response
     * @return
     */
    public boolean doInvoke(T content, I response) {
        if (header == null) {
            return false;
        }
        CallbackNotify nowNode = header;
        while (!nowNode.doNotify(content, response)) {
            logger.warn("node=");
            nowNode = nowNode.getNext();
            if (nowNode == null) {
                return false;
            }
        }
        return true;
    }
}

package com.feixue.mbridge.proxy;

/**
 * Created by zxxiao on 2017/7/13.
 */
public abstract class CallbackNotify<T, I> {

    private CallbackNotify<T, I> header;

    private CallbackNotify<T, I> next;

    protected CallbackNotify<T, I> getHeader() {
        return header;
    }

    protected void setHeader(CallbackNotify<T, I> header) {
        this.header = header;
    }

    protected CallbackNotify<T, I> getNext() {
        return next;
    }

    protected void setNext(CallbackNotify<T, I> next) {
        this.next = next;
    }

    /**
     * 通知上下文
     * @param content
     */
    abstract public boolean doNotify(T content, I response);
}

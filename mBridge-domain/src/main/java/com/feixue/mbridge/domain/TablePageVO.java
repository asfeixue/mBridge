package com.feixue.mbridge.domain;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/6/25.
 */
public class TablePageVO<T> implements Serializable {
    private static final long serialVersionUID = 9071040033146130413L;

    /*
    当前page数据
     */
    private T data;

    /*
    总记录数
     */
    private long size;

    public TablePageVO() {
    }

    public TablePageVO(T data, long size) {
        this.data = data;
        this.size = size;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "TablePageVO{" +
                "data=" + data +
                ", size=" + size +
                '}';
    }
}

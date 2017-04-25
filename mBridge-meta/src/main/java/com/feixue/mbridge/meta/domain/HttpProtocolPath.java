package com.feixue.mbridge.meta.domain;

import java.io.Serializable;

/**
 * Created by zxxiao on 2017/2/6.
 */
public class HttpProtocolPath implements Serializable {
    private static final long serialVersionUID = -4745477987452852734L;

    /*
    位置
     */
    private int index;

    /*
    名称
     */
    private String name;

    /*
    对应值
     */
    private Object value;

    public HttpProtocolPath() {
    }

    public HttpProtocolPath(int index, String name, Object value) {
        this.index = index;
        this.name = name;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "HttpProtocolPath{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}

package com.feixue.mbridge.domain.protocol;

import java.io.Serializable;

public class ProtocolPath implements Serializable {

    private static final long serialVersionUID = 8560475971547933762L;

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

    public ProtocolPath() {
    }

    public ProtocolPath(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public ProtocolPath(int index, String name, Object value) {
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
        return "ProtocolPath{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}

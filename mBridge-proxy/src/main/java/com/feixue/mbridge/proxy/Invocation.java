package com.feixue.mbridge.proxy;

import java.io.Serializable;

public class Invocation implements Serializable {
    private static final long serialVersionUID = 2048019592863817201L;

    private Object content;

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}

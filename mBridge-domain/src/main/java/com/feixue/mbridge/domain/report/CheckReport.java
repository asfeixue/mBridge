package com.feixue.mbridge.domain.report;

import org.apache.commons.lang3.time.FastDateFormat;

import java.io.Serializable;

public class CheckReport implements Serializable {
    private static final long serialVersionUID = 6781963880437848847L;

    /*
    检查结果
     */
    private boolean status;

    /*
    检查描述
     */
    private Object checkDesc;

    private String checkTime = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());

    public CheckReport() {
    }

    public CheckReport(boolean status) {
        this.status = status;
    }

    public CheckReport(boolean status, Object checkDesc) {
        this.status = status;
        this.checkDesc = checkDesc;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Object getCheckDesc() {
        return checkDesc;
    }

    public void setCheckDesc(Object checkDesc) {
        this.checkDesc = checkDesc;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    @Override
    public String toString() {
        return "CheckReport{" +
                "status=" + status +
                ", checkDesc='" + checkDesc + '\'' +
                ", checkTime=" + checkTime +
                '}';
    }
}

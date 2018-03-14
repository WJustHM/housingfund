package com.handge.housingfund.other;

/**
 * Created by tanyi on 2017/12/13.
 */
public class employee {
    private String xingming;//姓名
    private String wdid;//网点

    public employee(String xingming, String wdid) {
        this.xingming = xingming;
        this.wdid = wdid;
    }

    public String getXingming() {
        return xingming;
    }

    public void setXingming(String xingming) {
        this.xingming = xingming;
    }

    public String getWdid() {
        return wdid;
    }

    public void setWdid(String wdid) {
        this.wdid = wdid;
    }
}

package com.handge.housingfund.common.service.account.model;

import java.io.Serializable;

/**
 * Created by tanyi on 2017/7/25.
 */
public class Delres implements Serializable {
    private String id;
    private boolean res;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isRes() {
        return res;
    }

    public void setRes(boolean res) {
        this.res = res;
    }
}

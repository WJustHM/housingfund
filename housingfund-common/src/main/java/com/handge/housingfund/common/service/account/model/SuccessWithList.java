package com.handge.housingfund.common.service.account.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tanyi on 2017/7/25.
 */
public class SuccessWithList implements Serializable {
    private List<Delres> ids;
    private String state;

    public List<Delres> getIds() {
        return ids;
    }

    public void setIds(List<Delres> ids) {
        this.ids = ids;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

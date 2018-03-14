package com.handge.housingfund.common.service.account.model;

import java.io.Serializable;

/**
 * Created by tanyi on 2017/7/18.
 */
public class AccountRole extends Role implements Serializable {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

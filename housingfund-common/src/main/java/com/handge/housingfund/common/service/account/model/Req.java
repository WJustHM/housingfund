package com.handge.housingfund.common.service.account.model;

import java.io.Serializable;

/**
 * Created by tanyi on 2017/7/18.
 */
public abstract class Req<T> implements Serializable {
    private T Req;

    public T getReq() {
        return Req;
    }

    public void setReq(T req) {
        Req = req;
    }
}

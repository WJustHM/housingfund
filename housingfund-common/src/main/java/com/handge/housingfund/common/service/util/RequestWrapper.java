package com.handge.housingfund.common.service.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * Created by lian on 2017/7/12.贷款用
 */
@SuppressWarnings("unused")
@XmlRootElement(name = "RequestWrapper")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestWrapper<T> {

    private T Req;

    public RequestWrapper() {

    }

    public RequestWrapper(T req) {

        Req = req;
    }

    public T getReq() {
        return Req;
    }

    public void setReq(T req) {
        Req = req;
    }

    public String toString() {

        return "RequestWrapper{" +

                "Req='" + this.Req + '\'' +

                "}";
    }
}

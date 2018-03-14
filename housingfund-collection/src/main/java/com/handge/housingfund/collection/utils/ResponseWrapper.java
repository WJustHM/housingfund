package com.handge.housingfund.collection.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * Created by lian on 2017/7/12.
 */
@SuppressWarnings("WeakerAccess")
@XmlRootElement(name = "ResponseWrapper")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseWrapper<T> {

    private T Res;

    public T getRes() {
        return Res;
    }

    public void setRes(T Res) {
        this.Res = Res;
    }

    public ResponseWrapper(T Res) {

        this.Res = Res;
    }

}

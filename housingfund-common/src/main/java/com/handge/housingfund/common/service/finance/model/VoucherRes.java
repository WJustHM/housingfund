package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/12/6.
 */
@XmlRootElement(name = "业务凭据")
@XmlAccessorType(XmlAccessType.FIELD)
public class VoucherRes implements Serializable {

    private String JZPZH = null;//记账凭证号，做账失败时为null

    private String MSG;//消息

    public VoucherRes() {
    }

    public VoucherRes(String JZPZH, String MSG) {
        this.JZPZH = JZPZH;
        this.MSG = MSG;
    }

    public String getJZPZH() {
        return JZPZH;
    }

    public void setJZPZH(String JZPZH) {
        this.JZPZH = JZPZH;
    }

    public String getMSG() {
        return MSG;
    }

    public void setMSG(String MSG) {
        this.MSG = MSG;
    }

    @Override
    public String toString() {
        return "VoucherRes{" +
                "JZPZH='" + JZPZH + '\'' +
                ", MSG='" + MSG + '\'' +
                '}';
    }
}

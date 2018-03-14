package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/24.
 */
@XmlRootElement(name = "会计期间")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountPeriod implements Serializable {

    private String id;
    //会计年度
    private String KJND;
    //会计期间
    private String KJQJ;
    //起始日期
    private String QSRQ;
    //截至日期
    private String JIEZRQ;
    //是否结算
    private boolean SFJS;
    //结账人
    private String JZR;
    //结账日期
    private String JZRQ;

    public AccountPeriod() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKJND() {
        return KJND;
    }

    public void setKJND(String KJND) {
        this.KJND = KJND;
    }

    public String getKJQJ() {
        return KJQJ;
    }

    public void setKJQJ(String KJQJ) {
        this.KJQJ = KJQJ;
    }

    public String getQSRQ() {
        return QSRQ;
    }

    public void setQSRQ(String QSRQ) {
        this.QSRQ = QSRQ;
    }

    public String getJIEZRQ() {
        return JIEZRQ;
    }

    public void setJIEZRQ(String JIEZRQ) {
        this.JIEZRQ = JIEZRQ;
    }

    public boolean isSFJS() {
        return SFJS;
    }

    public void setSFJS(boolean SFJS) {
        this.SFJS = SFJS;
    }

    public String getJZR() {
        return JZR;
    }

    public void setJZR(String JZR) {
        this.JZR = JZR;
    }

    public String getJZRQ() {
        return JZRQ;
    }

    public void setJZRQ(String JZRQ) {
        this.JZRQ = JZRQ;
    }


    @Override
    public String toString() {
        return "AccountPeriod{" +
                "KJND=" + KJND +
                ", KJQJ='" + KJQJ + '\'' +
                ", QSRQ=" + QSRQ +
                ", JIEZRQ=" + JIEZRQ +
                ", SFJS=" + SFJS +
                ", JZR='" + JZR + '\'' +
                ", JZRQ='" + JZRQ + '\'' +
                '}';
    }
}

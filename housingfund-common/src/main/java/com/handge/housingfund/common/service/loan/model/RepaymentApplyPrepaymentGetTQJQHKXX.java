package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "RepaymentApplyPrepaymentGetTQJQHKXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentApplyPrepaymentGetTQJQHKXX  implements Serializable {

    private String CZY;  //操作员

    private String JKRXM;  //借款人姓名

    private String SYQS;  //剩余期数

    private String SYJE;  //剩余利息

    private String YWWD;  //业务网点

    private String DKZH;  //贷款账号

    private String HKJE;  //还款金额

    private String YDKKRQ;  //约定扣款日期

    private String SYBJ;  //剩余本金

    public String getCZY() {

        return this.CZY;

    }


    public void setCZY(String CZY) {

        this.CZY = CZY;

    }


    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }


    public String getSYQS() {

        return this.SYQS;

    }


    public void setSYQS(String SYQS) {

        this.SYQS = SYQS;

    }


    public String getSYJE() {

        return this.SYJE;

    }


    public void setSYJE(String SYJE) {

        this.SYJE = SYJE;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String getDKZH() {

        return this.DKZH;

    }


    public void setDKZH(String DKZH) {

        this.DKZH = DKZH;

    }


    public String getHKJE() {

        return this.HKJE;

    }


    public void setHKJE(String HKJE) {

        this.HKJE = HKJE;

    }


    public String getYDKKRQ() {

        return this.YDKKRQ;

    }


    public void setYDKKRQ(String YDKKRQ) {

        this.YDKKRQ = YDKKRQ;

    }


    public String getSYBJ() {

        return this.SYBJ;

    }


    public void setSYBJ(String SYBJ) {

        this.SYBJ = SYBJ;

    }


    public String toString() {

        return "RepaymentApplyPrepaymentGetTQJQHKXX{" +

                "CZY='" + this.CZY + '\'' + "," +
                "JKRXM='" + this.JKRXM + '\'' + "," +
                "SYQS='" + this.SYQS + '\'' + "," +
                "SYJE='" + this.SYJE + '\'' + "," +
                "YWWD='" + this.YWWD + '\'' + "," +
                "DKZH='" + this.DKZH + '\'' + "," +
                "HKJE='" + this.HKJE + '\'' + "," +
                "YDKKRQ='" + this.YDKKRQ + '\'' + "," +
                "SYBJ='" + this.SYBJ + '\'' +

                "}";

    }
}
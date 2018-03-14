package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "RepaymentApplyPrepaymentallPost")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentApplyPrepaymentallPost  implements Serializable {

    private String HKFS;  //还款方式

    private String SYBJ;  //剩余本金

    private String SYLX;  //剩余利息

    private String SYQS;  //剩余期数

    private String HKJE;  //还款金额

    private String YDKKRQ;  //约定扣款日期

    private String DKZH;  //贷款账号

    private String JKRXM;  //借款人姓名

    public String getHKFS() {

        return this.HKFS;

    }


    public void setHKFS(String HKFS) {

        this.HKFS = HKFS;

    }


    public String getSYBJ() {

        return this.SYBJ;

    }


    public void setSYBJ(String SYBJ) {

        this.SYBJ = SYBJ;

    }


    public String getSYLX() {

        return this.SYLX;

    }


    public void setSYLX(String SYLX) {

        this.SYLX = SYLX;

    }


    public String getSYQS() {

        return this.SYQS;

    }


    public void setSYQS(String SYQS) {

        this.SYQS = SYQS;

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


    public String getDKZH() {

        return this.DKZH;

    }


    public void setDKZH(String DKZH) {

        this.DKZH = DKZH;

    }


    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }


    public String toString() {

        return "RepaymentApplyPrepaymentallPost{" +

                "HKFS='" + this.HKFS + '\'' + "," +
                "SYBJ='" + this.SYBJ + '\'' + "," +
                "SYLX='" + this.SYLX + '\'' + "," +
                "SYQS='" + this.SYQS + '\'' + "," +
                "HKJE='" + this.HKJE + '\'' + "," +
                "YDKKRQ='" + this.YDKKRQ + '\'' + "," +
                "DKZH='" + this.DKZH + '\'' + "," +
                "JKRXM='" + this.JKRXM + '\'' +

                "}";

    }
}
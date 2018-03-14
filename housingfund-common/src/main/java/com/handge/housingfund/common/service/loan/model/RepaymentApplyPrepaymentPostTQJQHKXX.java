package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "RepaymentApplyPrepaymentPostTQJQHKXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentApplyPrepaymentPostTQJQHKXX  implements Serializable {

    private String JKRXM;  //借款人姓名

    private String SYQS;  //剩余期数

    private String SYLX;  //剩余利息

    private String JKRZJHM;  //贷款账号

    private String BCHKJE;  //还款金额

    private String YDKKRQ;  //约定扣款日期

    private String SYBJ;  //剩余本金

    private String HKFS;//还款方式

    public String getHKFS() {
        return HKFS;
    }

    public void setHKFS(String HKFS) {
        this.HKFS = HKFS;
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

    public String getSYLX() {
        return SYLX;
    }

    public void setSYLX(String SYLX) {
        this.SYLX = SYLX;
    }


    public String getJKRZJHM() {
        return JKRZJHM;
    }

    public void setJKRZJHM(String JKRZJHM) {
        this.JKRZJHM = JKRZJHM;
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

    public String getBCHKJE() {
        return BCHKJE;
    }

    public void setBCHKJE(String BCHKJE) {
        this.BCHKJE = BCHKJE;
    }
}
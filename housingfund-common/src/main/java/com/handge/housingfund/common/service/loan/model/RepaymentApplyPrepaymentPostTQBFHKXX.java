package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "RepaymentApplyPrepaymentPostTQBFHKXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentApplyPrepaymentPostTQBFHKXX  implements Serializable {

    private String JYLX;  //节约利息

    private String BCHKJE;  //还款金额

    private String YDKKRQ;  //约定扣款日期

    private String JKRXM;  //借款人姓名

    private String SYBJ;  //剩余本金

    private String GYYCHKE;  //该月一次还款额

    private String MYDJ;  //每月递减

    private String SYLX;  //剩余利息

    private String XYHKE;  //新月还款额

    private String JKRZJHM;  //贷款账号


    private String YSYHKE;  //原首月还款额

    private String XZHHKQX;//新最后还款期限
    private String YZHHKQX;//原最后还款期限
    private String XMYDJ;//新每月递减
    private String SYQS;//剩余期数
    private String HKFS;//还款方式

    public String getHKFS() {
        return HKFS;
    }

    public void setHKFS(String HKFS) {
        this.HKFS = HKFS;
    }

    public String getSYLX() {
        return SYLX;
    }

    public void setSYLX(String SYLX) {
        this.SYLX = SYLX;
    }

    public String getXZHHKQX() {
        return XZHHKQX;
    }

    public void setXZHHKQX(String XZHHKQX) {
        this.XZHHKQX = XZHHKQX;
    }

    public String getYZHHKQX() {
        return YZHHKQX;
    }

    public void setYZHHKQX(String YZHHKQX) {
        this.YZHHKQX = YZHHKQX;
    }

    public String getXMYDJ() {
        return XMYDJ;
    }

    public void setXMYDJ(String XMYDJ) {
        this.XMYDJ = XMYDJ;
    }

    public String getSYQS() {
        return SYQS;
    }

    public void setSYQS(String SYQS) {
        this.SYQS = SYQS;
    }

    public String getJYLX() {

        return this.JYLX;

    }


    public void setJYLX(String JYLX) {

        this.JYLX = JYLX;

    }


    public String getBCHKJE() {
        return BCHKJE;
    }

    public void setBCHKJE(String BCHKJE) {
        this.BCHKJE = BCHKJE;
    }

    public String getYDKKRQ() {

        return this.YDKKRQ;

    }


    public void setYDKKRQ(String YDKKRQ) {

        this.YDKKRQ = YDKKRQ;

    }


    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }


    public String getSYBJ() {

        return this.SYBJ;

    }


    public void setSYBJ(String SYBJ) {

        this.SYBJ = SYBJ;

    }


    public String getGYYCHKE() {

        return this.GYYCHKE;

    }


    public void setGYYCHKE(String GYYCHKE) {

        this.GYYCHKE = GYYCHKE;

    }


    public String getMYDJ() {

        return this.MYDJ;

    }


    public void setMYDJ(String MYDJ) {

        this.MYDJ = MYDJ;

    }


    public String getXYHKE() {

        return this.XYHKE;

    }


    public void setXYHKE(String XYHKE) {

        this.XYHKE = XYHKE;

    }


    public String getJKRZJHM() {
        return JKRZJHM;
    }

    public void setJKRZJHM(String JKRZJHM) {
        this.JKRZJHM = JKRZJHM;
    }

    public String getYSYHKE() {

        return this.YSYHKE;

    }


    public void setYSYHKE(String YSYHKE) {

        this.YSYHKE = YSYHKE;

    }


}
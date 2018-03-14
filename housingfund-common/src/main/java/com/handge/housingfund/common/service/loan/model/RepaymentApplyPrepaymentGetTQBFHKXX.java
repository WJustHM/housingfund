package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "RepaymentApplyPrepaymentGetTQBFHKXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class RepaymentApplyPrepaymentGetTQBFHKXX  implements Serializable {

    private String JYLX;  //节约利息

    private String YWWD;  //业务网点

    private String HKJE;  //还款金额

    private String YDKKRQ;  //约定扣款日期

    private String JKRXM;  //借款人姓名

    private String SYBJ;  //剩余本金

    private String GYYCHKE;  //该月一次还款额

    private String MYDJ;  //每月递减

    private String SYJE;  //剩余利息

    private String XYHKE;  //新月还款额

    private String DKZH;  //贷款账号

    private String CZY;  //操作员

    private String YSYHKE;  //原首月还款额

    public String getJYLX() {

        return this.JYLX;

    }


    public void setJYLX(String JYLX) {

        this.JYLX = JYLX;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

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


    public String getSYJE() {

        return this.SYJE;

    }


    public void setSYJE(String SYJE) {

        this.SYJE = SYJE;

    }


    public String getXYHKE() {

        return this.XYHKE;

    }


    public void setXYHKE(String XYHKE) {

        this.XYHKE = XYHKE;

    }


    public String getDKZH() {

        return this.DKZH;

    }


    public void setDKZH(String DKZH) {

        this.DKZH = DKZH;

    }


    public String getCZY() {

        return this.CZY;

    }


    public void setCZY(String CZY) {

        this.CZY = CZY;

    }


    public String getYSYHKE() {

        return this.YSYHKE;

    }


    public void setYSYHKE(String YSYHKE) {

        this.YSYHKE = YSYHKE;

    }


    public String toString() {

        return "RepaymentApplyPrepaymentGetTQBFHKXX{" +

                "JYLX='" + this.JYLX + '\'' + "," +
                "YWWD='" + this.YWWD + '\'' + "," +
                "HKJE='" + this.HKJE + '\'' + "," +
                "YDKKRQ='" + this.YDKKRQ + '\'' + "," +
                "JKRXM='" + this.JKRXM + '\'' + "," +
                "SYBJ='" + this.SYBJ + '\'' + "," +
                "GYYCHKE='" + this.GYYCHKE + '\'' + "," +
                "MYDJ='" + this.MYDJ + '\'' + "," +
                "SYJE='" + this.SYJE + '\'' + "," +
                "XYHKE='" + this.XYHKE + '\'' + "," +
                "DKZH='" + this.DKZH + '\'' + "," +
                "CZY='" + this.CZY + '\'' + "," +
                "YSYHKE='" + this.YSYHKE + '\'' +

                "}";

    }
}
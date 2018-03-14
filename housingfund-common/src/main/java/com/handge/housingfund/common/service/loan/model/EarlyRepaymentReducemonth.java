package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Funnyboy on 2017/8/10.
 */
@XmlRootElement(name = "EarlyRepaymentReducemonth")
@XmlAccessorType(XmlAccessType.FIELD)
public class EarlyRepaymentReducemonth  implements Serializable {
    private static final long serialVersionUID = 4769500046822409516L;
    private String JKRXM;
    private String HKFS;
    private String SYBJ;
    private String SYLX;
    private String SYQS;
    private String YDKKRQ;
    private String BCHKJE;
    private String YSYHKE;
    private String MYDJ;
    private String YZHHKQX;
    private String GYYCHKE;
    private String XYHKE;
    private String XMYDJ;
    private String JYLX;
    private String XZHHKQX;//还款期限
    private String JKRZJHM;

    public String getBCHKJE() {
        return BCHKJE;
    }

    public void setBCHKJE(String BCHKJE) {
        this.BCHKJE = BCHKJE;
    }

    public String getJKRZJHM() {
        return JKRZJHM;
    }

    public void setJKRZJHM(String JKRZJHM) {
        this.JKRZJHM = JKRZJHM;
    }

    public String getJKRXM() {
        return JKRXM;
    }

    public void setJKRXM(String JKRXM) {
        this.JKRXM = JKRXM;
    }

    public String getHKFS() {
        return HKFS;
    }

    public void setHKFS(String HKFS) {
        this.HKFS = HKFS;
    }

    public String getSYBJ() {
        return SYBJ;
    }

    public void setSYBJ(String SYBJ) {
        this.SYBJ = SYBJ;
    }

    public String getSYLX() {
        return SYLX;
    }

    public void setSYLX(String SYLX) {
        this.SYLX = SYLX;
    }

    public String getSYQS() {
        return SYQS;
    }

    public void setSYQS(String SYQS) {
        this.SYQS = SYQS;
    }

    public String getYDKKRQ() {
        return YDKKRQ;
    }

    public void setYDKKRQ(String YDKKRQ) {
        this.YDKKRQ = YDKKRQ;
    }

    public String getYSYHKE() {
        return YSYHKE;
    }

    public void setYSYHKE(String YSYHKE) {
        this.YSYHKE = YSYHKE;
    }

    public String getMYDJ() {
        return MYDJ;
    }

    public void setMYDJ(String MYDJ) {
        this.MYDJ = MYDJ;
    }

    public String getYZHHKQX() {
        return YZHHKQX;
    }

    public void setYZHHKQX(String YZHHKQX) {
        this.YZHHKQX = YZHHKQX;
    }

    public String getGYYCHKE() {
        return GYYCHKE;
    }

    public void setGYYCHKE(String GYYCHKE) {
        this.GYYCHKE = GYYCHKE;
    }

    public String getXYHKE() {
        return XYHKE;
    }

    public void setXYHKE(String XYHKE) {
        this.XYHKE = XYHKE;
    }

    public String getXMYDJ() {
        return XMYDJ;
    }

    public void setXMYDJ(String XMYDJ) {
        this.XMYDJ = XMYDJ;
    }

    public String getJYLX() {
        return JYLX;
    }

    public void setJYLX(String JYLX) {
        this.JYLX = JYLX;
    }

    public String getXZHHKQX() {
        return XZHHKQX;
    }

    public void setXZHHKQX(String XZHHKQX) {
        this.XZHHKQX = XZHHKQX;
    }
}

package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Funnyboy on 2017/8/10.
 */
@XmlRootElement(name = "EarlySettlementLoan")
@XmlAccessorType(XmlAccessType.FIELD)
public class EarlySettlementLoan  implements Serializable {
    private static final long serialVersionUID = -6023809972662626737L;
    private String JKRXM;
    private String HKFS;
    private String SYBJ;
    private String SYLX;
    private String SYQS;
    private String XYHKZE;
    private String YDKKRQ;
    private String JKRZJHM;

    public String getJKRZJHM() {
        return JKRZJHM;
    }

    public void setJKRZJHM(String JKRZJHM) {
        this.JKRZJHM = JKRZJHM;
    }

    public String getYDKKRQ() {
        return YDKKRQ;
    }

    public void setYDKKRQ(String YDKKRQ) {
        this.YDKKRQ = YDKKRQ;
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

    public String getXYHKZE() {
        return XYHKZE;
    }

    public void setXYHKZE(String XYHKZE) {
        this.XYHKZE = XYHKZE;
    }
}

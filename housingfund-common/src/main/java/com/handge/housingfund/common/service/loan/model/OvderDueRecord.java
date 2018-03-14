package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Funnyboy on 2017/12/17.
 */
@XmlRootElement(name = "OvderDueRecord")
@XmlAccessorType(XmlAccessType.FIELD)
public class OvderDueRecord implements Serializable{

    private String id;
    private String DKZH;
    private BigDecimal DQQC;
    private String YWZT;

    public OvderDueRecord() {
    }


    public OvderDueRecord(String id, String DKZH, BigDecimal DQQC, String YWZT) {
        this.id = id;
        this.DKZH = DKZH;
        this.DQQC = DQQC;
        this.YWZT = YWZT;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDKZH() {
        return DKZH;
    }

    public void setDKZH(String DKZH) {
        this.DKZH = DKZH;
    }

    public BigDecimal getDQQC() {
        return DQQC;
    }

    public void setDQQC(BigDecimal DQQC) {
        this.DQQC = DQQC;
    }

    public String getYWZT() {
        return YWZT;
    }

    public void setYWZT(String YWZT) {
        this.YWZT = YWZT;
    }
}

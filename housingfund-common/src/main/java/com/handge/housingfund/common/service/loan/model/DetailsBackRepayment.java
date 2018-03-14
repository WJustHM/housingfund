package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Funnyboy on 2017/12/17.
 */
@XmlRootElement(name = "DetailsBackRepayment")
@XmlAccessorType(XmlAccessType.FIELD)
public class DetailsBackRepayment implements Serializable {

    private String id;
    private BigDecimal FSE;
    private BigDecimal LXJE;
    private BigDecimal BJJE;
    private String  YWLSH;


    public DetailsBackRepayment() {
    }


    public DetailsBackRepayment(String id,BigDecimal FSE, BigDecimal LXJE, BigDecimal BJJE, String YWLSH) {
        this.FSE = FSE;
        this.LXJE = LXJE;
        this.BJJE = BJJE;
        this.YWLSH = YWLSH;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getFSE() {
        return FSE;
    }

    public void setFSE(BigDecimal FSE) {
        this.FSE = FSE;
    }

    public BigDecimal getLXJE() {
        return LXJE;
    }

    public void setLXJE(BigDecimal LXJE) {
        this.LXJE = LXJE;
    }

    public BigDecimal getBJJE() {
        return BJJE;
    }

    public void setBJJE(BigDecimal BJJE) {
        this.BJJE = BJJE;
    }

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }
}

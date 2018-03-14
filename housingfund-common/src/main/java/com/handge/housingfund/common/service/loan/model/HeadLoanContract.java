package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Funnyboy on 2017/10/12.
 */
@XmlRootElement(name = "HeadLoanContract")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadLoanContract implements Serializable {


    private static final long serialVersionUID = 8815705087738613750L;
    private LoanContractChangeBefore  loanContractChangeBefore;
    private LoanContractChangeAfter loanContractChangeAfter;
    private String YWLSH;
    private String YWWD;
    private String TZRQ;
    private String JKHTBH;
    private String CZY;

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String getTZRQ() {
        return TZRQ;
    }

    public void setTZRQ(String TZRQ) {
        this.TZRQ = TZRQ;
    }

    public String getJKHTBH() {
        return JKHTBH;
    }

    public void setJKHTBH(String JKHTBH) {
        this.JKHTBH = JKHTBH;
    }


    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public LoanContractChangeBefore getLoanContractChangeBefore() {
        return loanContractChangeBefore;
    }

    public void setLoanContractChangeBefore(LoanContractChangeBefore loanContractChangeBefore) {
        this.loanContractChangeBefore = loanContractChangeBefore;
    }

    public LoanContractChangeAfter getLoanContractChangeAfter() {
        return loanContractChangeAfter;
    }

    public void setLoanContractChangeAfter(LoanContractChangeAfter loanContractChangeAfter) {
        this.loanContractChangeAfter = loanContractChangeAfter;
    }

    @Override
    public String toString() {
        return "HeadLoanContract{" +
                "loanContractChangeBefore=" + loanContractChangeBefore +
                ", loanContractChangeAfter=" + loanContractChangeAfter +
                ", YWLSH='" + YWLSH + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", TZRQ='" + TZRQ + '\'' +
                ", JKHTBH='" + JKHTBH + '\'' +
                ", CZY='" + CZY + '\'' +
                '}';
    }
}

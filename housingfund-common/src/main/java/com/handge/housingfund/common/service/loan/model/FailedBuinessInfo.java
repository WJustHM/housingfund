package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Funnyboy on 2017/8/30.
 */
@XmlRootElement(name = "FailedBuinessInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class FailedBuinessInfo implements Serializable {

    private String YWLSH;

    private String ID;

    private String JKRXM;

    private String DKZH;

    private String YWFSRQ;

    private String SWTYH;

    private String FSE;

    private String BJJE;

    private String LXJE;

    private String DQQC;

    private String SBYY;

    private String RGCL;

    private String YWLX;

    private String ZJHM;

    private String id;

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getSWTYH() {
        return SWTYH;
    }

    public void setSWTYH(String SWTYH) {
        this.SWTYH = SWTYH;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYWLX() {
        return YWLX;
    }

    public void setYWLX(String YWLX) {
        this.YWLX = YWLX;
    }

    public String getRGCL() {
        return RGCL;
    }

    public void setRGCL(String RGCL) {
        this.RGCL = RGCL;
    }

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public String getJKRXM() {
        return JKRXM;
    }

    public void setJKRXM(String JKRXM) {
        this.JKRXM = JKRXM;
    }

    public String getDKZH() {
        return DKZH;
    }

    public void setDKZH(String DKZH) {
        this.DKZH = DKZH;
    }

    public String getYWFSRQ() {
        return YWFSRQ;
    }

    public void setYWFSRQ(String YWFSRQ) {
        this.YWFSRQ = YWFSRQ;
    }

    public String getFSE() {
        return FSE;
    }

    public void setFSE(String FSE) {
        this.FSE = FSE;
    }

    public String getBJJE() {
        return BJJE;
    }

    public void setBJJE(String BJJE) {
        this.BJJE = BJJE;
    }

    public String getLXJE() {
        return LXJE;
    }

    public void setLXJE(String LXJE) {
        this.LXJE = LXJE;
    }

    public String getDQQC() {
        return DQQC;
    }

    public void setDQQC(String DQQC) {
        this.DQQC = DQQC;
    }

    public String getSBYY() {
        return SBYY;
    }

    public void setSBYY(String SBYY) {
        this.SBYY = SBYY;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


    @Override
    public String toString() {
        return "FailedBuinessInfo{" +
                "YWLSH='" + YWLSH + '\'' +
                ", ID='" + ID + '\'' +
                ", JKRXM='" + JKRXM + '\'' +
                ", DKZH='" + DKZH + '\'' +
                ", YWFSRQ='" + YWFSRQ + '\'' +
                ", FSE='" + FSE + '\'' +
                ", BJJE='" + BJJE + '\'' +
                ", LXJE='" + LXJE + '\'' +
                ", DQQC='" + DQQC + '\'' +
                ", SBYY='" + SBYY + '\'' +
                '}';
    }
}

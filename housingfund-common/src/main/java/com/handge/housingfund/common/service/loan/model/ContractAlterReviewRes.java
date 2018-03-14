package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Funnyboy on 2017/8/17.
 */
@XmlRootElement(name = "ContractAlterReviewRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContractAlterReviewRes implements Serializable {

    private String YWLSH;   //业务流水号

    private String JKHTBH;  //借款合同编号

    private String BGLX;    //变更类型

    private String CZY;     //操作员

    private String ZhuangTai;   //状态

    private String YWWD;    //业务网点

    private String SLSJ;    //受理时间

    private String DDSJ;    //到达时间

    private String SCSHY;   //上次审核员

    private String DQSHY;

    private String DQXM;

    private String SFTS;

    private String id;

    private String JKRXM;

    @Override
    public String toString() {
        return "ContractAlterReviewRes{" +
                "YWLSH='" + YWLSH + '\'' +
                ", JKHTBH='" + JKHTBH + '\'' +
                ", BGLX='" + BGLX + '\'' +
                ", CZY='" + CZY + '\'' +
                ", ZhuangTai='" + ZhuangTai + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", SLSJ='" + SLSJ + '\'' +
                ", DDSJ='" + DDSJ + '\'' +
                ", SCSHY='" + SCSHY + '\'' +
                ", DQSHY='" + DQSHY + '\'' +
                ", DQXM='" + DQXM + '\'' +
                ", SFTS='" + SFTS + '\'' +
                ", id='" + id + '\'' +
                ", JKRXM='" + JKRXM + '\'' +
                '}';
    }

    public String getJKRXM() {
        return JKRXM;
    }

    public void setJKRXM(String JKRXM) {
        this.JKRXM = JKRXM;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSFTS() {
        return SFTS;
    }

    public void setSFTS(String SFTS) {
        this.SFTS = SFTS;
    }

    public String getDQSHY() {
        return DQSHY;
    }

    public void setDQSHY(String DQSHY) {
        this.DQSHY = DQSHY;
    }

    public String getDQXM() {
        return DQXM;
    }

    public void setDQXM(String DQXM) {
        this.DQXM = DQXM;
    }

    public String getSCSHY() {
        return SCSHY;
    }

    public void setSCSHY(String SCSHY) {
        this.SCSHY = SCSHY;
    }

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public String getJKHTBH() {
        return JKHTBH;
    }

    public void setJKHTBH(String JKHTBH) {
        this.JKHTBH = JKHTBH;
    }

    public String getBGLX() {
        return BGLX;
    }

    public void setBGLX(String BGLX) {
        this.BGLX = BGLX;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getZhuangTai() {
        return ZhuangTai;
    }

    public void setZhuangTai(String zhuangTai) {
        ZhuangTai = zhuangTai;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String getSLSJ() {
        return SLSJ;
    }

    public void setSLSJ(String SLSJ) {
        this.SLSJ = SLSJ;
    }

    public String getDDSJ() {
        return DDSJ;
    }

    public void setDDSJ(String DDSJ) {
        this.DDSJ = DDSJ;
    }

}

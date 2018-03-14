package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "LoanContractListResponseRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanContractListResponseRes  implements Serializable {

    private static final long serialVersionUID = 9010190507160856571L;

    private String DKZH;    //贷款账号

    private String YWLSH;  //业务流水号

    private String JKHTBH;  //借款合同编号

    private String ZhuangTai;  //状态

    private String CZY;  //操作员

    private String YWWD;  //业务网点

    private String SLSJ;    //受理时间

    private String ZJHM;

    private String JKRXM;

    private String  id;

    public String getDKZH() {
        return DKZH;
    }

    public void setDKZH(String DKZH) {
        this.DKZH = DKZH;
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

    public String getSLSJ() {
        return SLSJ;
    }

    public void setSLSJ(String SLSJ) {
        this.SLSJ = SLSJ;
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

    public String getZhuangTai() {
        return ZhuangTai;
    }

    public void setZhuangTai(String zhuangTai) {
        ZhuangTai = zhuangTai;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    @Override
    public String toString() {
        return "LoanContractListResponseRes{" +
                "DKZH='" + DKZH + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", JKHTBH='" + JKHTBH + '\'' +
                ", ZhuangTai='" + ZhuangTai + '\'' +
                ", CZY='" + CZY + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", SLSJ='" + SLSJ + '\'' +
                ", ZJHM='" + ZJHM + '\'' +
                ", JKRXM='" + JKRXM + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
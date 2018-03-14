package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "LoansResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoansResponse  implements Serializable {

    private String DKFFRQ;  //贷款发放日期

    private String JKRXM;  //借款人姓名

    private String YWLSH;  //业务流水号

    private String ZhuangTai;  //状态

    private String CWPZH;  //财务凭证号

    private String YWWD;  //业务网点

    private String JKHTBH;  //借款合同编号

    private String DKZH;  //贷款账号

    private String CZY;  //操作员

    private String DKFFE;  //贷款发放额

    private String XuHao;  //序号

    public String getDKFFRQ() {

        return this.DKFFRQ;

    }


    public void setDKFFRQ(String DKFFRQ) {

        this.DKFFRQ = DKFFRQ;

    }


    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }


    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getZhuangTai() {

        return this.ZhuangTai;

    }


    public void setZhuangTai(String ZhuangTai) {

        this.ZhuangTai = ZhuangTai;

    }


    public String getCWPZH() {

        return this.CWPZH;

    }


    public void setCWPZH(String CWPZH) {

        this.CWPZH = CWPZH;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String getJKHTBH() {

        return this.JKHTBH;

    }


    public void setJKHTBH(String JKHTBH) {

        this.JKHTBH = JKHTBH;

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


    public String getDKFFE() {

        return this.DKFFE;

    }


    public void setDKFFE(String DKFFE) {

        this.DKFFE = DKFFE;

    }


    public String getXuHao() {

        return this.XuHao;

    }


    public void setXuHao(String XuHao) {

        this.XuHao = XuHao;

    }


    public String toString() {

        return "LoansResponse{" +

                "DKFFRQ='" + this.DKFFRQ + '\'' + "," +
                "JKRXM='" + this.JKRXM + '\'' + "," +
                "YWLSH='" + this.YWLSH + '\'' + "," +
                "ZhuangTai='" + this.ZhuangTai + '\'' + "," +
                "CWPZH='" + this.CWPZH + '\'' + "," +
                "YWWD='" + this.YWWD + '\'' + "," +
                "JKHTBH='" + this.JKHTBH + '\'' + "," +
                "DKZH='" + this.DKZH + '\'' + "," +
                "CZY='" + this.CZY + '\'' + "," +
                "DKFFE='" + this.DKFFE + '\'' + "," +
                "XuHao='" + this.XuHao + '\'' +

                "}";

    }
}
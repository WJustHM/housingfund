package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "CommissionedDebitListResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommissionedDebitListResponse  implements Serializable {

    private String JKRXM;  //借款人姓名

    private String DKHKZH;  //贷款还款账号

    private String HTDKJE;  //合同贷款金额

    private String YWLSH;  //业务流水号

    private String ZhuangTai;  //状态

    private String JKRZJHM;  //借款人证件号码

    private String DKQS;  //贷款期数

    private String WTKHFS;  //委托扣划方式

    private String XuHao;  //序号

    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }


    public String getDKHKZH() {

        return this.DKHKZH;

    }


    public void setDKHKZH(String DKHKZH) {

        this.DKHKZH = DKHKZH;

    }


    public String getHTDKJE() {

        return this.HTDKJE;

    }


    public void setHTDKJE(String HTDKJE) {

        this.HTDKJE = HTDKJE;

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


    public String getJKRZJHM() {

        return this.JKRZJHM;

    }


    public void setJKRZJHM(String JKRZJHM) {

        this.JKRZJHM = JKRZJHM;

    }


    public String getDKQS() {

        return this.DKQS;

    }


    public void setDKQS(String DKQS) {

        this.DKQS = DKQS;

    }


    public String getWTKHFS() {

        return this.WTKHFS;

    }


    public void setWTKHFS(String WTKHFS) {

        this.WTKHFS = WTKHFS;

    }


    public String getXuHao() {

        return this.XuHao;

    }


    public void setXuHao(String XuHao) {

        this.XuHao = XuHao;

    }


    public String toString() {

        return "CommissionedDebitListResponse{" +

                "JKRXM='" + this.JKRXM + '\'' + "," +
                "DKHKZH='" + this.DKHKZH + '\'' + "," +
                "HTDKJE='" + this.HTDKJE + '\'' + "," +
                "YWLSH='" + this.YWLSH + '\'' + "," +
                "ZhuangTai='" + this.ZhuangTai + '\'' + "," +
                "JKRZJHM='" + this.JKRZJHM + '\'' + "," +
                "DKQS='" + this.DKQS + '\'' + "," +
                "WTKHFS='" + this.WTKHFS + '\'' + "," +
                "XuHao='" + this.XuHao + '\'' +

                "}";

    }
}
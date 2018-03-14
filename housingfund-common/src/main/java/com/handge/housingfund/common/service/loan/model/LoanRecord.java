package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "LoanRecord")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoanRecord  implements Serializable {

    private String SPBWJ;  //审批表文件

    private String HTWJ;    //合同文件

    private String HTDKJE;  //合同贷款金额

    private String JKHTH;  //借款合同号

    private String YWWD;  //业务网点

    private String DKYT;  //贷款用途

    private String DKQS;  //贷款期数

    private String DKZH;  //贷款账号

    private String CZY;  //操作员

    private String JKRXM;  //借款人姓名

    private String JKRZJHM; //借款人证件号码

    private String DKZHZT;  //贷款账户状态

    private String SWTYH;   //受委托银行

    private String FKRQ; //放款日期

    private String DQRQ; //到期日期

    private  String GTJKRXM; //共同借款人姓名

    private String GTJKRZJHM;//共同借款人证件号码

    public String getGTJKRXM() {
        return GTJKRXM;
    }

    public void setGTJKRXM(String GTJKRXM) {
        this.GTJKRXM = GTJKRXM;
    }

    public String getGTJKRZJHM() {
        return GTJKRZJHM;
    }

    public void setGTJKRZJHM(String GTJKRZJHM) {
        this.GTJKRZJHM = GTJKRZJHM;
    }
    public String getDQRQ() {
        return DQRQ;
    }

    public void setDQRQ(String DQRQ) {
        this.DQRQ = DQRQ;
    }

    public String getFKRQ() {
        return FKRQ;
    }

    public void setFKRQ(String FKRQ) {
        this.FKRQ = FKRQ;
    }

    public String getSWTYH() {
        return SWTYH;
    }

    public void setSWTYH(String SWTYH) {
        this.SWTYH = SWTYH;
    }

    public String getJKRZJHM() {
        return JKRZJHM;
    }

    public void setJKRZJHM(String JKRZJHM) {
        this.JKRZJHM = JKRZJHM;
    }

    public String getHTWJ() {
        return HTWJ;
    }

    public void setHTWJ(String HTWJ) {
        this.HTWJ = HTWJ;
    }

    public String getSPBWJ() {

        return this.SPBWJ;

    }


    public void setSPBWJ(String SPBWJ) {

        this.SPBWJ = SPBWJ;

    }


    public String getHTDKJE() {

        return this.HTDKJE;

    }


    public void setHTDKJE(String HTDKJE) {

        this.HTDKJE = HTDKJE;

    }


    public String getJKHTH() {

        return this.JKHTH;

    }


    public void setJKHTH(String JKHTH) {

        this.JKHTH = JKHTH;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

    }


    public String getDKYT() {

        return this.DKYT;

    }


    public void setDKYT(String DKYT) {

        this.DKYT = DKYT;

    }


    public String getDKQS() {

        return this.DKQS;

    }


    public void setDKQS(String DKQS) {

        this.DKQS = DKQS;

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


    public String getJKRXM() {

        return this.JKRXM;

    }


    public void setJKRXM(String JKRXM) {

        this.JKRXM = JKRXM;

    }

    public String getDKZHZT() {
        return DKZHZT;
    }

    public void setDKZHZT(String DKZHZT) {
        this.DKZHZT = DKZHZT;
    }

    @Override
    public String toString() {
        return "LoanRecord{" +
                "SPBWJ='" + SPBWJ + '\'' +
                ", HTWJ='" + HTWJ + '\'' +
                ", HTDKJE='" + HTDKJE + '\'' +
                ", JKHTH='" + JKHTH + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", DKYT='" + DKYT + '\'' +
                ", DKQS='" + DKQS + '\'' +
                ", DKZH='" + DKZH + '\'' +
                ", CZY='" + CZY + '\'' +
                ", JKRXM='" + JKRXM + '\'' +
                ", JKRZJHM='" + JKRZJHM + '\'' +
                ", DKZHZT='" + DKZHZT + '\'' +
                ", SWTYH='" + SWTYH + '\'' +
                '}';
    }
}
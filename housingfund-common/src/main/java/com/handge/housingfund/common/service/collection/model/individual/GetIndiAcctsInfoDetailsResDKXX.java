package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by sjw on 2017/8/18.
 */
@XmlRootElement(name = "GetIndiAcctsInfoDetailsResDKXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetIndiAcctsInfoDetailsResDKXX  implements Serializable{

    private  String JKHTBH; //借款合同编号

    private  String DKZH;   //贷款账号

    private  String HTDKJE;  //合同贷款金（元）

    private  String DKYE;  //贷款余额（元）

    private  String DKQS; //贷款期数

    private  String SYQS; //剩余期数

    private  String HSLXZE; //回收利息总额（元）

    private  String YQBJ; //逾期本金（元）

    private  String YQLX; //逾期利息（元）

    private  String YQFX; //逾期罚息（元）

    private  String GRZHZT; //个人账户状态

    private  String HKRQ; //还款日期

    private  String XingMing; //姓名

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

    public String getXingMing() {
        return XingMing;
    }

    public void setXingMing(String xingMing) {
        XingMing = xingMing;
    }

    public String getJKHTBH() {
        return JKHTBH;
    }

    public void setJKHTBH(String JKHTBH) {
        this.JKHTBH = JKHTBH;
    }

    public String getDKZH() {
        return DKZH;
    }

    public void setDKZH(String DKZH) {
        this.DKZH = DKZH;
    }

    public String getHTDKJ() {
        return HTDKJE;
    }

    public void setHTDKJ(String HTDKJ) {
        this.HTDKJE = HTDKJ;
    }

    public String getDKYE() {
        return DKYE;
    }

    public void setDKYE(String DKYE) {
        this.DKYE = DKYE;
    }

    public String getDKQS() {
        return DKQS;
    }

    public void setDKQS(String DKQS) {
        this.DKQS = DKQS;
    }

    public String getSYQS() {
        return SYQS;
    }

    public void setSYQS(String SYQS) {
        this.SYQS = SYQS;
    }

    public String getHSLXZE() {
        return HSLXZE;
    }

    public void setHSLXZE(String HSLXZE) {
        this.HSLXZE = HSLXZE;
    }

    public String getYQBJ() {
        return YQBJ;
    }

    public void setYQBJ(String YQJE) {
        this.YQBJ = YQJE;
    }

    public String getYQLX() {
        return YQLX;
    }

    public void setYQLX(String YQLX) {
        this.YQLX = YQLX;
    }

    public String getYQFX() {
        return YQFX;
    }

    public void setYQFX(String YQFX) {
        this.YQFX = YQFX;
    }

    public String getGRZHZT() {
        return GRZHZT;
    }

    public void setGRZHZT(String GRZHZT) {
        this.GRZHZT = GRZHZT;
    }

    public String getHKRQ() {
        return HKRQ;
    }

    public void setHKRQ(String HKRQ) {
        this.HKRQ = HKRQ;
    }

    public String  tostring(){

        return "GetIndiAcctsInfoDetailsResDKXX{" +

                "JKHTBH='" + this.JKHTBH + '\'' +
                "DKZH='" + this.DKZH + '\'' +
                "HTDKJ='" + this.HTDKJE + '\'' +
                "DKYE='" + this.DKYE + '\'' +
                "DKQS='" + this.DKQS + '\'' +
                "SYQS='" + this.SYQS + '\'' +
                "HSLXZE='" + this.HSLXZE + '\'' +
                "YQBJ='" + this.YQBJ + '\'' +
                "YQLX='" + this.YQLX + '\'' +
                "YQFX='" + this.YQFX + '\'' +
                "GRZHZT='" + this.GRZHZT + '\'' +
                "HKRQ='" + this.HKRQ + '\'' +
                "XingMing='" + this.XingMing + '\'' +
                "}";
    }
}

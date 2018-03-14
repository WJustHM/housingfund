package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "BorrowerChangePost")
@XmlAccessorType(XmlAccessType.FIELD)
public class BorrowerChangePost  implements Serializable {

    private String BLZL;  //办理资料

    private String JKRGJJZH;  //借款人公积金账号

    private String JKRZJLX;  //借款人证件类型

    private BorrowerChargePostBGHGTJKRXX BGHGTJKRXX;  //变更后共同借款人信息

    private BorrowerChargePostBGHJKRXX BGHJKRXX;  //变更后借款人信息

    private String JKHTBH;  //借款合同编号

    private String JKRZJHM;  //借款人证件号码

    private String YWWD;  //业务网点

    private String DKZH;  //贷款账号

    private String CZY;  //操作员

    private String JKRXM;  //借款人姓名

    public String getBLZL() {

        return this.BLZL;

    }


    public void setBLZL(String BLZL) {

        this.BLZL = BLZL;

    }


    public String getJKRGJJZH() {

        return this.JKRGJJZH;

    }


    public void setJKRGJJZH(String JKRGJJZH) {

        this.JKRGJJZH = JKRGJJZH;

    }


    public String getJKRZJLX() {

        return this.JKRZJLX;

    }


    public void setJKRZJLX(String JKRZJLX) {

        this.JKRZJLX = JKRZJLX;

    }


    public BorrowerChargePostBGHGTJKRXX getBGHGTJKRXX() {

        return this.BGHGTJKRXX;

    }


    public void setBGHGTJKRXX(BorrowerChargePostBGHGTJKRXX BGHGTJKRXX) {

        this.BGHGTJKRXX = BGHGTJKRXX;

    }


    public BorrowerChargePostBGHJKRXX getBGHJKRXX() {

        return this.BGHJKRXX;

    }


    public void setBGHJKRXX(BorrowerChargePostBGHJKRXX BGHJKRXX) {

        this.BGHJKRXX = BGHJKRXX;

    }


    public String getJKHTBH() {

        return this.JKHTBH;

    }


    public void setJKHTBH(String JKHTBH) {

        this.JKHTBH = JKHTBH;

    }


    public String getJKRZJHM() {

        return this.JKRZJHM;

    }


    public void setJKRZJHM(String JKRZJHM) {

        this.JKRZJHM = JKRZJHM;

    }


    public String getYWWD() {

        return this.YWWD;

    }


    public void setYWWD(String YWWD) {

        this.YWWD = YWWD;

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


    public String toString() {

        return "BorrowerChangePost{" +

                "BLZL='" + this.BLZL + '\'' + "," +
                "JKRGJJZH='" + this.JKRGJJZH + '\'' + "," +
                "JKRZJLX='" + this.JKRZJLX + '\'' + "," +
                "BGHGTJKRXX='" + this.BGHGTJKRXX + '\'' + "," +
                "BGHJKRXX='" + this.BGHJKRXX + '\'' + "," +
                "JKHTBH='" + this.JKHTBH + '\'' + "," +
                "JKRZJHM='" + this.JKRZJHM + '\'' + "," +
                "YWWD='" + this.YWWD + '\'' + "," +
                "DKZH='" + this.DKZH + '\'' + "," +
                "CZY='" + this.CZY + '\'' + "," +
                "JKRXM='" + this.JKRXM + '\'' +

                "}";

    }
}
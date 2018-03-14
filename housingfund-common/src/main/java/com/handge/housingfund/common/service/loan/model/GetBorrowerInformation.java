package com.handge.housingfund.common.service.loan.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Funnyboy on 2017/8/16.
 */
@XmlRootElement(name = "GetBorrowerInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetBorrowerInformation implements Serializable {
    private static final long serialVersionUID = -4309817787377771754L;
    private BorrowerInformation JKRXX;//借款人信息
    private CommonBorrowerInformation GTJKRXX;//共同借款人信息

    private String JKRGJGZH;

    private String JKHTBH;

    private String DKZH;

    private String CZY;

    private String YWWD;

    private ArrayList<String> DELTAJKR;

    private ArrayList<String> DELTAGTJKR;

    public String getJKRGJGZH() {
        return JKRGJGZH;
    }

    public void setJKRGJGZH(String JKRGJGZH) {
        this.JKRGJGZH = JKRGJGZH;
    }

    public BorrowerInformation getJKRXX() {
        return JKRXX;
    }

    public void setJKRXX(BorrowerInformation JKRXX) {
        this.JKRXX = JKRXX;
    }

    public CommonBorrowerInformation getGTJKRXX() {
        return GTJKRXX;
    }

    public void setGTJKRXX(CommonBorrowerInformation GTJKRXX) {
        this.GTJKRXX = GTJKRXX;
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

    public String getJKHTBH() {
        return JKHTBH;
    }

    public void setJKHTBH(String JKHTBH) {
        this.JKHTBH = JKHTBH;
    }

    public ArrayList<String> getDELTAJKR() {
        return DELTAJKR;
    }

    public void setDELTAJKR(ArrayList<String> DELTAJKR) {
        this.DELTAJKR = DELTAJKR;
    }

    public ArrayList<String> getDELTAGTJKR() {
        return DELTAGTJKR;
    }

    public void setDELTAGTJKR(ArrayList<String> DELTAGTJKR) {
        this.DELTAGTJKR = DELTAGTJKR;
    }

    public String getDKZH() {
        return DKZH;
    }

    public void setDKZH(String DKZH) {
        this.DKZH = DKZH;
    }
}

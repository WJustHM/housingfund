package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;


@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection", "serial"})
@XmlRootElement(name = "TransferOutNotReviewedListModleResults")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferOutNotReviewedListModleResults implements Serializable {


    private String YWLSH;//业务流水号

    private String LXHBH;//联系函编号

    private String ZRGJJZXMC;//转入公积金中心名称

    private String ZGXM;//职工姓名

    private String ZJLX;//证件类型

    private String ZJHM;//证件号码

    private String DDSJ;//到达时间

    private String ZYJE;//转移金额

    private String YZHBJJE;//原账户本金金额

    private String BNDLX;//本年度利息

    private String SFTS;//是否特审

    private String DQXM;

    private String DQSHY;

    private String CZY;

    private String YWWD;

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

    @Override
    public String toString() {
        return "TransferOutNotReviewedListModleResults{" +
                "YWLSH='" + YWLSH + '\'' +
                ", LXHBH='" + LXHBH + '\'' +
                ", ZRGJJZXMC='" + ZRGJJZXMC + '\'' +
                ", ZGXM='" + ZGXM + '\'' +
                ", ZJLX='" + ZJLX + '\'' +
                ", ZJHM='" + ZJHM + '\'' +
                ", DDSJ='" + DDSJ + '\'' +
                ", ZYJE='" + ZYJE + '\'' +
                ", YZHBJJE='" + YZHBJJE + '\'' +
                ", BNDLX='" + BNDLX + '\'' +
                ", SFTS='" + SFTS + '\'' +
                ", DQXM='" + DQXM + '\'' +
                ", DQSHY='" + DQSHY + '\'' +
                ", CZY='" + CZY + '\'' +
                ", YWWD='" + YWWD + '\'' +
                '}';
    }

    public TransferOutNotReviewedListModleResults(String YWLSH, String LXHBH, String ZRGJJZXMC, String ZGXM, String ZJLX, String ZJHM, String DDSJ, String ZYJE, String YZHBJJE, String BNDLX, String SFTS, String DQXM, String DQSHY) {
        this.YWLSH = YWLSH;
        this.LXHBH = LXHBH;
        this.ZRGJJZXMC = ZRGJJZXMC;
        this.ZGXM = ZGXM;
        this.ZJLX = ZJLX;
        this.ZJHM = ZJHM;
        this.DDSJ = DDSJ;
        this.ZYJE = ZYJE;
        this.YZHBJJE = YZHBJJE;
        this.BNDLX = BNDLX;
        this.SFTS = SFTS;
        this.DQXM = DQXM;
        this.DQSHY = DQSHY;
    }

    public String getDQXM() {
        return DQXM;
    }

    public void setDQXM(String DQXM) {
        this.DQXM = DQXM;
    }

    public String getDQSHY() {
        return DQSHY;
    }

    public void setDQSHY(String DQSHY) {
        this.DQSHY = DQSHY;
    }


    public TransferOutNotReviewedListModleResults() {
    }

    /**
     * 业务流水号
     **/
    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    /**
     * 联系函编号
     **/
    public String getLXHBH() {
        return LXHBH;
    }

    public void setLXHBH(String LXHBH) {
        this.LXHBH = LXHBH;
    }

    /**
     * 转入公积金中心名称
     **/
    public String getZRGJJZXMC() {
        return ZRGJJZXMC;
    }

    public void setZRGJJZXMC(String ZRGJJZXMC) {
        this.ZRGJJZXMC = ZRGJJZXMC;
    }

    /**
     * 职工姓名
     **/
    public String getZGXM() {
        return ZGXM;
    }

    public void setZGXM(String ZGXM) {
        this.ZGXM = ZGXM;
    }

    /**
     * 证件类型
     **/
    public String getZJLX() {
        return ZJLX;
    }

    public void setZJLX(String ZJLX) {
        this.ZJLX = ZJLX;
    }

    /**
     * 证件号码
     **/
    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    /**
     * 到达时间
     **/
    public String getDDSJ() {
        return DDSJ;
    }

    public void setDDSJ(String DDSJ) {
        this.DDSJ = DDSJ;
    }

    /**
     * 转移金额
     **/
    public String getZYJE() {
        return ZYJE;
    }

    public void setZYJE(String ZYJE) {
        this.ZYJE = ZYJE;
    }

    /**
     * 原账户本金金额
     **/
    public String getYZHBJJE() {
        return YZHBJJE;
    }

    public void setYZHBJJE(String YZHBJJE) {
        this.YZHBJJE = YZHBJJE;
    }

    /**
     * 本年度利息
     **/
    public String getBNDLX() {
        return BNDLX;
    }

    public void setBNDLX(String BNDLX) {
        this.BNDLX = BNDLX;
    }

    /**
     * 是否特审
     **/
    public String getSFTS() {
        return SFTS;
    }

    public void setSFTS(String SFTS) {
        this.SFTS = SFTS;
    }


    public void checkValidation() {

    }
}

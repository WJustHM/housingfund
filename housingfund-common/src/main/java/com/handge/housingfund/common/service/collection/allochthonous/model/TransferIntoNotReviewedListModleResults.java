package com.handge.housingfund.common.service.collection.allochthonous.model;

import com.handge.housingfund.common.service.util.*;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;


@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection", "serial"})
@XmlRootElement(name = "TransferIntoNotReviewedListModleResults")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferIntoNotReviewedListModleResults implements Serializable {


    private String YWLSH;//业务流水号

    private String LXHBH;//联系函编号

    private String ZGXM;//职工姓名

    private String ZJHM;//证件号码

    private String ZCGJJZXMC;//转出公积金中心名称

    private String YGZDWMC;//原工作单位名称

    private String XGRZFGJJZH;//现个人住房公积金账号

    private String XDWMC;//现单位名称

    private String DDSJ;//到达时间

    private String SFTS;//是否特审(1 or 0)

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
        return "TransferIntoNotReviewedListModleResults{" +
                "YWLSH='" + YWLSH + '\'' +
                ", LXHBH='" + LXHBH + '\'' +
                ", ZGXM='" + ZGXM + '\'' +
                ", ZJHM='" + ZJHM + '\'' +
                ", ZCGJJZXMC='" + ZCGJJZXMC + '\'' +
                ", YGZDWMC='" + YGZDWMC + '\'' +
                ", XGRZFGJJZH='" + XGRZFGJJZH + '\'' +
                ", XDWMC='" + XDWMC + '\'' +
                ", DDSJ='" + DDSJ + '\'' +
                ", SFTS='" + SFTS + '\'' +
                ", DQXM='" + DQXM + '\'' +
                ", DQSHY='" + DQSHY + '\'' +
                ", CZY='" + CZY + '\'' +
                ", YWWD='" + YWWD + '\'' +
                '}';
    }

    public TransferIntoNotReviewedListModleResults(String YWLSH, String LXHBH, String ZGXM, String ZJHM, String ZCGJJZXMC, String YGZDWMC, String XGRZFGJJZH, String XDWMC, String DDSJ, String SFTS, String DQXM, String DQSHY) {
        this.YWLSH = YWLSH;
        this.LXHBH = LXHBH;
        this.ZGXM = ZGXM;
        this.ZJHM = ZJHM;
        this.ZCGJJZXMC = ZCGJJZXMC;
        this.YGZDWMC = YGZDWMC;
        this.XGRZFGJJZH = XGRZFGJJZH;
        this.XDWMC = XDWMC;
        this.DDSJ = DDSJ;
        this.SFTS = SFTS;
        this.DQXM = DQXM;
        this.DQSHY = DQSHY;
    }

    public TransferIntoNotReviewedListModleResults() {
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
     * 职工姓名
     **/
    public String getZGXM() {
        return ZGXM;
    }

    public void setZGXM(String ZGXM) {
        this.ZGXM = ZGXM;
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
     * 转出公积金中心名称
     **/
    public String getZCGJJZXMC() {
        return ZCGJJZXMC;
    }

    public void setZCGJJZXMC(String ZCGJJZXMC) {
        this.ZCGJJZXMC = ZCGJJZXMC;
    }

    /**
     * 原工作单位名称
     **/
    public String getYGZDWMC() {
        return YGZDWMC;
    }

    public void setYGZDWMC(String YGZDWMC) {
        this.YGZDWMC = YGZDWMC;
    }

    /**
     * 现个人住房公积金账号
     **/
    public String getXGRZFGJJZH() {
        return XGRZFGJJZH;
    }

    public void setXGRZFGJJZH(String XGRZFGJJZH) {
        this.XGRZFGJJZH = XGRZFGJJZH;
    }

    /**
     * 现单位名称
     **/
    public String getXDWMC() {
        return XDWMC;
    }

    public void setXDWMC(String XDWMC) {
        this.XDWMC = XDWMC;
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
     * 是否特审(1 or 0)
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

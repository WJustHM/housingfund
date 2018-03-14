package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by xuefei_wang on 17-8-15.
 */
@XmlRootElement(name = "账套")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountBookModel implements Serializable{

    private static final long serialVersionUID = -7427770894843257397L;
    //账套名称
    private String ZTMC;
    //会计主管
    private String KJZG;
    //启用日期
    private String ZTQYRQ;
    //启用期间
    private String QYQJ;
    //建账日期
    private String JZRQ;
    //建账人
    private String JZR;
    //备注
    private String ZTBZ;

    public AccountBookModel() {}

    public AccountBookModel(String ZTMC, String KJZG, String ZTQYRQ, String QYQJ, String JZRQ, String JZR, String ZTBZ) {
        this.ZTMC = ZTMC;
        this.KJZG = KJZG;
        this.ZTQYRQ = ZTQYRQ;
        this.QYQJ = QYQJ;
        this.JZRQ = JZRQ;
        this.JZR = JZR;
        this.ZTBZ = ZTBZ;
    }

    public String getZTMC() {
        return ZTMC;
    }

    public void setZTMC(String ZTMC) {
        this.ZTMC = ZTMC;
    }

    public String getKJZG() {
        return KJZG;
    }

    public void setKJZG(String KJZG) {
        this.KJZG = KJZG;
    }

    public String getZTQYRQ() {
        return ZTQYRQ;
    }

    public void setZTQYRQ(String ZTQYRQ) {
        this.ZTQYRQ = ZTQYRQ;
    }

    public String getQYQJ() {
        return QYQJ;
    }

    public void setQYQJ(String QYQJ) {
        this.QYQJ = QYQJ;
    }

    public String getJZRQ() {
        return JZRQ;
    }

    public void setJZRQ(String JZRQ) {
        this.JZRQ = JZRQ;
    }

    public String getJZR() {
        return JZR;
    }

    public void setJZR(String JZR) {
        this.JZR = JZR;
    }

    public String getZTBZ() {
        return ZTBZ;
    }

    public void setZTBZ(String ZTBZ) {
        this.ZTBZ = ZTBZ;
    }

    @Override
    public String toString() {
        return "AccountBookModel{" +
                "ZTMC='" + ZTMC + '\'' +
                ", KJZG='" + KJZG + '\'' +
                ", ZTQYRQ=" + ZTQYRQ +
                ", QYQJ=" + QYQJ +
                ", JZRQ=" + JZRQ +
                ", JZR='" + JZR + '\'' +
                ", ZTBZ='" + ZTBZ + '\'' +
                '}';
    }
}

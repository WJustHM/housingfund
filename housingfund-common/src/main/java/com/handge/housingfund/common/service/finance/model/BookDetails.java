package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/9/5.
 */
@XmlRootElement(name = "明细账详情")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookDetails implements Serializable, Cloneable {

    private static final long serialVersionUID = -1310765270642707615L;

    private String JZPZH;//    记账凭证号

    private String JZRQ; //    记账日期

    private String ZhaiYao;//  摘要

    private String JFFSE;//    借方发生额

    private String DFFSE;//    贷方发生额

    private String YEFX;//     余额方向

    private String YuE;//      余额

    private String KJQJ;//     会计期间

    private Boolean SFYC;//    是否异常

    public BookDetails() {
    }

    public BookDetails(String JZPZH, String JZRQ, String zhaiYao, String JFFSE, String DFFSE, String YEFX, String yuE, String KJQJ, Boolean SFYC) {
        this.JZPZH = JZPZH;
        this.JZRQ = JZRQ;
        this.ZhaiYao = zhaiYao;
        this.JFFSE = JFFSE;
        this.DFFSE = DFFSE;
        this.YEFX = YEFX;
        this.YuE = yuE;
        this.KJQJ = KJQJ;
        this.SFYC = SFYC;
    }

    public Boolean getSFYC() {
        return SFYC;
    }

    public void setSFYC(Boolean SFYC) {
        this.SFYC = SFYC;
    }

    public String getJZPZH() {
        return JZPZH;
    }

    public void setJZPZH(String JZPZH) {
        this.JZPZH = JZPZH;
    }

    public String getJZRQ() {
        return JZRQ;
    }

    public void setJZRQ(String JZRQ) {
        this.JZRQ = JZRQ;
    }

    public String getZhaiYao() {
        return ZhaiYao;
    }

    public void setZhaiYao(String zhaiYao) {
        ZhaiYao = zhaiYao;
    }

    public String getJFFSE() {
        return JFFSE;
    }

    public void setJFFSE(String JFFSE) {
        this.JFFSE = JFFSE;
    }

    public String getDFFSE() {
        return DFFSE;
    }

    public void setDFFSE(String DFFSE) {
        this.DFFSE = DFFSE;
    }

    public String getYEFX() {
        return YEFX;
    }

    public void setYEFX(String YEFX) {
        this.YEFX = YEFX;
    }

    public String getYuE() {
        return YuE;
    }

    public void setYuE(String yuE) {
        YuE = yuE;
    }

    public String getKJQJ() {
        return KJQJ;
    }

    public void setKJQJ(String KJQJ) {
        this.KJQJ = KJQJ;
    }

    @Override
    public String toString() {
        return "BookDetails{" +
                "JZPZH='" + JZPZH + '\'' +
                ", JZRQ='" + JZRQ + '\'' +
                ", ZhaiYao='" + ZhaiYao + '\'' +
                ", JFFSE='" + JFFSE + '\'' +
                ", DFFSE='" + DFFSE + '\'' +
                ", YEFX='" + YEFX + '\'' +
                ", YuE='" + YuE + '\'' +
                ", KJQJ='" + KJQJ + '\'' +
                ", SFYC=" + SFYC +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

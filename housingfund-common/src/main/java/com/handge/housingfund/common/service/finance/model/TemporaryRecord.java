package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/9/30.
 */
@XmlRootElement(name = "暂收记录")
@XmlAccessorType(XmlAccessType.FIELD)
public class TemporaryRecord implements Serializable {

    private String id;

    private String SKZH; //收款账号

    private String SKHM; //收款户名

    private String SKYHMC;//收款银行名称

    private String FKZH; //付款账号

    private String FKHM; //付款户名

    private String HRJE;//汇入金额

    private String HRSJ; //汇入时间,格式:YYYYMMDD'

    private String JZPZH; //记账凭证号

    private String YJZPZH; //源记账凭证号

    private String ZhaiYao; //摘要

    private String remark;//备注

    private String state; //状态（1：已分配，0：未分配）

    public TemporaryRecord() {

    }

    public TemporaryRecord(String id, String SKZH, String SKHM, String FKZH, String FKHM, String HRJE, String HRSJ, String JZPZH, String YJZPZH, String zhaiYao, String state, String remark, String SKYHMC) {
        this.id = id;
        this.SKZH = SKZH;
        this.SKHM = SKHM;
        this.FKZH = FKZH;
        this.FKHM = FKHM;
        this.HRJE = HRJE;
        this.HRSJ = HRSJ;
        this.JZPZH = JZPZH;
        this.YJZPZH = YJZPZH;
        this.ZhaiYao = zhaiYao;
        this.state = state;
        this.remark = remark;
        this.SKYHMC = SKYHMC;
    }

    public String getYJZPZH() {
        return YJZPZH;
    }

    public void setYJZPZH(String YJZPZH) {
        this.YJZPZH = YJZPZH;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSKZH() {
        return SKZH;
    }

    public void setSKZH(String SKZH) {
        this.SKZH = SKZH;
    }

    public String getSKHM() {
        return SKHM;
    }

    public void setSKHM(String SKHM) {
        this.SKHM = SKHM;
    }

    public String getFKZH() {
        return FKZH;
    }

    public void setFKZH(String FKZH) {
        this.FKZH = FKZH;
    }

    public String getFKHM() {
        return FKHM;
    }

    public void setFKHM(String FKHM) {
        this.FKHM = FKHM;
    }

    public String getHRJE() {
        return HRJE;
    }

    public void setHRJE(String HRJE) {
        this.HRJE = HRJE;
    }

    public String getHRSJ() {
        return HRSJ;
    }

    public void setHRSJ(String HRSJ) {
        this.HRSJ = HRSJ;
    }

    public String getJZPZH() {
        return JZPZH;
    }

    public void setJZPZH(String JZPZH) {
        this.JZPZH = JZPZH;
    }

    public String getZhaiYao() {
        return ZhaiYao;
    }

    public void setZhaiYao(String zhaiYao) {
        ZhaiYao = zhaiYao;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSKYHMC() {
        return SKYHMC;
    }

    public void setSKYHMC(String SKYHMC) {
        this.SKYHMC = SKYHMC;
    }

    @Override
    public String toString() {
        return "TemporaryRecord{" +
                "id='" + id + '\'' +
                ", SKZH='" + SKZH + '\'' +
                ", SKHM='" + SKHM + '\'' +
                ", SKYHMC='" + SKYHMC + '\'' +
                ", FKZH='" + FKZH + '\'' +
                ", FKHM='" + FKHM + '\'' +
                ", HRJE='" + HRJE + '\'' +
                ", HRSJ='" + HRSJ + '\'' +
                ", JZPZH='" + JZPZH + '\'' +
                ", YJZPZH='" + YJZPZH + '\'' +
                ", ZhaiYao='" + ZhaiYao + '\'' +
                ", remark='" + remark + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}

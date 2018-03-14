package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/6.
 */
@XmlRootElement(name = "银行结算流水信息")
@XmlAccessorType(XmlAccessType.FIELD)
public class SettlementDayBook implements Serializable {
    private static final long serialVersionUID = -27428479741212173L;

    private String id;
    //业务流水号
    private String YWLSH;
    //资金业务类型
    private String ZJYWLX;
    //业务凭证号码
    private String YWPZHM;
    ////银行结算流水号
    private String YHJSLSH;
    //发生额
    private String FSE;
    //结算发生日期
    private String JSFSRQ;
    //结算银行代码
    private String JSYHDM;
    //付款银行代码
    private String FKYHDM;
    //付款账号号码
    private String FKZHHM;
    //付款账号名称
    private String FKZHMC;
    //收款银行代码
    private String SKYHDM;
    //收款账户号码
    private String SKZHHM;
    //收款账户名称
    private String SKZHMC;
    //摘要
    private String ZHAIYAO;

    public SettlementDayBook() {
    }

    public SettlementDayBook(String id, String YWLSH, String ZJYWLX, String YWPZHM, String YHJSLSH, String FSE,
                             String JSFSRQ, String JSYHDM, String FKYHDM, String FKZHHM, String FKZHMC,
                             String SKYHDM, String SKZHHM, String SKZHMC, String ZHAIYAO) {
        this.id = id;
        this.YWLSH = YWLSH;
        this.ZJYWLX = ZJYWLX;
        this.YWPZHM = YWPZHM;
        this.YHJSLSH = YHJSLSH;
        this.FSE = FSE;
        this.JSFSRQ = JSFSRQ;
        this.JSYHDM = JSYHDM;
        this.FKYHDM = FKYHDM;
        this.FKZHHM = FKZHHM;
        this.FKZHMC = FKZHMC;
        this.SKYHDM = SKYHDM;
        this.SKZHHM = SKZHHM;
        this.SKZHMC = SKZHMC;
        this.ZHAIYAO = ZHAIYAO;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public String getZJYWLX() {
        return ZJYWLX;
    }

    public void setZJYWLX(String ZJYWLX) {
        this.ZJYWLX = ZJYWLX;
    }

    public String getYWPZHM() {
        return YWPZHM;
    }

    public void setYWPZHM(String YWPZHM) {
        this.YWPZHM = YWPZHM;
    }

    public String getYHJSLSH() {
        return YHJSLSH;
    }

    public void setYHJSLSH(String YHJSLSH) {
        this.YHJSLSH = YHJSLSH;
    }

    public String getFSE() {
        return FSE;
    }

    public void setFSE(String FSE) {
        this.FSE = FSE;
    }

    public String getJSFSRQ() {
        return JSFSRQ;
    }

    public void setJSFSRQ(String JSFSRQ) {
        this.JSFSRQ = JSFSRQ;
    }

    public String getJSYHDM() {
        return JSYHDM;
    }

    public void setJSYHDM(String JSYHDM) {
        this.JSYHDM = JSYHDM;
    }

    public String getFKYHDM() {
        return FKYHDM;
    }

    public void setFKYHDM(String FKYHDM) {
        this.FKYHDM = FKYHDM;
    }

    public String getFKZHHM() {
        return FKZHHM;
    }

    public void setFKZHHM(String FKZHHM) {
        this.FKZHHM = FKZHHM;
    }

    public String getFKZHMC() {
        return FKZHMC;
    }

    public void setFKZHMC(String FKZHMC) {
        this.FKZHMC = FKZHMC;
    }

    public String getSKYHDM() {
        return SKYHDM;
    }

    public void setSKYHDM(String SKYHDM) {
        this.SKYHDM = SKYHDM;
    }

    public String getSKZHHM() {
        return SKZHHM;
    }

    public void setSKZHHM(String SKZHHM) {
        this.SKZHHM = SKZHHM;
    }

    public String getSKZHMC() {
        return SKZHMC;
    }

    public void setSKZHMC(String SKZHMC) {
        this.SKZHMC = SKZHMC;
    }

    public String getZHAIYAO() {
        return ZHAIYAO;
    }

    public void setZHAIYAO(String ZHAIYAO) {
        this.ZHAIYAO = ZHAIYAO;
    }

    @Override
    public String toString() {
        return "SettlementDayBook{" +
                "id='" + id + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", ZJYWLX='" + ZJYWLX + '\'' +
                ", YWPZHM='" + YWPZHM + '\'' +
                ", YHJSLSH='" + YHJSLSH + '\'' +
                ", FSE=" + FSE +
                ", JSFSRQ=" + JSFSRQ +
                ", JSYHDM='" + JSYHDM + '\'' +
                ", FKYHDM='" + FKYHDM + '\'' +
                ", FKZHHM='" + FKZHHM + '\'' +
                ", FKZHMC='" + FKZHMC + '\'' +
                ", SKYHDM='" + SKYHDM + '\'' +
                ", SKZHHM='" + SKZHHM + '\'' +
                ", SKZHMC='" + SKZHMC + '\'' +
                ", ZHAIYAO='" + ZHAIYAO + '\'' +
                '}';
    }
}

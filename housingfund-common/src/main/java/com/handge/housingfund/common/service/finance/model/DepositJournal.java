package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/7.
 */
@XmlRootElement(name = "银行存款日记账")
@XmlAccessorType(XmlAccessType.FIELD)
public class DepositJournal implements Serializable {
    private static final long serialVersionUID = 1589113116318022253L;

    private String id;
    //出纳流水号
    private String CNLSH;
    //银行专户号码
    private String YHZHHM;
    //借方发生额
    private String JFFSE;
    //贷方发生额
    private String DFFSE;
    //余额
    private String YuE;
    //余额借贷方向 附A.22
    private String YEJDFX;
    //银行结算流水号
    private String YHJSLSH;
    //摘要
    private String ZHAIYAO;
    //记账日期 yyyyMMdd
    private String JZRQ;
    //入账日期 yyyyMMdd
    private String RZRQ;
    //入账状态 附A.28
    private String RZZT;
    //凭证所属年月 yyyy-MM
    private String PZSSNY;
    ////冲账标识 附A.9
    private String CZBS;
    //资金业务类型
    private String ZJYWLX;
    //记账凭证号
    private String JZPZH;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCNLSH() {
        return CNLSH;
    }

    public void setCNLSH(String CNLSH) {
        this.CNLSH = CNLSH;
    }

    public String getYHZHHM() {
        return YHZHHM;
    }

    public void setYHZHHM(String YHZHHM) {
        this.YHZHHM = YHZHHM;
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

    public String getYuE() {
        return YuE;
    }

    public void setYuE(String yuE) {
        YuE = yuE;
    }

    public String getYEJDFX() {
        return YEJDFX;
    }

    public void setYEJDFX(String YEJDFX) {
        this.YEJDFX = YEJDFX;
    }

    public String getYHJSLSH() {
        return YHJSLSH;
    }

    public void setYHJSLSH(String YHJSLSH) {
        this.YHJSLSH = YHJSLSH;
    }

    public String getZHAIYAO() {
        return ZHAIYAO;
    }

    public void setZHAIYAO(String ZHAIYAO) {
        this.ZHAIYAO = ZHAIYAO;
    }

    public String getJZRQ() {
        return JZRQ;
    }

    public void setJZRQ(String JZRQ) {
        this.JZRQ = JZRQ;
    }

    public String getRZRQ() {
        return RZRQ;
    }

    public void setRZRQ(String RZRQ) {
        this.RZRQ = RZRQ;
    }

    public String getRZZT() {
        return RZZT;
    }

    public void setRZZT(String RZZT) {
        this.RZZT = RZZT;
    }

    public String getPZSSNY() {
        return PZSSNY;
    }

    public void setPZSSNY(String PZSSNY) {
        this.PZSSNY = PZSSNY;
    }

    public String getCZBS() {
        return CZBS;
    }

    public void setCZBS(String CZBS) {
        this.CZBS = CZBS;
    }

    public String getZJYWLX() {
        return ZJYWLX;
    }

    public void setZJYWLX(String ZJYWLX) {
        this.ZJYWLX = ZJYWLX;
    }

    public String getJZPZH() {
        return JZPZH;
    }

    public void setJZPZH(String JZPZH) {
        this.JZPZH = JZPZH;
    }

    @Override
    public String toString() {
        return "DepositJournal{" +
                "id='" + id + '\'' +
                ", CNLSH='" + CNLSH + '\'' +
                ", YHZHHM='" + YHZHHM + '\'' +
                ", JFFSE=" + JFFSE +
                ", DFFSE=" + DFFSE +
                ", YuE=" + YuE +
                ", YEJDFX='" + YEJDFX + '\'' +
                ", YHJSLSH='" + YHJSLSH + '\'' +
                ", ZHAIYAO='" + ZHAIYAO + '\'' +
                ", JZRQ=" + JZRQ +
                ", RZRQ=" + RZRQ +
                ", RZZT='" + RZZT + '\'' +
                ", PZSSNY=" + PZSSNY +
                ", CZBS='" + CZBS + '\'' +
                ", ZJYWLX='" + ZJYWLX + '\'' +
                ", JZPZH='" + JZPZH + '\'' +
                '}';
    }
}

package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/8/24.
 */
@XmlRootElement(name = "业务凭证管理列表")
@XmlAccessorType(XmlAccessType.FIELD)
public class VoucherManagerBase implements Serializable {

    private static final long serialVersionUID = 2087798351110683117L;

    private String id;

    private String JZPZH;//  记账凭证号

    private String JZRQ; //      记账日期

    private String JiZhang;//     记账

    private String YWMC;//     业务名称

    private String YWLX;//     业务类型

    private String YWLSH;//业务流水号

    private String FSE;//发生额

    private String ZhaiYao;//摘要

    public VoucherManagerBase() {
    }

    public VoucherManagerBase(String id, String JZPZH, String JZRQ, String jiZhang, String YWMC, String YWLX, String YWLSH, String FSE, String ZhaiYao) {
        this.id = id;
        this.JZPZH = JZPZH;
        this.JZRQ = JZRQ;
        this.JiZhang = jiZhang;
        this.YWMC = YWMC;
        this.YWLX = YWLX;
        this.YWLSH = YWLSH;
        this.FSE = FSE;
        this.ZhaiYao = ZhaiYao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getJiZhang() {
        return JiZhang;
    }

    public void setJiZhang(String jiZhang) {
        JiZhang = jiZhang;
    }

    public String getYWMC() {
        return YWMC;
    }

    public void setYWMC(String YWMC) {
        this.YWMC = YWMC;
    }

    public String getYWLX() {
        return YWLX;
    }

    public void setYWLX(String YWLX) {
        this.YWLX = YWLX;
    }

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public String getFSE() {
        return FSE;
    }

    public void setFSE(String FSE) {
        this.FSE = FSE;
    }

    public String getZhaiYao() {
        return ZhaiYao;
    }

    public void setZhaiYao(String zhaiYao) {
        ZhaiYao = zhaiYao;
    }

    @Override
    public String toString() {
        return "VoucherManagerBase{" +
                "id='" + id + '\'' +
                ", JZPZH='" + JZPZH + '\'' +
                ", JZRQ='" + JZRQ + '\'' +
                ", JiZhang='" + JiZhang + '\'' +
                ", YWMC='" + YWMC + '\'' +
                ", YWLX='" + YWLX + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", FSE='" + FSE + '\'' +
                ", ZhaiYao='" + ZhaiYao + '\'' +
                '}';
    }
}

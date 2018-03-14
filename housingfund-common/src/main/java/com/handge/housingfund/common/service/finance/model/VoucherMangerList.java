package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/8/26.
 */
@XmlRootElement(name = "会计科目列表")
@XmlAccessorType(XmlAccessType.FIELD)
public class VoucherMangerList implements Serializable {

    private static final long serialVersionUID = -7010168586245031950L;

    private String ZhaiYao;//摘要
    private String KJKM;//会计科目
    private String JFJE;//借方金额
    private String DFJE;//贷方金额
    private String KMYEFX;//科目余额方向
    private String KJKMKMMC;//会计科目+科目名称

    public VoucherMangerList() {
    }

    public VoucherMangerList(String zhaiYao, String KJKM, String JFJE, String DFJE, String KMYEFX) {
        this.ZhaiYao = zhaiYao;
        this.KJKM = KJKM;
        this.JFJE = JFJE;
        this.DFJE = DFJE;
        this.KMYEFX = KMYEFX;
    }

    public String getZhaiYao() {
        return this.ZhaiYao;
    }

    public void setZhaiYao(String zhaiYao) {
        ZhaiYao = zhaiYao;
    }

    public String getKJKM() {
        return KJKM;
    }

    public void setKJKM(String KJKM) {
        this.KJKM = KJKM;
    }

    public String getJFJE() {
        return JFJE;
    }

    public void setJFJE(String JFJE) {
        this.JFJE = JFJE;
    }

    public String getDFJE() {
        return DFJE;
    }

    public void setDFJE(String DFJE) {
        this.DFJE = DFJE;
    }

    public String getKMYEFX() {
        return KMYEFX;
    }

    public void setKMYEFX(String KMYEFX) {
        this.KMYEFX = KMYEFX;
    }

    public String getKJKMKMMC() {
        return KJKMKMMC;
    }

    public void setKJKMKMMC(String KJKMKMMC) {
        this.KJKMKMMC = KJKMKMMC;
    }
}

package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Funnyboy on 2017/8/16.
 */
@XmlRootElement(name = "BorrowingInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class BorrowingInfo implements Serializable {
    private static final long serialVersionUID = -58750115929129083L;
    private String  JKRXM;
    private String  JKRZJLX;
    private String  JKRZJHM;
    private String  CSNY;
    private String  XingBie;
    private String  XueLi;
    private String  NinaLing;
    private String  JKZK;
    private String  HYZK;
    private String  ZhiCheng;
    private String  ZhiWu;
    private String  YGXZ;
    private String  ZYJJLY;
    private String  YSR;
    private String  JTYSR;
    @Annotation.Phone(name="固定号码")
    private String  GDDHHM;
    @Annotation.Mobile(name="手机号码")
    private String  SJHM;
    private String  JTZZ;
    private String  HKSZD;

    public String getJKRXM() {
        return JKRXM;
    }

    public void setJKRXM(String JKRXM) {
        this.JKRXM = JKRXM;
    }

    public String getJKRZJLX() {
        return JKRZJLX;
    }

    public void setJKRZJLX(String JKRZJLX) {
        this.JKRZJLX = JKRZJLX;
    }

    public String getJKRZJHM() {
        return JKRZJHM;
    }

    public void setJKRZJHM(String JKRZJHM) {
        this.JKRZJHM = JKRZJHM;
    }

    public String getCSNY() {
        return CSNY;
    }

    public void setCSNY(String CSNY) {
        this.CSNY = CSNY;
    }



    public String getJKZK() {
        return JKZK;
    }

    public void setJKZK(String JKZK) {
        this.JKZK = JKZK;
    }

    public String getHYZK() {
        return HYZK;
    }

    public void setHYZK(String HYZK) {
        this.HYZK = HYZK;
    }



    public String getYGXZ() {
        return YGXZ;
    }

    public void setYGXZ(String YGXZ) {
        this.YGXZ = YGXZ;
    }

    public String getZYJJLY() {
        return ZYJJLY;
    }

    public void setZYJJLY(String ZYJJLY) {
        this.ZYJJLY = ZYJJLY;
    }

    public String getYSR() {
        return YSR;
    }

    public void setYSR(String YSR) {
        this.YSR = YSR;
    }

    public String getJTYSR() {
        return JTYSR;
    }

    public void setJTYSR(String JTYSR) {
        this.JTYSR = JTYSR;
    }

    public String getGDDHHM() {
        return GDDHHM;
    }

    public void setGDDHHM(String GDDHHM) {
        this.GDDHHM = GDDHHM;
    }

    public String getSJHM() {
        return SJHM;
    }

    public void setSJHM(String SJHM) {
        this.SJHM = SJHM;
    }

    public String getJTZZ() {
        return JTZZ;
    }

    public void setJTZZ(String JTZZ) {
        this.JTZZ = JTZZ;
    }

    public String getHKSZD() {
        return HKSZD;
    }

    public void setHKSZD(String HKSZD) {
        this.HKSZD = HKSZD;
    }


    public String getXingBie() {
        return XingBie;
    }

    public void setXingBie(String xingBie) {
        XingBie = xingBie;
    }

    public String getXueLi() {
        return XueLi;
    }

    public void setXueLi(String xueLi) {
        XueLi = xueLi;
    }

    public String getNinaLing() {
        return NinaLing;
    }

    public void setNinaLing(String ninaLing) {
        NinaLing = ninaLing;
    }

    public String getZhiCheng() {
        return ZhiCheng;
    }

    public void setZhiCheng(String zhiCheng) {
        ZhiCheng = zhiCheng;
    }

    public String getZhiWu() {
        return ZhiWu;
    }

    public void setZhiWu(String zhiWu) {
        ZhiWu = zhiWu;
    }
}

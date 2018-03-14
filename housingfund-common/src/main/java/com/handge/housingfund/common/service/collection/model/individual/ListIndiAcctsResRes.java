package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ListIndiAcctsResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListIndiAcctsResRes implements Serializable {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    private String XingMing;  //姓名

    private String GRJCJS;  //个人缴存基数

    private String JZNY;  //缴至年月

    private String GRZHYE;  //个人账户余额

    private String SLSJ;  //受理时间

    private String ZJHM;  //证件号码

    private String GRZHZT;  //个人账户状态

    private String SFDJ;        //是否冻结

    private String YJCE;  //月缴存额

    private String DWMC;  //单位名称

    private String GRZH;  //个人账号

    private String YWWD;

    public String getXingMing() {
        return XingMing;
    }

    public void setXingMing(String xingMing) {
        XingMing = xingMing;
    }

    public String getGRJCJS() {
        return GRJCJS;
    }

    public void setGRJCJS(String GRJCJS) {
        this.GRJCJS = GRJCJS;
    }

    public String getJZNY() {
        return JZNY;
    }

    public void setJZNY(String JZNY) {
        this.JZNY = JZNY;
    }

    public String getGRZHYE() {
        return GRZHYE;
    }

    public void setGRZHYE(String GRZHYE) {
        this.GRZHYE = GRZHYE;
    }

    public String getSLSJ() {
        return SLSJ;
    }

    public void setSLSJ(String SLSJ) {
        this.SLSJ = SLSJ;
    }

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getGRZHZT() {
        return GRZHZT;
    }

    public void setGRZHZT(String GRZHZT) {
        this.GRZHZT = GRZHZT;
    }

    public String getSFDJ() {
        return SFDJ;
    }

    public void setSFDJ(String SFDJ) {
        this.SFDJ = SFDJ;
    }

    public String getYJCE() {
        return YJCE;
    }

    public void setYJCE(String YJCE) {
        this.YJCE = YJCE;
    }

    public String getDWMC() {
        return DWMC;
    }

    public void setDWMC(String DWMC) {
        this.DWMC = DWMC;
    }

    public String getGRZH() {
        return GRZH;
    }

    public void setGRZH(String GRZH) {
        this.GRZH = GRZH;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    @Override
    public String toString() {
        return "ListIndiAcctsResRes{" +
                "XingMing='" + XingMing + '\'' +
                ", GRJCJS='" + GRJCJS + '\'' +
                ", JZNY='" + JZNY + '\'' +
                ", GRZHYE='" + GRZHYE + '\'' +
                ", SLSJ='" + SLSJ + '\'' +
                ", ZJHM='" + ZJHM + '\'' +
                ", GRZHZT='" + GRZHZT + '\'' +
                ", SFDJ='" + SFDJ + '\'' +
                ", YJCE='" + YJCE + '\'' +
                ", DWMC='" + DWMC + '\'' +
                ", GRZH='" + GRZH + '\'' +
                ", YWWD='" + YWWD + '\'' +
                '}';
    }
}
package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HeadUnitDepositRatioReceiptRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadUnitDepositRatioReceiptRes implements Serializable {

    private static final long serialVersionUID = 7686552204051180622L;
    private String TZQGRJCBL;  //调整前个人缴存比例

    private String JBRZJLX;  //经办人证件类型

    private String YWLSH;  //业务流水号

    private String JBRXM;  //经办人姓名

    private String SXNY;  //生效年月

    private String TZQDWJCBL;  //调整前单位缴存比例

    private String TZHGRJCBL;  //调整后个人缴存比例

    private String TZSJ;  //填制时间

    private String YZM;  //验证码

    private String TZHDWJCBL;  //调整后单位缴存比例

    private String JBRZJHM;  //经办人证件号码

    private String DWMC;  //单位名称

    private String DWZH;  //单位账号

    private String CZY;  //操作员

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    private String YWWD; //业务网点

    private String SHR; //审核人

    public String getSHR() {
        return SHR;
    }

    public void setSHR(String SHR) {
        this.SHR = SHR;
    }

    public String getTZQGRJCBL() {

        return this.TZQGRJCBL;

    }


    public void setTZQGRJCBL(String TZQGRJCBL) {

        this.TZQGRJCBL = TZQGRJCBL;

    }


    public String getJBRZJLX() {

        return this.JBRZJLX;

    }


    public void setJBRZJLX(String JBRZJLX) {

        this.JBRZJLX = JBRZJLX;

    }


    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getJBRXM() {

        return this.JBRXM;

    }


    public void setJBRXM(String JBRXM) {

        this.JBRXM = JBRXM;

    }


    public String getSXNY() {

        return this.SXNY;

    }


    public void setSXNY(String SXNY) {

        this.SXNY = SXNY;

    }


    public String getTZQDWJCBL() {

        return this.TZQDWJCBL;

    }


    public void setTZQDWJCBL(String TZQDWJCBL) {

        this.TZQDWJCBL = TZQDWJCBL;

    }


    public String getTZHGRJCBL() {

        return this.TZHGRJCBL;

    }


    public void setTZHGRJCBL(String TZHGRJCBL) {

        this.TZHGRJCBL = TZHGRJCBL;

    }


    public String getTZSJ() {

        return this.TZSJ;

    }


    public void setTZSJ(String TZSJ) {

        this.TZSJ = TZSJ;

    }


    public String getYZM() {

        return this.YZM;

    }


    public void setYZM(String YZM) {

        this.YZM = YZM;

    }


    public String getTZHDWJCBL() {

        return this.TZHDWJCBL;

    }


    public void setTZHDWJCBL(String TZHDWJCBL) {

        this.TZHDWJCBL = TZHDWJCBL;

    }


    public String getJBRZJHM() {

        return this.JBRZJHM;

    }


    public void setJBRZJHM(String JBRZJHM) {

        this.JBRZJHM = JBRZJHM;

    }


    public String getDWMC() {

        return this.DWMC;

    }


    public void setDWMC(String DWMC) {

        this.DWMC = DWMC;

    }


    public String getDWZH() {

        return this.DWZH;

    }


    public void setDWZH(String DWZH) {

        this.DWZH = DWZH;

    }


    public String getCZY() {

        return this.CZY;

    }


    public void setCZY(String CZY) {

        this.CZY = CZY;

    }


    public String toString() {

        return "HeadUnitDepositRatioReceiptRes{" +

            "TZQGRJCBL='" + this.TZQGRJCBL + '\'' + "," +
            "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
            "YWLSH='" + this.YWLSH + '\'' + "," +
            "JBRXM='" + this.JBRXM + '\'' + "," +
            "SXNY='" + this.SXNY + '\'' + "," +
            "TZQDWJCBL='" + this.TZQDWJCBL + '\'' + "," +
            "TZHGRJCBL='" + this.TZHGRJCBL + '\'' + "," +
            "TZSJ='" + this.TZSJ + '\'' + "," +
            "YZM='" + this.YZM + '\'' + "," +
            "TZHDWJCBL='" + this.TZHDWJCBL + '\'' + "," +
            "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
            "DWMC='" + this.DWMC + '\'' + "," +
            "DWZH='" + this.DWZH + '\'' + "," +
            "CZY='" + this.CZY + '\'' +
            "SHR='" + this.SHR + '\'' +
            "YWWD='" + this.YWWD + '\'' +
            "}";

    }
}
package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "HeadUnitPayBackReceiptRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadUnitPayBackReceiptRes implements Serializable {

    private String JBRZJLX;  //经办人证件类型

    private String YWLSH;  //业务流水号

    private HeadUnitPayBackReceiptResBJHJ BJHJ;  //补缴合计

    private String JBRXM;  //经办人姓名

    private String BJNY;  //汇补缴年月

    private String BJFS;  //补缴方式

    private String DWZHYE;  //单位账户余额

    private ArrayList<HeadUnitPayBackReceiptResBJXX> BJXX;  //补缴信息

    private String ZSYE;  //暂收余额（元）

    private String JZNY;  //缴至年月

    private String TZSJ;  //填制时间

    private String YZM;  //验证码

    private String JZRQ;  //记账日期

    private String JBRZJHM;  //经办人证件号码

    private String DWMC;  //单位名称

    private String DWYHJNY;  //单位应汇缴年月

    private String DWZH;  //单位账号

    private String YWWD; //业务网点

    private String  CZY;//操作员

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
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


    public HeadUnitPayBackReceiptResBJHJ getBJHJ() {

        return this.BJHJ;

    }


    public void setBJHJ(HeadUnitPayBackReceiptResBJHJ BJHJ) {

        this.BJHJ = BJHJ;

    }


    public String getJBRXM() {

        return this.JBRXM;

    }


    public void setJBRXM(String JBRXM) {

        this.JBRXM = JBRXM;

    }


    public String getBJNY() {

        return this.BJNY;

    }


    public void setBJNY(String BJNY) {

        this.BJNY = BJNY;

    }


    public String getBJFS() {

        return this.BJFS;

    }


    public void setBJFS(String BJFS) {

        this.BJFS = BJFS;

    }


    public String getDWZHYE() {

        return this.DWZHYE;

    }


    public void setDWZHYE(String DWZHYE) {

        this.DWZHYE = DWZHYE;

    }


    public ArrayList<HeadUnitPayBackReceiptResBJXX> getBJXX() {

        return this.BJXX;

    }


    public void setBJXX(ArrayList<HeadUnitPayBackReceiptResBJXX> BJXX) {

        this.BJXX = BJXX;

    }


    public String getZSYE() {

        return this.ZSYE;

    }


    public void setZSYE(String ZSYE) {

        this.ZSYE = ZSYE;

    }


    public String getJZNY() {

        return this.JZNY;

    }


    public void setJZNY(String JZNY) {

        this.JZNY = JZNY;

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


    public String getJZRQ() {

        return this.JZRQ;

    }


    public void setJZRQ(String JZRQ) {

        this.JZRQ = JZRQ;

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


    public String getDWYHJNY() {

        return this.DWYHJNY;

    }


    public void setDWYHJNY(String DWYHJNY) {

        this.DWYHJNY = DWYHJNY;

    }


    public String getDWZH() {

        return this.DWZH;

    }


    public void setDWZH(String DWZH) {

        this.DWZH = DWZH;

    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String toString() {

        return "HeadUnitPayBackReceiptRes{" +

            "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
            "YWLSH='" + this.YWLSH + '\'' + "," +
            "BJHJ='" + this.BJHJ + '\'' + "," +
            "JBRXM='" + this.JBRXM + '\'' + "," +
            "BJNY='" + this.BJNY + '\'' + "," +
            "BJFS='" + this.BJFS + '\'' + "," +
            "DWZHYE='" + this.DWZHYE + '\'' + "," +
            "BJXX='" + this.BJXX + '\'' + "," +
            "ZSYE='" + this.ZSYE + '\'' + "," +
            "JZNY='" + this.JZNY + '\'' + "," +
            "TZSJ='" + this.TZSJ + '\'' + "," +
            "YZM='" + this.YZM + '\'' + "," +
            "JZRQ='" + this.JZRQ + '\'' + "," +
            "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
            "DWMC='" + this.DWMC + '\'' + "," +
            "DWYHJNY='" + this.DWYHJNY + '\'' + "," +
            "DWZH='" + this.DWZH + '\'' +
            "YWWD='" + this.YWWD + '\'' +
            "CZY='" + this.CZY + '\'' +
            "}";

    }
}
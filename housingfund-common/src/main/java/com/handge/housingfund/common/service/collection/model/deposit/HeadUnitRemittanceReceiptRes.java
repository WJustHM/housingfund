package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "HeadUnitRemittanceReceiptRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadUnitRemittanceReceiptRes implements Serializable {

    private String JBRZJLX;  //经办人证件类型

    private String YWLSH;  //业务流水号

    private String DWZHYE;  //单位账户余额

    private HeadUnitRemittanceReceiptResJCXX JCXX;  //缴存信息

    private String JBRXM;  //经办人姓名

    private String HJNY;  //汇缴年月

    private String ZSYE;  //暂收余额

    private HeadUnitRemittanceReceiptResYJEZHJ YJEZHJ;  //个人缴存基数、月缴存额合计

    private Integer DWFCRS;  //单位封存人数

    private String TZSJ;  //填制时间

    private String YZM;  //验证码

    private ArrayList<HeadUnitRemittanceReceiptResDWHJQC> DWHJQC;  //单位汇缴清册

    private String JBRZJHM;  //经办人证件号码

    private String DWMC;  //单位名称

    private String DWYHJNY;  //单位应汇缴年月

    private String DWZH;  //单位账号

    private Integer DWJCRS;  //单位缴存人数

    private String YWWD; //业务网点

    private String CZY; //操作员

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }


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


    public String getDWZHYE() {

        return this.DWZHYE;

    }


    public void setDWZHYE(String DWZHYE) {

        this.DWZHYE = DWZHYE;

    }


    public HeadUnitRemittanceReceiptResJCXX getJCXX() {

        return this.JCXX;

    }


    public void setJCXX(HeadUnitRemittanceReceiptResJCXX JCXX) {

        this.JCXX = JCXX;

    }


    public String getJBRXM() {

        return this.JBRXM;

    }


    public void setJBRXM(String JBRXM) {

        this.JBRXM = JBRXM;

    }


    public String getHJNY() {

        return this.HJNY;

    }


    public void setHJNY(String HJNY) {

        this.HJNY = HJNY;

    }


    public String getZSYE() {

        return this.ZSYE;

    }


    public void setZSYE(String ZSYE) {

        this.ZSYE = ZSYE;

    }


    public HeadUnitRemittanceReceiptResYJEZHJ getYJEZHJ() {

        return this.YJEZHJ;

    }


    public void setYJEZHJ(HeadUnitRemittanceReceiptResYJEZHJ YJEZHJ) {

        this.YJEZHJ = YJEZHJ;

    }


    public Integer getDWFCRS() {

        return this.DWFCRS;

    }


    public void setDWFCRS(Integer DWFCRS) {

        this.DWFCRS = DWFCRS;

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


    public ArrayList<HeadUnitRemittanceReceiptResDWHJQC> getDWHJQC() {

        return this.DWHJQC;

    }


    public void setDWHJQC(ArrayList<HeadUnitRemittanceReceiptResDWHJQC> DWHJQC) {

        this.DWHJQC = DWHJQC;

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


    public Integer getDWJCRS() {

        return this.DWJCRS;

    }


    public void setDWJCRS(Integer DWJCRS) {

        this.DWJCRS = DWJCRS;

    }


    public String toString() {

        return "HeadUnitRemittanceReceiptRes{" +

            "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
            "YWLSH='" + this.YWLSH + '\'' + "," +
            "DWZHYE='" + this.DWZHYE + '\'' + "," +
            "JCXX='" + this.JCXX + '\'' + "," +
            "JBRXM='" + this.JBRXM + '\'' + "," +
            "HJNY='" + this.HJNY + '\'' + "," +
            "ZSYE='" + this.ZSYE + '\'' + "," +
            "YJEZHJ='" + this.YJEZHJ + '\'' + "," +
            "DWFCRS='" + this.DWFCRS + '\'' + "," +
            "TZSJ='" + this.TZSJ + '\'' + "," +
            "YZM='" + this.YZM + '\'' + "," +
            "DWHJQC='" + this.DWHJQC + '\'' + "," +
            "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
            "DWMC='" + this.DWMC + '\'' + "," +
            "DWYHJNY='" + this.DWYHJNY + '\'' + "," +
            "DWZH='" + this.DWZH + '\'' + "," +
            "DWJCRS='" + this.DWJCRS + '\'' +
            "YWWD='" + this.YWWD + '\'' +
            "CZY='" + this.CZY + '\'' +
            "}";

    }
}
package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HeadUnitPayHoldReceiptRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadUnitPayHoldReceiptRes implements Serializable {

    private String JBRZJLX;  //经办人证件类型

    private String YWLSH;  //业务流水号

    private String SQHJKSNY;  //申请缓缴开始年月

    private String JBRXM;  //经办人姓名

    private String JZNY;  //缴至年月

    private String HJYY;  //缓缴原因

    private String TZSJ;  //填制时间

    private String YZM;  //验证码

    private String JBRZJHM;  //经办人证件号码

    private String DWMC;  //单位名称

    private String DWYHJNY;  //单位应汇缴年月

    private String DWZH;  //单位账号

    private String SQHJJSNY;  //申请缓缴结束年月

    private String CZY;  //操作员

    private String YWWD; //业务网点

    private String SHR;// 审核人

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


    public String getSQHJKSNY() {

        return this.SQHJKSNY;

    }


    public void setSQHJKSNY(String SQHJKSNY) {

        this.SQHJKSNY = SQHJKSNY;

    }


    public String getJBRXM() {

        return this.JBRXM;

    }


    public void setJBRXM(String JBRXM) {

        this.JBRXM = JBRXM;

    }


    public String getJZNY() {

        return this.JZNY;

    }


    public void setJZNY(String JZNY) {

        this.JZNY = JZNY;

    }


    public String getHJYY() {

        return this.HJYY;

    }


    public void setHJYY(String HJYY) {

        this.HJYY = HJYY;

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


    public String getSQHJJSNY() {

        return this.SQHJJSNY;

    }


    public void setSQHJJSNY(String SQHJJSNY) {

        this.SQHJJSNY = SQHJJSNY;

    }


    public String getCZY() {

        return this.CZY;

    }


    public void setCZY(String CZY) {

        this.CZY = CZY;

    }

    public String getSHR() {
        return SHR;
    }

    public void setSHR(String SHR) {
        this.SHR = SHR;
    }

    public String toString() {

        return "HeadUnitPayHoldReceiptRes{" +

            "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
            "YWLSH='" + this.YWLSH + '\'' + "," +
            "SQHJKSNY='" + this.SQHJKSNY + '\'' + "," +
            "JBRXM='" + this.JBRXM + '\'' + "," +
            "JZNY='" + this.JZNY + '\'' + "," +
            "HJYY='" + this.HJYY + '\'' + "," +
            "TZSJ='" + this.TZSJ + '\'' + "," +
            "YZM='" + this.YZM + '\'' + "," +
            "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
            "DWMC='" + this.DWMC + '\'' + "," +
            "DWYHJNY='" + this.DWYHJNY + '\'' + "," +
            "DWZH='" + this.DWZH + '\'' + "," +
            "SQHJJSNY='" + this.SQHJJSNY + '\'' + "," +
            "CZY='" + this.CZY + '\'' +
            "SHR='" + this.SHR + '\'' +
            "YWWD='" + this.YWWD + '\'' +
            "}";

    }
}
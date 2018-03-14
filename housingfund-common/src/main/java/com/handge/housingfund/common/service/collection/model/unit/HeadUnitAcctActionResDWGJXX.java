package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HeadUnitAcctActionResDWGJXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadUnitAcctActionResDWGJXX  implements Serializable{

    private String DWFRDBZJHM;  //单位法人代表证件号码

    private String YWLSH;  //业务流水号

    private String DWFRDBXM;  //单位法人代表姓名

    private String JBRXM;  //经办人姓名

    private String DJSYYZ;  //登记使用印章

    private String DWLB;  //单位类别

    private String RiQi;  //日期

    private String JBRSJHM;  //经办人手机号码

    private String FCHXHYY;  //单位操作原因（销户、封存）

    private String QTCZYY;  //其他操作原因（如启封等等）

    private String JZNY;  //缴至年月

    private String ZZJGDM;  //组织机构代码

    private String BeiZhu;  //备注

    private String JBRZJHM;  //经办人证件号码

    private String DWMC;  //单位名称

    private String DWZH;  //单位账号

    private String JBRQM;  //经办人签名

    private String SHR; //审核人

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    private String YWWD; //业务网点

    public String getSHR() {
        return SHR;
    }

    public void setSHR(String SHR) {
        this.SHR = SHR;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    private String CZY; //操作员

    public String getDWFRDBZJHM() {

        return this.DWFRDBZJHM;

    }


    public void setDWFRDBZJHM(String DWFRDBZJHM) {

        this.DWFRDBZJHM = DWFRDBZJHM;

    }


    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getDWFRDBXM() {

        return this.DWFRDBXM;

    }


    public void setDWFRDBXM(String DWFRDBXM) {

        this.DWFRDBXM = DWFRDBXM;

    }


    public String getJBRXM() {

        return this.JBRXM;

    }


    public void setJBRXM(String JBRXM) {

        this.JBRXM = JBRXM;

    }


    public String getDJSYYZ() {

        return this.DJSYYZ;

    }


    public void setDJSYYZ(String DJSYYZ) {

        this.DJSYYZ = DJSYYZ;

    }


    public String getDWLB() {

        return this.DWLB;

    }


    public void setDWLB(String DWLB) {

        this.DWLB = DWLB;

    }


    public String getRiQi() {

        return this.RiQi;

    }


    public void setRiQi(String RiQi) {

        this.RiQi = RiQi;

    }


    public String getJBRSJHM() {

        return this.JBRSJHM;

    }


    public void setJBRSJHM(String JBRSJHM) {

        this.JBRSJHM = JBRSJHM;

    }


    public String getFCHXHYY() {

        return this.FCHXHYY;

    }


    public void setFCHXHYY(String FCHXHYY) {

        this.FCHXHYY = FCHXHYY;

    }

    public String getQTCZYY() {
        return QTCZYY;
    }

    public void setQTCZYY(String QTCZYY) {
        this.QTCZYY = QTCZYY;
    }

    public String getJZNY() {

        return this.JZNY;

    }


    public void setJZNY(String JZNY) {

        this.JZNY = JZNY;

    }


    public String getZZJGDM() {

        return this.ZZJGDM;

    }


    public void setZZJGDM(String ZZJGDM) {

        this.ZZJGDM = ZZJGDM;

    }


    public String getBeiZhu() {

        return this.BeiZhu;

    }


    public void setBeiZhu(String BeiZhu) {

        this.BeiZhu = BeiZhu;

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


    public String getJBRQM() {

        return this.JBRQM;

    }


    public void setJBRQM(String JBRQM) {

        this.JBRQM = JBRQM;

    }


    @Override
    public String toString() {
        return "HeadUnitAcctActionResDWGJXX{" +
                "DWFRDBZJHM='" + DWFRDBZJHM + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", DWFRDBXM='" + DWFRDBXM + '\'' +
                ", JBRXM='" + JBRXM + '\'' +
                ", DJSYYZ='" + DJSYYZ + '\'' +
                ", DWLB='" + DWLB + '\'' +
                ", RiQi='" + RiQi + '\'' +
                ", JBRSJHM='" + JBRSJHM + '\'' +
                ", FCHXHYY='" + FCHXHYY + '\'' +
                ", QTCZYY='" + QTCZYY + '\'' +
                ", JZNY='" + JZNY + '\'' +
                ", ZZJGDM='" + ZZJGDM + '\'' +
                ", BeiZhu='" + BeiZhu + '\'' +
                ", JBRZJHM='" + JBRZJHM + '\'' +
                ", DWMC='" + DWMC + '\'' +
                ", SHR='" + SHR + '\'' +
                ", CZY='" + CZY + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", DWZH='" + DWZH + '\'' +
                ", JBRQM='" + JBRQM + '\'' +
                '}';
    }
}
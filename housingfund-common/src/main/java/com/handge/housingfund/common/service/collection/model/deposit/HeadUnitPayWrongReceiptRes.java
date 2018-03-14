package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "HeadUnitPayWrongReceiptRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadUnitPayWrongReceiptRes implements Serializable {

    private HeadUnitPayWrongReceiptResCJEHJ CJEHJ;  //错缴额合计信息

    private String JBRZJLX;  //经办人证件类型

    private String YWLSH;  //业务流水号

    private String JBRXM;  //经办人姓名

    private ArrayList<HeadUnitPayWrongReceiptResGZXX> GZXX;  //更正信息

    private String JCGZNY;  //缴存更正年月

    private String SKYHMC;  //收款银行名称

    private String TZSJ;  //填制时间

    private String YZM;  //验证码

    private String SKYHHM;  //收款银行户名

    private String DWMC;  //单位名称

    private String DWZH;  //单位账号

    private String JBRZJHM;  //经办人证件号码

    private String SKYHZH;  //收款银行账号

    private String SKYHDM;  //收款银行代码

    private String CZY;   //操作员

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    private String YWWD;  //业务网点

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }



    public HeadUnitPayWrongReceiptResCJEHJ getCJEHJ() {

        return this.CJEHJ;

    }


    public void setCJEHJ(HeadUnitPayWrongReceiptResCJEHJ CJEHJ) {

        this.CJEHJ = CJEHJ;

    }


    public String getJBRZJLX() {

        return this.JBRZJLX;

    }


    public void setJBRZJLX(String JBRZJLX) {

        this.JBRZJLX = JBRZJLX;

    }

    public String getSKYHDM() {
        return SKYHDM;
    }

    public void setSKYHDM(String SKYHDM) {
        this.SKYHDM = SKYHDM;
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


    public ArrayList<HeadUnitPayWrongReceiptResGZXX> getGZXX() {

        return this.GZXX;

    }


    public void setGZXX(ArrayList<HeadUnitPayWrongReceiptResGZXX> GZXX) {

        this.GZXX = GZXX;

    }


    public String getJCGZNY() {

        return this.JCGZNY;

    }


    public void setJCGZNY(String JCGZNY) {

        this.JCGZNY = JCGZNY;

    }


    public String getSKYHMC() {

        return this.SKYHMC;

    }


    public void setSKYHMC(String SKYHMC) {

        this.SKYHMC = SKYHMC;

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


    public String getSKYHHM() {

        return this.SKYHHM;

    }


    public void setSKYHHM(String SKYHHM) {

        this.SKYHHM = SKYHHM;

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


    public String getJBRZJHM() {

        return this.JBRZJHM;

    }


    public void setJBRZJHM(String JBRZJHM) {

        this.JBRZJHM = JBRZJHM;

    }


    public String getSKYHZH() {

        return this.SKYHZH;

    }


    public void setSKYHZH(String SKYHZH) {

        this.SKYHZH = SKYHZH;

    }

    @Override
    public String toString() {
        return "HeadUnitPayWrongReceiptRes{" +
            "CJEHJ=" + CJEHJ +
            ", JBRZJLX='" + JBRZJLX + '\'' +
            ", YWLSH='" + YWLSH + '\'' +
            ", JBRXM='" + JBRXM + '\'' +
            ", GZXX=" + GZXX +
            ", JCGZNY='" + JCGZNY + '\'' +
            ", SKYHMC='" + SKYHMC + '\'' +
            ", TZSJ='" + TZSJ + '\'' +
            ", YZM='" + YZM + '\'' +
            ", SKYHHM='" + SKYHHM + '\'' +
            ", DWMC='" + DWMC + '\'' +
            ", DWZH='" + DWZH + '\'' +
            ", JBRZJHM='" + JBRZJHM + '\'' +
            ", SKYHZH='" + SKYHZH + '\'' +
            ", SKYHDM='" + SKYHDM + '\'' +
            ", CZY='" + CZY + '\'' +
            ", YWWD='" + YWWD + '\'' +
            '}';
    }
}
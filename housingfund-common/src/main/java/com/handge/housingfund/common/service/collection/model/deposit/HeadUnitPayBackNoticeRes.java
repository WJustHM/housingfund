package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HeadUnitPayBackNoticeRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadUnitPayBackNoticeRes implements Serializable {

    private String YWLSH;  //业务流水号

    private String DWMC;  //单位名称

    private String DWZH;  //单位账号

    private String BJNY;  //汇补缴年月

    private Integer FSRS;  //发生人数

    private String FSE;  //发生额

    private String ZJE;  //总金额（大写）

    private String SKZHMC;  //收款账号名称

    private String SKZH;  //收款账号

    private String BJFS;  //补缴方式

    private String SLSJ;  //受理时间

    private String STYHDM;  //受托银行代码

    private String STYHMC;  //受托银行名称

    private String CZY;  //操作员

    private String JBRXM;    //经办人姓名

    private String JZSJ;     //截止时间

    private String YWWD;     //业务网点

    private String TZRQ;     //填制日期

    public String getSTYHMC() {

        return this.STYHMC;

    }


    public void setSTYHMC(String STYHMC) {

        this.STYHMC = STYHMC;

    }


    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getFSE() {

        return this.FSE;

    }


    public void setFSE(String FSE) {

        this.FSE = FSE;

    }


    public String getZJE() {

        return this.ZJE;

    }


    public void setZJE(String ZJE) {

        this.ZJE = ZJE;

    }


    public String getBJNY() {

        return this.BJNY;

    }


    public void setBJNY(String BJNY) {

        this.BJNY = BJNY;

    }


    public String getSKZHMC() {

        return this.SKZHMC;

    }


    public void setSKZHMC(String SKZHMC) {

        this.SKZHMC = SKZHMC;

    }

    public String getJBRXM() {
        return JBRXM;
    }

    public void setJBRXM(String JBRXM) {
        this.JBRXM = JBRXM;
    }

    public String getJZSJ() {
        return JZSJ;
    }

    public void setJZSJ(String JZSJ) {
        this.JZSJ = JZSJ;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String getTZRQ() {
        return TZRQ;
    }

    public void setTZRQ(String TZRQ) {
        this.TZRQ = TZRQ;
    }

    public String getBJFS() {

        return this.BJFS;

    }


    public void setBJFS(String BJFS) {

        this.BJFS = BJFS;

    }


    public String getSKZH() {

        return this.SKZH;

    }


    public void setSKZH(String SKZH) {

        this.SKZH = SKZH;

    }


    public String getSLSJ() {

        return this.SLSJ;

    }


    public void setSLSJ(String SLSJ) {

        this.SLSJ = SLSJ;

    }


    public String getSTYHDM() {

        return this.STYHDM;

    }


    public void setSTYHDM(String STYHDM) {

        this.STYHDM = STYHDM;

    }


    public Integer getFSRS() {

        return this.FSRS;

    }


    public void setFSRS(Integer FSRS) {

        this.FSRS = FSRS;

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

    @Override
    public String toString() {
        return "HeadUnitPayBackNoticeRes{" +
            "YWLSH='" + YWLSH + '\'' +
            ", DWMC='" + DWMC + '\'' +
            ", DWZH='" + DWZH + '\'' +
            ", BJNY='" + BJNY + '\'' +
            ", FSRS=" + FSRS +
            ", FSE=" + FSE +
            ", ZJE='" + ZJE + '\'' +
            ", SKZHMC='" + SKZHMC + '\'' +
            ", SKZH='" + SKZH + '\'' +
            ", BJFS='" + BJFS + '\'' +
            ", SLSJ='" + SLSJ + '\'' +
            ", STYHDM='" + STYHDM + '\'' +
            ", STYHMC='" + STYHMC + '\'' +
            ", CZY='" + CZY + '\'' +
            ", JBRXM='" + JBRXM + '\'' +
            ", JZSJ='" + JZSJ + '\'' +
            ", YWWD='" + YWWD + '\'' +
            ", TZRQ='" + TZRQ + '\'' +
            '}';
    }
}
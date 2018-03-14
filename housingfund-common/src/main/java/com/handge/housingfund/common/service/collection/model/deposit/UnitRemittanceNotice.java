package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "UnitRemittanceNotice")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitRemittanceNotice  implements Serializable {

    private String YWLSH;   //业务流水号

    private String YWWD;    //业务网点

    private String TZRQ;    //填制日期

    private String DWMC;    //单位名称

    private String DWZH;    //单位账号

    private String HBJNY;   //汇补缴年月

    private String HJFS;    //汇缴方式

    private String FSRS;    //发生人数

    private String FSE;     //发生额

    private String ZJE;     //总金额（大写）

    private String SKZHMC;  //收款账户名称

    private String SKZH;    //收款账号

    private String STYHMC;  //受托银行名称

    private String STYHDM;  //受托银行代码

    private String JZSJ;    //截止时间

    private String DWJBR;   //单位经办人

    private String BJSJ;    //办结时间

    public String getBJSJ() {
        return BJSJ;
    }

    public void setBJSJ(String BJSJ) {
        this.BJSJ = BJSJ;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    private String CZY;  //操作员

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
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

    public String getDWMC() {
        return DWMC;
    }

    public void setDWMC(String DWMC) {
        this.DWMC = DWMC;
    }

    public String getDWZH() {
        return DWZH;
    }

    public void setDWZH(String DWZH) {
        this.DWZH = DWZH;
    }

    public String getHBJNY() {
        return HBJNY;
    }

    public void setHBJNY(String HBJNY) {
        this.HBJNY = HBJNY;
    }

    public String getHJFS() {
        return HJFS;
    }

    public void setHJFS(String HJFS) {
        this.HJFS = HJFS;
    }

    public String getFSRS() {
        return FSRS;
    }

    public void setFSRS(String FSRS) {
        this.FSRS = FSRS;
    }

    public String getFSE() {
        return FSE;
    }

    public void setFSE(String FSE) {
        this.FSE = FSE;
    }

    public String getZJE() {
        return ZJE;
    }

    public void setZJE(String ZJE) {
        this.ZJE = ZJE;
    }

    public String getSKZHMC() {
        return SKZHMC;
    }

    public void setSKZHMC(String SKZHMC) {
        this.SKZHMC = SKZHMC;
    }

    public String getSKZH() {
        return SKZH;
    }

    public void setSKZH(String SKZH) {
        this.SKZH = SKZH;
    }

    public String getSTYHMC() {
        return STYHMC;
    }

    public void setSTYHMC(String STYHMC) {
        this.STYHMC = STYHMC;
    }

    public String getSTYHDM() {
        return STYHDM;
    }

    public void setSTYHDM(String STYHDM) {
        this.STYHDM = STYHDM;
    }

    public String getJZSJ() {
        return JZSJ;
    }

    public void setJZSJ(String JZSJ) {
        this.JZSJ = JZSJ;
    }

    public String getDWJBR() {
        return DWJBR;
    }

    public void setDWJBR(String DWJBR) {
        this.DWJBR = DWJBR;
    }

    @Override
    public String toString() {
        return "UnitRemittanceNotice{" +
                "YWLSH='" + YWLSH + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", TZRQ='" + TZRQ + '\'' +
                ", DWMC='" + DWMC + '\'' +
                ", DWZH='" + DWZH + '\'' +
                ", HBJNY='" + HBJNY + '\'' +
                ", HJFS='" + HJFS + '\'' +
                ", FSRS='" + FSRS + '\'' +
                ", FSE=" + FSE +
                ", ZJE='" + ZJE + '\'' +
                ", SKZHMC='" + SKZHMC + '\'' +
                ", SKZH='" + SKZH + '\'' +
                ", STYHMC='" + STYHMC + '\'' +
                ", STYHDM='" + STYHDM + '\'' +
                ", JZSJ='" + JZSJ + '\'' +
                ", DWJBR='" + DWJBR + '\'' +
                ", CZY='" + CZY + '\'' +
                ", BJSJ='" + BJSJ + '\'' +
                '}';
    }
}

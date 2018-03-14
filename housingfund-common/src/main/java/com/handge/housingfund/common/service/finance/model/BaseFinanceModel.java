package com.handge.housingfund.common.service.finance.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by xuefei_wang on 17-8-22.
 */

@XmlRootElement(name = "BaseFinanceModel")
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseFinanceModel implements Serializable {

    private String id;

    private String YWLSH;

    private String YWMC;

    private String YWMCID;

    private String MBBH;

    private String STATUS;

    private String YWWD;

    private Object TASKCONTENT;

    private String CZY;

    private String SLSJ;

    private String HeJi;//合计

    private String BeiZhu;//备注

    private String ZHCLRY;

    private String JZPZH;//记账凭证号

    private String SBYY;//失败原因

    private boolean enable = false;

    public BaseFinanceModel() {

    }

    public BaseFinanceModel(String id, String YWLSH, String YWMC, String YWMCID, String MBBH,
                            String STATUS, String YWWD, Object TASKCONTENT, String CZY,
                            String SLSJ, String heJi, String BeiZhu, String ZHCLRY, String JZPZH, String SBYY) {
        this.id = id;
        this.YWLSH = YWLSH;
        this.YWMC = YWMC;
        this.YWMCID = YWMCID;
        this.MBBH = MBBH;
        this.STATUS = STATUS;
        this.YWWD = YWWD;
        this.TASKCONTENT = TASKCONTENT;
        this.CZY = CZY;
        this.SLSJ = SLSJ;
        this.HeJi = heJi;
        this.BeiZhu = BeiZhu;
        this.ZHCLRY = ZHCLRY;
        this.JZPZH = JZPZH;
        this.SBYY = SBYY;
    }

    public void setEnable(boolean enable){
        this.enable = enable;
    }

    public boolean getEnable(){
        return this.enable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public String getYWMC() {
        return YWMC;
    }

    public void setYWMC(String YWMC) {
        this.YWMC = YWMC;
    }

    public String getYWMCID() {
        return YWMCID;
    }

    public void setYWMCID(String YWMCID) {
        this.YWMCID = YWMCID;
    }

    public String getMBBH() {
        return MBBH;
    }

    public void setMBBH(String MBBH) {
        this.MBBH = MBBH;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public Object getTASKCONTENT() {
        return TASKCONTENT;
    }

    public void setTASKCONTENT(Object TASKCONTENT) {
        this.TASKCONTENT = TASKCONTENT;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getSLSJ() {
        return SLSJ;
    }

    public void setSLSJ(String SLSJ) {
        this.SLSJ = SLSJ;
    }

    public String getHeJi() {
        return HeJi;
    }

    public void setHeJi(String heJi) {
        HeJi = heJi;
    }

    public String getZHCLRY() {
        return ZHCLRY;
    }

    public void setZHCLRY(String ZHCLRY) {
        this.ZHCLRY = ZHCLRY;
    }

    public String getJZPZH() {
        return JZPZH;
    }

    public void setJZPZH(String JZPZH) {
        this.JZPZH = JZPZH;
    }

    public String getSBYY() {
        return SBYY;
    }

    public void setSBYY(String SBYY) {
        this.SBYY = SBYY;
    }

    public String getBeiZhu() {
        return BeiZhu;
    }

    public void setBeiZhu(String beiZhu) {
        BeiZhu = beiZhu;
    }

    @Override
    public String toString() {
        return "BaseFinanceModel{" +
                "id='" + id + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", YWMC='" + YWMC + '\'' +
                ", YWMCID='" + YWMCID + '\'' +
                ", MBBH='" + MBBH + '\'' +
                ", STATUS='" + STATUS + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", TASKCONTENT=" + TASKCONTENT +
                ", CZY='" + CZY + '\'' +
                ", SLSJ='" + SLSJ + '\'' +
                ", HeJi='" + HeJi + '\'' +
                ", BeiZhu='" + BeiZhu + '\'' +
                ", ZHCLRY='" + ZHCLRY + '\'' +
                ", JZPZH='" + JZPZH + '\'' +
                ", SBYY='" + SBYY + '\'' +
                '}';
    }
}

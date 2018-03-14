package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/7/27.
 * 描述
 */

@XmlRootElement(name = "Withdrawl")
@XmlAccessorType(XmlAccessType.FIELD)
public class Withdrawl implements Serializable {

    private String YWLSH;//业务流水号
    private String id;//业务流水号
    private String GRZH;//个人账号
    private String XingMing;//姓名
    private String FSE;//发生额
    private String ZhuangTai;//状态
    private String JZPZH;//记账凭证号
    private String DWMC;//单位名称
    private String CZY;//操作员
    private String YWWD;//业务网点
    private String SLSJ;//受理时间
    private String TQYY;//提取原因
    private String GRZHYE;//个人账户余额
    private String SBYY;//失败原因
    private String ZJHM;//证件号码
    private String ZongE;//总额额
    private String FSLXE;//发生利息额

    public String getZongE() {
        return ZongE;
    }

    public void setZongE(String zongE) {
        ZongE = zongE;
    }

    public String getFSLXE() {
        return FSLXE;
    }

    public void setFSLXE(String FSLXE) {
        this.FSLXE = FSLXE;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getSBYY() {
        return SBYY;
    }

    public void setSBYY(String SBYY) {
        this.SBYY = SBYY;
    }

    public String getTQYY() {
        return TQYY;
    }

    public void setTQYY(String TQYY) {
        this.TQYY = TQYY;
    }

    public String getGRZHYE() {
        return GRZHYE;
    }

    public void setGRZHYE(String GRZHYE) {
        this.GRZHYE = GRZHYE;
    }

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public String getGRZH() {
        return GRZH;
    }

    public void setGRZH(String GRZH) {
        this.GRZH = GRZH;
    }

    public String getXingMing() {
        return XingMing;
    }

    public void setXingMing(String xingMing) {
        XingMing = xingMing;
    }

    public String getFSE() {
        return FSE;
    }

    public void setFSE(String FSE) {
        this.FSE = FSE;
    }

    public String getZhuangTai() {
        return ZhuangTai;
    }

    public void setZhuangTai(String zhuangTai) {
        ZhuangTai = zhuangTai;
    }

    public String getJZPZH() {
        return JZPZH;
    }

    public void setJZPZH(String JZPZH) {
        this.JZPZH = JZPZH;
    }

    public String getDWMC() {
        return DWMC;
    }

    public void setDWMC(String DWMC) {
        this.DWMC = DWMC;
    }

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

    public String getSLSJ() {
        return SLSJ;
    }

    public void setSLSJ(String SLSJ) {
        this.SLSJ = SLSJ;
    }

    @Override
    public String toString() {
        return "Withdrawl{" +
                "YWLSH='" + YWLSH + '\'' +
                ", GRZH='" + GRZH + '\'' +
                ", XingMing='" + XingMing + '\'' +
                ", FSE='" + FSE + '\'' +
                ", ZhuangTai='" + ZhuangTai + '\'' +
                ", JZPZH='" + JZPZH + '\'' +
                ", DWMC='" + DWMC + '\'' +
                ", CZY='" + CZY + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", SLSJ='" + SLSJ + '\'' +
                ", TQYY='" + TQYY + '\'' +
                ", GRZHYE='" + GRZHYE + '\'' +
                ", SBYY='" + SBYY + '\'' +
                ", ZJHM='" + ZJHM + '\'' +
                '}';
    }
}

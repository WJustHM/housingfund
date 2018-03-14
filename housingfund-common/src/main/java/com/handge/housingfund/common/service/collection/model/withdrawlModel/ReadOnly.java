package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by xuefei_wang on 17-6-24.
 */
@XmlRootElement(name = "ReadOnly")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReadOnly implements Serializable {
    /**
     * @param XingMing 姓名
     * @param zjlx 证件类型
     * @param zjhm 证件号码
     * @param dwmc 单位名称
     * @param jzny 缴至年月
     * @param grzhye 个人账户余额
     * @param grzhzt 个人账户状态
     * @param grckzhhm 个人存款账户号码
     * @param HuMing 户名
     * @param grckzhkhyhmc 个人存款账户开户银行名称
     * @param jqe 结清额
     * @param hkfs 还款方式
     * @param fse 发生额
     * @param grzh 个人账号
     */
    @XmlElement(name = "XingMing")
    private String XingMing;
    @XmlElement(name = "ZJLX")
    private String zjlx;
    @XmlElement(name = "ZJHM")
    private String zjhm;
    @XmlElement(name = "GRZH")
    private String grzh;
    @XmlElement(name = "DWMC")
    private String dwmc;
    @XmlElement(name = "JZNY")
    private String jzny;
    @XmlElement(name = "GRZHYE")
    private String grzhye;
    @XmlElement(name = "GRZHZT")
    private String grzhzt;
    @XmlElement(name = "GRCKZHHM")
    private String grckzhhm;
    @XmlElement(name = "HuMing")
    private String HuMing;
    @XmlElement(name = "GRCKZHKHYHMC")
    private String grckzhkhyhmc;
    @XmlElement(name = "JQE")
    private String jqe;
    @XmlElement(name = "HKFS")
    private String hkfs;
    @XmlElement(name = "FSE")
    private String fse;
    @XmlElement(name = "JKRZJHM")
    private String jkrzjhm;
    @XmlElement(name = "JKRXM")
    private String jkrxm;
    @XmlElement(name = "DKZJHM")
    private String dkzjhm;

    public String getDkzjhm() {
        return dkzjhm;
    }

    public void setDkzjhm(String dkzjhm) {
        this.dkzjhm = dkzjhm;
    }

    public String getJkrxm() {
        return jkrxm;
    }

    public void setJkrxm(String jkrxm) {
        this.jkrxm = jkrxm;
    }

    public String getJkrzjhm() {
        return jkrzjhm;
    }

    public void setJkrzjhm(String jkrzjhm) {
        this.jkrzjhm = jkrzjhm;
    }

    public String getGrzh() {
        return grzh;
    }

    public void setGrzh(String grzh) {
        this.grzh = grzh;
    }

    public String getFse() {
        return fse;
    }

    public void setFse(String fse) {
        this.fse = fse;
    }

    public String getJqe() {
        return jqe;
    }

    public void setJqe(String jqe) {
        this.jqe = jqe;
    }

    public String getHkfs() {
        return hkfs;
    }

    public void setHkfs(String hkfs) {
        this.hkfs = hkfs;
    }

    public String getGrckzhhm() {
        return grckzhhm;
    }

    public void setGrckzhhm(String grckzhhm) {
        this.grckzhhm = grckzhhm;
    }

    public String getHuMing() {
        return HuMing;
    }

    public void setHuMing(String huMing) {
        HuMing = huMing;
    }

    public String getGrckzhkhyhmc() {
        return grckzhkhyhmc;
    }

    public void setGrckzhkhyhmc(String grckzhkhyhmc) {
        this.grckzhkhyhmc = grckzhkhyhmc;
    }

    public String getXingMing() {
        return XingMing;
    }

    public void setXingMing(String xingMing) {
        XingMing = xingMing;
    }

    public String getZjlx() {
        return zjlx;
    }

    public void setZjlx(String zjlx) {
        this.zjlx = zjlx;
    }

    public String getZjhm() {
        return zjhm;
    }

    public void setZjhm(String zjhm) {
        this.zjhm = zjhm;
    }

    public String getDwmc() {
        return dwmc;
    }

    public void setDwmc(String dwmc) {
        this.dwmc = dwmc;
    }

    public String getJzny() {
        return jzny;
    }

    public void setJzny(String jzny) {
        this.jzny = jzny;
    }

    public String getGrzhye() {
        return grzhye;
    }

    public void setGrzhye(String grzhye) {
        this.grzhye = grzhye;
    }

    public String getGrzhzt() {
        return grzhzt;
    }

    public void setGrzhzt(String grzhzt) {
        this.grzhzt = grzhzt;
    }

    public ReadOnly() {

    }

    @Override
    public String toString() {
        return "ReadOnly{" +
                "XingMing='" + XingMing + '\'' +
                ", zjlx='" + zjlx + '\'' +
                ", zjhm='" + zjhm + '\'' +
                ", grzh='" + grzh + '\'' +
                ", dwmc='" + dwmc + '\'' +
                ", jzny='" + jzny + '\'' +
                ", grzhye='" + grzhye + '\'' +
                ", grzhzt='" + grzhzt + '\'' +
                ", grckzhhm='" + grckzhhm + '\'' +
                ", HuMing='" + HuMing + '\'' +
                ", grckzhkhyhmc='" + grckzhkhyhmc + '\'' +
                ", jqe='" + jqe + '\'' +
                ", hkfs='" + hkfs + '\'' +
                ", fse='" + fse + '\'' +
                ", isGtjkr='" + jkrzjhm + '\'' +
                '}';
    }

    static public ReadOnly createReadOnly() {
        return new ReadOnly();
    }


}
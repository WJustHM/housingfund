package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/7/15.
 * 描述
 */
@XmlRootElement(name = "IndiAcctInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class IndiAcctInfo implements Serializable {
    /**
     * @param ywlsh 业务流水号
     * @param grzh 个人账号
     * @param XingMing 姓名
     * @param zjlx 证件类型
     * @param zjhm 证件号码
     * @param dwmc 单位名称
     * @param jzny 缴至年月
     * @param grzhye 个人账户余额
     * @param grzhzt 个人账户状态
     */
    @XmlElement(name = "YWLSH")
    private String ywlsh;
    @XmlElement(name = "GRZH")
    private String grzh;
    @XmlElement(name = "XingMing")
    private String XingMing;
    @XmlElement(name = "ZJLX")
    private String zjlx;
    @XmlElement(name = "ZJHM")
    private String zjhm;
    @XmlElement(name = "DWMC")
    private String dwmc;
    @XmlElement(name = "JZNY")
    private String jzny;
    @XmlElement(name = "GRZHYE")
    private String grzhye;
    @XmlElement(name = "GRZHZT")
    private String grzhzt;

    public String getYwlsh() {
        return ywlsh;
    }

    public void setYwlsh(String ywlsh) {
        this.ywlsh = ywlsh;
    }

    public String getGrzh() {
        return grzh;
    }

    public void setGrzh(String grzh) {
        this.grzh = grzh;
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

    @Override
    public String toString() {
        return "IndiAcctInfo{" +
                "ywlsh='" + ywlsh + '\'' +
                ", grzh='" + grzh + '\'' +
                ", XingMing='" + XingMing + '\'' +
                ", zjlx='" + zjlx + '\'' +
                ", zjhm='" + zjhm + '\'' +
                ", dwmc='" + dwmc + '\'' +
                ", jzny='" + jzny + '\'' +
                ", grzhye=" + grzhye +
                ", grzhzt='" + grzhzt + '\'' +
                '}';
    }
}

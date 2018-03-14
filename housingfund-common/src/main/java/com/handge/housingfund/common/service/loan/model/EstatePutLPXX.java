package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "EstatePutLPXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class EstatePutLPXX  implements Serializable {
    @Annotation.Money(name = "建筑总金额")
    private String JZZJE;  //建筑总金额
    @Annotation.Money(name = "获取土地单价")
    private String HQTDDJ;  //获取土地单价
    @Annotation.Number(name = "业务流水号")
    private String YWLSH;  //业务流水号

    private String YSXKZH;  //预售许可证号

    private String LXR;  //联系人
    @Annotation.Phone(name = "联系电话",regex = "((^[0][1-9]{2,3}-[0-9]{5,10}$)|(^[1-9]{1}[0-9]{5,8}$))|(^[1][0-9]{10}$)")
    private String LXDH;  //联系电话
    @Annotation.Money(name = "获取土地总价")
    private String HQTDZJ;  //获取土地总价

    private String AJXYRQ;  //按揭协议日期
    @Annotation.Ratio(name = "保证金比例")
    private String BZJBL;  //保证金比例

    private String LPMC;  //楼盘名称

    private String BeiZhu;  //备注

    private String FKGS;  //房开公司

    private String LPDZ;  //楼盘地址

    private String JZZMJ;  //建筑总面积

    public String getJZZJE() {

        return this.JZZJE;

    }


    public void setJZZJE(String JZZJE) {

        this.JZZJE = JZZJE;

    }


    public String getHQTDDJ() {

        return this.HQTDDJ;

    }


    public void setHQTDDJ(String HQTDDJ) {

        this.HQTDDJ = HQTDDJ;

    }


    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getYSXKZH() {

        return this.YSXKZH;

    }


    public void setYSXKZH(String YSXKZH) {

        this.YSXKZH = YSXKZH;

    }


    public String getLXR() {

        return this.LXR;

    }


    public void setLXR(String LXR) {

        this.LXR = LXR;

    }


    public String getLXDH() {

        return this.LXDH;

    }


    public void setLXDH(String LXDH) {

        this.LXDH = LXDH;

    }


    public String getHQTDZJ() {

        return this.HQTDZJ;

    }


    public void setHQTDZJ(String HQTDZJ) {

        this.HQTDZJ = HQTDZJ;

    }


    public String getAJXYRQ() {

        return this.AJXYRQ;

    }


    public void setAJXYRQ(String AJXYRQ) {

        this.AJXYRQ = AJXYRQ;

    }


    public String getBZJBL() {

        return this.BZJBL;

    }


    public void setBZJBL(String BZJBL) {

        this.BZJBL = BZJBL;

    }


    public String getLPMC() {

        return this.LPMC;

    }


    public void setLPMC(String LPMC) {

        this.LPMC = LPMC;

    }


    public String getBeiZhu() {

        return this.BeiZhu;

    }


    public void setBeiZhu(String BeiZhu) {

        this.BeiZhu = BeiZhu;

    }


    public String getFKGS() {

        return this.FKGS;

    }


    public void setFKGS(String FKGS) {

        this.FKGS = FKGS;

    }


    public String getLPDZ() {

        return this.LPDZ;

    }


    public void setLPDZ(String LPDZ) {

        this.LPDZ = LPDZ;

    }


    public String getJZZMJ() {

        return this.JZZMJ;

    }


    public void setJZZMJ(String JZZMJ) {

        this.JZZMJ = JZZMJ;

    }


    public String toString() {

        return "EstatePutLPXX{" +

                "JZZJE='" + this.JZZJE + '\'' + "," +
                "HQTDDJ='" + this.HQTDDJ + '\'' + "," +
                "YWLSH='" + this.YWLSH + '\'' + "," +
                "YSXKZH='" + this.YSXKZH + '\'' + "," +
                "LXR='" + this.LXR + '\'' + "," +
                "LXDH='" + this.LXDH + '\'' + "," +
                "HQTDZJ='" + this.HQTDZJ + '\'' + "," +
                "AJXYRQ='" + this.AJXYRQ + '\'' + "," +
                "BZJBL='" + this.BZJBL + '\'' + "," +
                "LPMC='" + this.LPMC + '\'' + "," +
                "BeiZhu='" + this.BeiZhu + '\'' + "," +
                "FKGS='" + this.FKGS + '\'' + "," +
                "LPDZ='" + this.LPDZ + '\'' + "," +
                "JZZMJ='" + this.JZZMJ + '\'' +

                "}";

    }
}
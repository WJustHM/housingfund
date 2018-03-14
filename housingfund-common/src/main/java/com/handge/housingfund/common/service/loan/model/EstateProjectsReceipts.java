package com.handge.housingfund.common.service.loan.model;

import java.io.Serializable;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/8/7.
 * 描述
 */
public class EstateProjectsReceipts  implements Serializable {
    private String YZM;  //验证码

    private String YWLSH;  //业务流水号

    private String TZRQ;  //填制日期

    private String CZY; //操作员

    private EstateProjectInfoLPXX LPXX;//楼盘信息

    private EstateProjectInfoLDXX LDXXs;//楼栋信息集合

    public String getYZM() {
        return YZM;
    }

    public void setYZM(String YZM) {
        this.YZM = YZM;
    }

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public String getTZRQ() {
        return TZRQ;
    }

    public void setTZRQ(String TZRQ) {
        this.TZRQ = TZRQ;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public EstateProjectInfoLPXX getLPXX() {
        return LPXX;
    }

    public void setLPXX(EstateProjectInfoLPXX LPXX) {
        this.LPXX = LPXX;
    }

    public EstateProjectInfoLDXX getLDXXs() {
        return LDXXs;
    }

    public void setLDXXs(EstateProjectInfoLDXX LDXXs) {
        this.LDXXs = LDXXs;
    }

    @Override
    public String toString() {
        return "EstateProjectsReceipts{" +
                "YZM='" + YZM + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", TZRQ='" + TZRQ + '\'' +
                ", CZY='" + CZY + '\'' +
                ", LPXX=" + LPXX +
                ", LDXXs=" + LDXXs +
                '}';
    }
}

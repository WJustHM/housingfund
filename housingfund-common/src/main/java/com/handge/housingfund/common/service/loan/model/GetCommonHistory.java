package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by 向超 on 2017/8/22.
 */
@XmlRootElement(name = "GetCommonHistory")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetCommonHistory implements Serializable{

    private String YWLX;//业务类型

    private String SLSJ;//受理时间

    private String CZNR;//操作内容

    private String CZY;//操作员

    private String YWWD;//业务网点

    private String  CZQD;//操作渠道

    private String YWLSH;//业务流水号

    public String getCZQD() {
        return CZQD;
    }

    public void setCZQD(String CZQD) {
        this.CZQD = CZQD;
    }

    public String getYWLX() {
        return YWLX;
    }

    public void setYWLX(String YWLX) {
        this.YWLX = YWLX;
    }

    public String getSLSJ() {
        return SLSJ;
    }

    public void setSLSJ(String SLSJ) {
        this.SLSJ = SLSJ;
    }

    public String getCZNR() {
        return CZNR;
    }

    public void setCZNR(String CZNR) {
        this.CZNR = CZNR;
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

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    @Override
    public String toString() {
        return "GetCommonHistory{" +
                "YWLX='" + YWLX + '\'' +
                ", SLSJ='" + SLSJ + '\'' +
                ", CZNR='" + CZNR + '\'' +
                ", CZY='" + CZY + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", CZQD='" + CZQD + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                '}';
    }
}

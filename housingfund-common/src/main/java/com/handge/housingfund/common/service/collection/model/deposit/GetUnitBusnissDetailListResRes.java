package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "GetUnitBusnissDetailListResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUnitBusnissDetailListResRes  implements Serializable{


   private  String    YWLSH;  //业务流水号 1

   private  String    YWMXLX;  //业务明细类型 1

   private  String    DWMC;  //单位名称 1

   private  String    DWZH;  //单位账号 1

    private String id;

    private String FSE;
    private String FSLXE;
    private String FSRS;
    private String HBJNY;
    private String JZRQ;
    private String CZBS;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCZBS() {
        return CZBS;
    }

    public void setCZBS(String CZBS) {
        this.CZBS = CZBS;
    }

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public String getYWMXLX() {
        return YWMXLX;
    }

    public void setYWMXLX(String YWMXLX) {
        this.YWMXLX = YWMXLX;
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

    public String getFSE() {
        return FSE;
    }

    public void setFSE(String FSE) {
        this.FSE = FSE;
    }

    public String getFSLXE() {
        return FSLXE;
    }

    public void setFSLXE(String FSLXE) {
        this.FSLXE = FSLXE;
    }

    public String getFSRS() {
        return FSRS;
    }

    public void setFSRS(String FSRS) {
        this.FSRS = FSRS;
    }

    public String getHBJNY() {
        return HBJNY;
    }

    public void setHBJNY(String HBJNY) {
        this.HBJNY = HBJNY;
    }

    public String getJZRQ() {
        return JZRQ;
    }

    public void setJZRQ(String JZRQ) {
        this.JZRQ = JZRQ;
    }

    @Override
    public String toString() {
        return "GetUnitBusnissDetailListResRes{" +
                "YWLSH='" + YWLSH + '\'' +
                ", YWMXLX='" + YWMXLX + '\'' +
                ", DWMC='" + DWMC + '\'' +
                ", DWZH='" + DWZH + '\'' +
                ", FSE='" + FSE + '\'' +
                ", FSLXE='" + FSLXE + '\'' +
                ", FSRS='" + FSRS + '\'' +
                ", HBJNY='" + HBJNY + '\'' +
                ", JZRQ='" + JZRQ + '\'' +
                '}';
    }
}
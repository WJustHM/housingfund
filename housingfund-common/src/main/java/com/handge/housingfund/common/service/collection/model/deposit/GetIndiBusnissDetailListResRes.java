package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GetIndiBusnissDetailListResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetIndiBusnissDetailListResRes implements Serializable {


    private String YWLSH;  //业务流水号

    private String YWMXLX;  //业务明细类型


    private String CZBS;  //冲账标识

    private String GRZH;  //个人账号

    private String XingMing;  //姓名

    private String ZJHM;

    private String id;

    private String FSE;
    private String DNGJFSE;
    private String SNJZFSE;
    private String FSLXE;
    private String TQYY;
    private String TQFS;
    private String JZRQ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJZRQ() {
        return JZRQ;
    }

    public void setJZRQ(String JZRQ) {
        this.JZRQ = JZRQ;
    }

    public String getFSE() {
        return FSE;
    }

    public void setFSE(String FSE) {
        this.FSE = FSE;
    }

    public String getDNGJFSE() {
        return DNGJFSE;
    }

    public void setDNGJFSE(String DNGJFSE) {
        this.DNGJFSE = DNGJFSE;
    }

    public String getSNJZFSE() {
        return SNJZFSE;
    }

    public void setSNJZFSE(String SNJZFSE) {
        this.SNJZFSE = SNJZFSE;
    }

    public String getFSLXE() {
        return FSLXE;
    }

    public void setFSLXE(String FSLXE) {
        this.FSLXE = FSLXE;
    }

    public String getTQYY() {
        return TQYY;
    }

    public void setTQYY(String TQYY) {
        this.TQYY = TQYY;
    }

    public String getTQFS() {
        return TQFS;
    }

    public void setTQFS(String TQFS) {
        this.TQFS = TQFS;
    }

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getYWLSH() {

        return this.YWLSH;

    }


    public void setYWLSH(String YWLSH) {

        this.YWLSH = YWLSH;

    }


    public String getYWMXLX() {

        return this.YWMXLX;

    }


    public void setYWMXLX(String YWMXLX) {

        this.YWMXLX = YWMXLX;

    }


    public String getCZBS() {

        return this.CZBS;

    }


    public void setCZBS(String CZBS) {

        this.CZBS = CZBS;

    }


    public String getGRZH() {

        return this.GRZH;

    }


    public void setGRZH(String GRZH) {

        this.GRZH = GRZH;

    }


    public String getXingMing() {

        return this.XingMing;

    }


    public void setXingMing(String XingMing) {

        this.XingMing = XingMing;

    }


}
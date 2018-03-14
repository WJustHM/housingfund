package com.handge.housingfund.common.service.collection.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by 凡 on 2017/10/10.
 */
@XmlRootElement(name = "InventoryInit")
@XmlAccessorType(XmlAccessType.FIELD)
public class InventoryInit implements Serializable {

    private String DWZH;

    private String DWMC;

    private String JZNY;

    private String WFTYE;

    private String QCNYQ;

    private String QCNYZ;

    private String JBRXM;

    private String JBRZJLX;

    private String JBRZJHM;

    private String CZY;

    private String YWWD;

    public String getDWZH() {
        return DWZH;
    }

    public void setDWZH(String DWZH) {
        this.DWZH = DWZH;
    }

    public String getDWMC() {
        return DWMC;
    }

    public void setDWMC(String DWMC) {
        this.DWMC = DWMC;
    }

    public String getJZNY() {
        return JZNY;
    }

    public void setJZNY(String JZNY) {
        this.JZNY = JZNY;
    }

    public String getWFTYE() {
        return WFTYE;
    }

    public void setWFTYE(String WFTYE) {
        this.WFTYE = WFTYE;
    }

    public String getQCNYQ() {
        return QCNYQ;
    }

    public void setQCNYQ(String QCNYQ) {
        this.QCNYQ = QCNYQ;
    }

    public String getJBRXM() {
        return JBRXM;
    }

    public void setJBRXM(String JBRXM) {
        this.JBRXM = JBRXM;
    }

    public String getJBRZJLX() {
        return JBRZJLX;
    }

    public void setJBRZJLX(String JBRZJLX) {
        this.JBRZJLX = JBRZJLX;
    }

    public String getJBRZJHM() {
        return JBRZJHM;
    }

    public void setJBRZJHM(String JBRZJHM) {
        this.JBRZJHM = JBRZJHM;
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

    public String getQCNYZ() {
        return QCNYZ;
    }

    public void setQCNYZ(String QCNYZ) {
        this.QCNYZ = QCNYZ;
    }
}

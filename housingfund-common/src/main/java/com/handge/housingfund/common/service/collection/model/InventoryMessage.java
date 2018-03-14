package com.handge.housingfund.common.service.collection.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 凡 on 2017/10/7.
 */
@XmlRootElement(name = "InventoryMessage")
@XmlAccessorType(XmlAccessType.FIELD)
public class InventoryMessage implements Serializable {

    private String QCNY; //清册年月

    private String DWJCRS; //单位缴存人数

    private String DWFCRS; //单位封存人数

    private String FSRS; //发生人数

    private String FSE; //发生额

    private String DWJCBL; //单位缴存比例

    private String GRJCBL; //个人缴存比例

    private String DWYJCEHJ; //单位月缴存额合计

    private String GRYJCEHJ; //个人月缴存额合计

    private ArrayList<InventoryDetail> QCXQ;

    public String getQCNY() {
        return QCNY;
    }

    public void setQCNY(String QCNY) {
        this.QCNY = QCNY;
    }

    public String getDWJCRS() {
        return DWJCRS;
    }

    public void setDWJCRS(String DWJCRS) {
        this.DWJCRS = DWJCRS;
    }

    public String getDWFCRS() {
        return DWFCRS;
    }

    public void setDWFCRS(String DWFCRS) {
        this.DWFCRS = DWFCRS;
    }

    public String getFSRS() {
        return FSRS;
    }

    public void setFSRS(String FSRS) {
        this.FSRS = FSRS;
    }

    public String getFSE() {
        return FSE;
    }

    public void setFSE(String FSE) {
        this.FSE = FSE;
    }

    public String getDWJCBL() {
        return DWJCBL;
    }

    public void setDWJCBL(String DWJCBL) {
        this.DWJCBL = DWJCBL;
    }

    public String getGRJCBL() {
        return GRJCBL;
    }

    public void setGRJCBL(String GRJCBL) {
        this.GRJCBL = GRJCBL;
    }

    public ArrayList<InventoryDetail> getQCXQ() {
        return QCXQ;
    }

    public void setQCXQ(ArrayList<InventoryDetail> QCXQ) {
        this.QCXQ = QCXQ;
    }

    public String getDWYJCEHJ() {
        return DWYJCEHJ;
    }

    public void setDWYJCEHJ(String DWYJCEHJ) {
        this.DWYJCEHJ = DWYJCEHJ;
    }

    public String getGRYJCEHJ() {
        return GRYJCEHJ;
    }

    public void setGRYJCEHJ(String GRYJCEHJ) {
        this.GRYJCEHJ = GRYJCEHJ;
    }

    @Override
    public String toString() {
        return "InventoryMessage{" +
                "QCNY='" + QCNY + '\'' +
                ", DWJCRS=" + DWJCRS +
                ", DWFCRS=" + DWFCRS +
                ", FSRS=" + FSRS +
                ", FSE=" + FSE +
                ", DWJCBL='" + DWJCBL + '\'' +
                ", GRJCBL='" + GRJCBL + '\'' +
                ", QCXQ=" + QCXQ +
                '}';
    }
}

package com.handge.housingfund.common.service.collection.model.deposit;

import com.handge.housingfund.common.service.collection.model.InventoryMessage;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "ExportInventoryConfirmationInfoRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExportInventoryConfirmationInfoRes implements Serializable {

    private  String    QCNY;  //清册年月

    private  String    YWWD;  //业务网点

    private  Integer    FSRS;  //发生人数

    private  String    TZSJ;  //填制时间

    private InventoryMessage inventoryMessage; //清册详情

    private  String    DWMC;  //单位名称

    private  String    DWZH;  //单位账号

    private String FSE; //发生额

    public String getQCNY() {
        return QCNY;
    }

    public void setQCNY(String QCNY) {
        this.QCNY = QCNY;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public Integer getFSRS() {
        return FSRS;
    }

    public void setFSRS(Integer FSRS) {
        this.FSRS = FSRS;
    }

    public String getTZSJ() {
        return TZSJ;
    }

    public void setTZSJ(String TZSJ) {
        this.TZSJ = TZSJ;
    }

    public InventoryMessage getInventoryMessage() {
        return inventoryMessage;
    }

    public void setInventoryMessage(InventoryMessage inventoryMessage) {
        this.inventoryMessage = inventoryMessage;
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
}

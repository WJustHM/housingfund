package com.handge.housingfund.common.service.collection.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by 凡 on 2017/10/9.
 */
@XmlRootElement(name = "InventoryChangeDetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class InventoryChangeDetail implements Serializable {

    private String GRZH; //个人账号

    private String XingMing; //姓名

    private String ZJHM; //证件号码

    private String FSE; //发生额

    private String BGYY; //变更原因

    public String getGRZH() {
        return GRZH;
    }

    public void setGRZH(String GRZH) {
        this.GRZH = GRZH;
    }

    public String getXingMing() {
        return XingMing;
    }

    public void setXingMing(String xingMing) {
        this.XingMing = xingMing;
    }

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getFSE() {
        return FSE;
    }

    public void setFSE(String FSE) {
        this.FSE = FSE;
    }

    public String getBGYY() {
        return BGYY;
    }

    public void setBGYY(String BGYY) {
        this.BGYY = BGYY;
    }

    @Override
    public String toString() {
        return "InventoryChangeDetail{" +
                "GRZH='" + GRZH + '\'' +
                ", XingMing='" + XingMing + '\'' +
                ", ZJHM='" + ZJHM + '\'' +
                ", FSE=" + FSE +
                ", BGYY='" + BGYY + '\'' +
                '}';
    }
}

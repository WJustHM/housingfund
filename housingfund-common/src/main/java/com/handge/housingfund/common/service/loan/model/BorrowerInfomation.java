package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Funnyboy on 2017/10/13.
 */
@XmlRootElement(name = "BorrowerInfomation")
@XmlAccessorType(XmlAccessType.FIELD)
public class BorrowerInfomation implements Serializable {
    private static final long serialVersionUID = 1279244156840293119L;
    String XingMing;
    String ZJHM;
    String SJHM;
    String JTZZ;

    public String getXingMing() {
        return XingMing;
    }

    public void setXingMing(String xingMing) {
        XingMing = xingMing;
    }

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getSJHM() {
        return SJHM;
    }

    public void setSJHM(String SJHM) {
        this.SJHM = SJHM;
    }

    public String getJTZZ() {
        return JTZZ;
    }

    public void setJTZZ(String JTZZ) {
        this.JTZZ = JTZZ;
    }

    @Override
    public String toString() {
        return "BorrowerInfomation{" +
                "XingMing='" + XingMing + '\'' +
                ", ZJHM='" + ZJHM + '\'' +
                ", SJHM='" + SJHM + '\'' +
                ", JTZZ='" + JTZZ + '\'' +
                '}';
    }
}

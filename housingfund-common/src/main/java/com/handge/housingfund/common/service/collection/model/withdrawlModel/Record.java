package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/7/28.
 * 描述
 */
@XmlRootElement(name = "record")
@XmlAccessorType(XmlAccessType.FIELD)
public class Record  implements Serializable {
    String TQRQ;//提取日期
    String BCTQJE;//本次提取金额
    String LJTQJE;//累计提取金额
    String BLR;//办理人
    String CZY;//操作员
    String YWWD;//业务网点

    public String getTQRQ() {
        return TQRQ;
    }

    public void setTQRQ(String TQRQ) {
        this.TQRQ = TQRQ;
    }

    public String getBCTQJE() {
        return BCTQJE;
    }

    public void setBCTQJE(String BCTQJE) {
        this.BCTQJE = BCTQJE;
    }

    public String getLJTQJE() {
        return LJTQJE;
    }

    public void setLJTQJE(String LJTQJE) {
        this.LJTQJE = LJTQJE;
    }

    public String getBLR() {
        return BLR;
    }

    public void setBLR(String BLR) {
        this.BLR = BLR;
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

    @Override
    public String toString() {
        return "Record{" +
                "TQRQ='" + TQRQ + '\'' +
                ", BCTQJE=" + BCTQJE +
                ", LJTQJE=" + LJTQJE +
                ", BLR='" + BLR + '\'' +
                ", CZY='" + CZY + '\'' +
                ", YWWD='" + YWWD + '\'' +
                '}';
    }
}

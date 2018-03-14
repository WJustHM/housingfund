package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/7/15.
 * 描述
 */
@XmlRootElement(name = "ReceiptIndiAcctInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReceiptIndiAcctInfo implements Serializable {
    /**
     * @param grzh 个人账号
     * @param XingMing 姓名
     * @param dwmc 单位名称
     * @param dwzh 单位账号
     */
    @XmlElement(name = "GRZH")
    private String grzh;
    @XmlElement(name = "XingMing")
    private String XingMing;
    @XmlElement(name = "DWMC")
    private String dwmc;
    @XmlElement(name = "DWZH")
    private String dwzh;

    public String getGrzh() {
        return grzh;
    }

    public void setGrzh(String grzh) {
        this.grzh = grzh;
    }

    public String getXingMing() {
        return XingMing;
    }

    public void setXingMing(String xingMing) {
        XingMing = xingMing;
    }

    public String getDwmc() {
        return dwmc;
    }

    public void setDwmc(String dwmc) {
        this.dwmc = dwmc;
    }

    public String getDwzh() {
        return dwzh;
    }

    public void setDwzh(String dwzh) {
        this.dwzh = dwzh;
    }

    @Override
    public String toString() {
        return "IndiAcctInfo{" +
                "grzh='" + grzh + '\'' +
                ", XingMing='" + XingMing + '\'' +
                ", dwmc='" + dwmc + '\'' +
                ", dwzh='" + dwzh + '\'' +
                '}';
    }
}

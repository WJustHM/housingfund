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
@XmlRootElement(name = "ReceiptWithdrawlsInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReceiptWithdrawlsInfo implements Serializable {
    /**
     * @param tqyy 提取原因
     * @param sfxh 是否销户(11 部分提取 12销户提取)
     * @param blr 办理人
     * @param zjhm 证件号码
     * @param tqje 提取金额
     * @param dqye 当前余额
     * @param skyhkh 收款银行卡号
     * @param skyh 收款银行
     * @param blsj 办理时间
     * @param xctqrq 下次提取日期
     */
    @XmlElement(name = "TQYY")
    private String tqyy;
    @XmlElement(name = "SFXH")
    private String sfxh;
    @XmlElement(name = "BLR")
    private String blr;
    @XmlElement(name = "ZJHM")
    private String zjhm;
    @XmlElement(name = "TQJE")
    private String tqje;
    @XmlElement(name = "DQYE")
    private String dqye;
    @XmlElement(name = "SKYHKH")
    private String skyhkh;
    @XmlElement(name = "SKYH")
    private String skyh;
    @XmlElement(name = "BLSJ")
    private String blsj;
    @XmlElement(name = "XCTQRQ")
    private String xctqrq;
    @XmlElement(name = "ZongE")
    private String zongE;

    public String getFslxe() {
        return fslxe;
    }

    public void setFslxe(String fslxe) {
        this.fslxe = fslxe;
    }

    @XmlElement(name = "FSLXE")
    private String fslxe
            ;

    public String getZongE() {
        return zongE;
    }

    public void setZongE(String zongE) {
        this.zongE = zongE;
    }

    public String getTqyy() {
        return tqyy;
    }

    public void setTqyy(String tqyy) {
        this.tqyy = tqyy;
    }

    public String getSfxh() {
        return sfxh;
    }

    public void setSfxh(String sfxh) {
        this.sfxh = sfxh;
    }

    public String getBlr() {
        return blr;
    }

    public void setBlr(String blr) {
        this.blr = blr;
    }

    public String getZjhm() {
        return zjhm;
    }

    public void setZjhm(String zjhm) {
        this.zjhm = zjhm;
    }

    public String getTqje() {
        return tqje;
    }

    public void setTqje(String tqje) {
        this.tqje = tqje;
    }

    public String getDqye() {
        return dqye;
    }

    public void setDqye(String dqye) {
        this.dqye = dqye;
    }

    public String getSkyhkh() {
        return skyhkh;
    }

    public void setSkyhkh(String skyhkh) {
        this.skyhkh = skyhkh;
    }

    public String getSkyh() {
        return skyh;
    }

    public void setSkyh(String skyh) {
        this.skyh = skyh;
    }

    public String getBlsj() {
        return blsj;
    }

    public void setBlsj(String blsj) {
        this.blsj = blsj;
    }

    public String getXctqrq() {
        return xctqrq;
    }

    public void setXctqrq(String xctqrq) {
        this.xctqrq = xctqrq;
    }

}

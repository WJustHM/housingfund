package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by gxy on 17-9-21.
 */
@XmlRootElement(name = "定期存款明细信息")
@XmlAccessorType(XmlAccessType.FIELD)
public class FixedDepositDetails implements Serializable {

    private static final long serialVersionUID = 1270093044928664363L;
    private String id;
    //定期存款编号
    private String DQCKBH;
    //专户号码
    private String ZHHM;
    //账户名称
    private String ZHMC;
    //开户银行名称
    private String KHYHMC;
    //利率
    private String LILV;
    //本金金额
    private String BJJE;
    //存入日期
    private String CRRQ;
    //到期日期
    private String DQRQ;
    //存款期限
    private String CKQX;
    //支取情况
    private String ZQQK;
    //取款日期
    private String QKRQ;
    //利息收入
    private String LXSR;
    //操作员
    private String CZY;
    //受理日期
    private String SLRQ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDQCKBH() {
        return DQCKBH;
    }

    public void setDQCKBH(String DQCKBH) {
        this.DQCKBH = DQCKBH;
    }

    public String getZHHM() {
        return ZHHM;
    }

    public void setZHHM(String ZHHM) {
        this.ZHHM = ZHHM;
    }

    public String getZHMC() {
        return ZHMC;
    }

    public void setZHMC(String ZHMC) {
        this.ZHMC = ZHMC;
    }

    public String getKHYHMC() {
        return KHYHMC;
    }

    public void setKHYHMC(String KHYHMC) {
        this.KHYHMC = KHYHMC;
    }

    public String getLILV() {
        return LILV;
    }

    public void setLILV(String LILV) {
        this.LILV = LILV;
    }

    public String getBJJE() {
        return BJJE;
    }

    public void setBJJE(String BJJE) {
        this.BJJE = BJJE;
    }

    public String getCRRQ() {
        return CRRQ;
    }

    public void setCRRQ(String CRRQ) {
        this.CRRQ = CRRQ;
    }

    public String getDQRQ() {
        return DQRQ;
    }

    public void setDQRQ(String DQRQ) {
        this.DQRQ = DQRQ;
    }

    public String getCKQX() {
        return CKQX;
    }

    public void setCKQX(String CKQX) {
        this.CKQX = CKQX;
    }

    public String getZQQK() {
        return ZQQK;
    }

    public void setZQQK(String ZQQK) {
        this.ZQQK = ZQQK;
    }

    public String getQKRQ() {
        return QKRQ;
    }

    public void setQKRQ(String QKRQ) {
        this.QKRQ = QKRQ;
    }

    public String getLXSR() {
        return LXSR;
    }

    public void setLXSR(String LXSR) {
        this.LXSR = LXSR;
    }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getSLRQ() {
        return SLRQ;
    }

    public void setSLRQ(String SLRQ) {
        this.SLRQ = SLRQ;
    }
}

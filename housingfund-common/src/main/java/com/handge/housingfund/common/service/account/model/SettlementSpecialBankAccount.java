package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/5.
 */
@XmlRootElement(name = "银行专户信息")
@XmlAccessorType(XmlAccessType.FIELD)
public class SettlementSpecialBankAccount implements Serializable {

    private static final long serialVersionUID = 5327680573357999094L;

    private String id;
    //科目编号
    private String KMBH;
    //科目名称
    private String KMMC;
    //银行专户号码
    private String YHZHHM;
    //银行专户名称
    private String YHZHMC;
    //银行代码 （3位代码）
    private String YHDM;
    //银行名称
    private String YHMC;
    //开户日期 YYYYMMDD
    private String KHRQ;
    //专户性质 （2位）
    private String ZHXZ;
    //销户日期 YYYYMMDD
    private String XHRQ;
    //是否已使用
    private Boolean SFYSY;
    //专户状态（0：正常， 1：销户）
    private String status;
    //银行节点号
    private String node;
    //银行id
    private String YHID;
    //客户编号
    private String KHBH;

    public SettlementSpecialBankAccount() {
    }

    public SettlementSpecialBankAccount(String id, String KMBH, String KMMC, String YHZHHM, String YHZHMC, String YHDM, String YHMC, String KHRQ, String ZHXZ, String XHRQ, Boolean SFYSY, String status, String node, String YHID, String KHBH) {
        this.id = id;
        this.KMBH = KMBH;
        this.KMMC = KMMC;
        this.YHZHHM = YHZHHM;
        this.YHZHMC = YHZHMC;
        this.YHDM = YHDM;
        this.YHMC = YHMC;
        this.KHRQ = KHRQ;
        this.ZHXZ = ZHXZ;
        this.XHRQ = XHRQ;
        this.SFYSY = SFYSY;
        this.status = status;
        this.node = node;
        this.YHID = YHID;
        this.KHBH = KHBH;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKMBH() {
        return KMBH;
    }

    public void setKMBH(String KMBH) {
        this.KMBH = KMBH;
    }

    public String getYHZHHM() {
        return YHZHHM;
    }

    public void setYHZHHM(String YHZHHM) {
        this.YHZHHM = YHZHHM;
    }

    public String getYHZHMC() {
        return YHZHMC;
    }

    public void setYHZHMC(String YHZHMC) {
        this.YHZHMC = YHZHMC;
    }

    public String getYHDM() {
        return YHDM;
    }

    public void setYHDM(String YHDM) {
        this.YHDM = YHDM;
    }

    public String getYHMC() {
        return YHMC;
    }

    public void setYHMC(String YHMC) {
        this.YHMC = YHMC;
    }

    public String getKHRQ() {
        return KHRQ;
    }

    public void setKHRQ(String KHRQ) {
        this.KHRQ = KHRQ;
    }

    public String getZHXZ() {
        return ZHXZ;
    }

    public void setZHXZ(String ZHXZ) {
        this.ZHXZ = ZHXZ;
    }

    public String getXHRQ() {
        return XHRQ;
    }

    public void setXHRQ(String XHRQ) {
        this.XHRQ = XHRQ;
    }

    public Boolean getSFYSY() {
        return SFYSY;
    }

    public void setSFYSY(Boolean SFYSY) {
        this.SFYSY = SFYSY;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getYHID() {
        return YHID;
    }

    public void setYHID(String YHID) {
        this.YHID = YHID;
    }

    public String getKHBH() {
        return KHBH;
    }

    public void setKHBH(String KHBH) {
        this.KHBH = KHBH;
    }

    public String getKMMC() {
        return KMMC;
    }

    public void setKMMC(String KMMC) {
        this.KMMC = KMMC;
    }

    @Override
    public String toString() {
        return "SettlementSpecialBankAccount{" +
                "id='" + id + '\'' +
                ", KMBH='" + KMBH + '\'' +
                ", KMMC='" + KMMC + '\'' +
                ", YHZHHM='" + YHZHHM + '\'' +
                ", YHZHMC='" + YHZHMC + '\'' +
                ", YHDM='" + YHDM + '\'' +
                ", YHMC='" + YHMC + '\'' +
                ", KHRQ='" + KHRQ + '\'' +
                ", ZHXZ='" + ZHXZ + '\'' +
                ", XHRQ='" + XHRQ + '\'' +
                ", SFYSY=" + SFYSY +
                ", status='" + status + '\'' +
                ", node='" + node + '\'' +
                ", YHID='" + YHID + '\'' +
                ", KHBH='" + KHBH + '\'' +
                '}';
    }
}

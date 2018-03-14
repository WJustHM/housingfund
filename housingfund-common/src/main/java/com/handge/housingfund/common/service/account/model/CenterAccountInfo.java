package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/7.
 */
@XmlRootElement(name = "中心账户信息")
@XmlAccessorType(XmlAccessType.FIELD)
public class CenterAccountInfo implements Serializable {
    private static final long serialVersionUID = -7137029033264034715L;

    //银行全称
    private String bank_name;

    //对手方联行号
    private String chgno;

    //银行节点号
    private String node;

    //银行代码，联行号前三位
    private String code;

    //银行专户号码
    private String YHZHHM;

    //银行专户名称
    private String YHZHMC;

    //专户性质 （2位）
    private String ZHXZ;

    //客户编号
    private String KHBH;

    //批量项目编号
    private String PLXMBH;

    //科目编号
    private String KMBH;

    //跨行是否实时
    private Boolean KHSFSS;

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getChgno() {
        return chgno;
    }

    public void setChgno(String chgno) {
        this.chgno = chgno;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getZHXZ() {
        return ZHXZ;
    }

    public void setZHXZ(String ZHXZ) {
        this.ZHXZ = ZHXZ;
    }

    public String getKHBH() {
        return KHBH;
    }

    public void setKHBH(String KHBH) {
        this.KHBH = KHBH;
    }

    public String getPLXMBH() {
        return PLXMBH;
    }

    public void setPLXMBH(String PLXMBH) {
        this.PLXMBH = PLXMBH;
    }

    public String getKMBH() {
        return KMBH;
    }

    public void setKMBH(String KMBH) {
        this.KMBH = KMBH;
    }

    public Boolean getKHSFSS() {
        return KHSFSS;
    }

    public void setKHSFSS(Boolean KHSFSS) {
        this.KHSFSS = KHSFSS;
    }

    @Override
    public String toString() {
        return "CenterAccountInfo{" +
                "bank_name='" + bank_name + '\'' +
                ", chgno='" + chgno + '\'' +
                ", node='" + node + '\'' +
                ", code='" + code + '\'' +
                ", YHZHHM='" + YHZHHM + '\'' +
                ", YHZHMC='" + YHZHMC + '\'' +
                ", ZHXZ='" + ZHXZ + '\'' +
                ", KHBH='" + KHBH + '\'' +
                ", PLXMBH='" + PLXMBH + '\'' +
                ", KMBH='" + KMBH + '\'' +
                ", KHSFSS=" + KHSFSS +
                '}';
    }
}

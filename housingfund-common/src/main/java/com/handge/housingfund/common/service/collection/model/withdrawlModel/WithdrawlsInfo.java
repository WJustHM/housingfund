package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import com.handge.housingfund.common.annotation.Annotation;

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
@XmlRootElement(name = "WithdrawlsInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class WithdrawlsInfo implements Serializable {
    /**
     * @param tqyy 提取原因
     * @param tqfs 提取方式
     * @param ywmxlx 业务明细类型(11 部分提取 12销户提取)
     * @param blr 办理人
     * @param dlrxm 代理人姓名
     * @param dlrzjlx 代理人证件类型
     * @param dlrzjhm 代理人证件号码
     * @param fse 发生额
     * @param fslxe 发生利息额
     * @param grckzhhm 个人存款账户号码
     * @param HuMing 户名
     * @param grckzhkhyhmc 个人存款账户开户银行名称
     * @param jzrq 记账日期
     * @param yhke 月还款额
     * @param xctqrq 下次提取日期
     * @param jqe 结清额
     * @param hkfs 还款方式
     */
    @XmlElement(name = "TQYY")
    private String tqyy;
    @XmlElement(name = "TQFS")
    private String tqfs;
    @XmlElement(name = "YWMXLX")
    private String ywmxlx;
    @XmlElement(name = "XHYY")
    private String xhyy;
    @XmlElement(name = "BLR")
    private String blr;
    @XmlElement(name = "DLRXM")
    private String dlrxm;
    @XmlElement(name = "DLRZJLX")
    private String dlrzjlx;
    @XmlElement(name = "DLRZJHM")
    private String dlrzjhm;
    @XmlElement(name = "FSE")
    @Annotation.Money(name = "发生额")
    private String fse;
    @XmlElement(name = "FSLXE")
    @Annotation.Money(name = "发生利息额")
    private String fslxe;
    @XmlElement(name = "GRCKZHHM")
    private String grckzhhm;
    @XmlElement(name = "HuMing")
    private String HuMing;
    @XmlElement(name = "GRCKZHKHYHMC")
    private String grckzhkhyhmc;
    @XmlElement(name = "JZRQ")
    private String jzrq;
    @XmlElement(name = "YHKE")
    @Annotation.Money(name = "月还款额")
    private String yhke;
    @XmlElement(name = "XCTQRQ")
    private String xctqrq;
    @XmlElement(name = "JQE")
    @Annotation.Money(name = "结清额")
    private String jqe;
    @XmlElement(name = "HKFS")
    private String hkfs;

    @XmlElement(name = "DKZJHM")
    private String dkzjhm;
    @XmlElement(name = "QTTQBZ")
    private String qttqbz;

    public String getQttqbz() {
        return qttqbz;
    }

    public void setQttqbz(String qttqbz) {
        this.qttqbz = qttqbz;
    }

    public String getXhyy() {
        return xhyy;
    }

    public String getDkzjhm() {
        return dkzjhm;
    }

    public void setDkzjhm(String dkzjhm) {
        this.dkzjhm = dkzjhm;
    }

    public String getJqe() {
        return jqe;
    }

    public void setJqe(String jqe) {
        this.jqe = jqe;
    }

    public String getHkfs() {
        return hkfs;
    }

    public void setHkfs(String hkfs) {
        this.hkfs = hkfs;
    }
    public String getYhke() {
        return yhke;
    }

    public void setYhke(String yhke) {
        this.yhke = yhke;
    }

    public String getXctqrq() {
        return xctqrq;
    }

    public void setXctqrq(String xctqrq) {
        this.xctqrq = xctqrq;
    }

    public String getTqyy() {
        return tqyy;
    }

    public void setTqyy(String tqyy) {
        this.tqyy = tqyy;
    }

    public String getTqfs() {
        return tqfs;
    }

    public void setTqfs(String tqfs) {
        this.tqfs = tqfs;
    }

    public String getYwmxlx() {
        return ywmxlx;
    }

    public void setYwmxlx(String ywmxlx) {
        this.ywmxlx = ywmxlx;
    }

    public void setXhyy(String xhyy) {
        this.xhyy = xhyy;
    }

    public String getBlr() {
        return blr;
    }

    public void setBlr(String blr) {
        this.blr = blr;
    }

    public String getDlrxm() {
        return dlrxm;
    }

    public void setDlrxm(String dlrxm) {
        this.dlrxm = dlrxm;
    }

    public String getDlrzjlx() {
        return dlrzjlx;
    }

    public void setDlrzjlx(String dlrzjlx) {
        this.dlrzjlx = dlrzjlx;
    }

    public String getDlrzjhm() {
        return dlrzjhm;
    }

    public void setDlrzjhm(String dlrzjhm) {
        this.dlrzjhm = dlrzjhm;
    }

    public String getFse() {
        return fse;
    }

    public void setFse(String fse) {
        this.fse = fse;
    }

    public String getFslxe() {
        return fslxe;
    }

    public void setFslxe(String fslxe) {
        this.fslxe = fslxe;
    }

    public String getGrckzhhm() {
        return grckzhhm;
    }

    public void setGrckzhhm(String grckzhhm) {
        this.grckzhhm = grckzhhm;
    }

    public String getHuMing() {
        return HuMing;
    }

    public void setHuMing(String huMing) {
        HuMing = huMing;
    }

    public String getGrckzhkhyhmc() {
        return grckzhkhyhmc;
    }

    public void setGrckzhkhyhmc(String grckzhkhyhmc) {
        this.grckzhkhyhmc = grckzhkhyhmc;
    }

    public String getJzrq() {
        return jzrq;
    }

    public void setJzrq(String jzrq) {
        this.jzrq = jzrq;
    }

    @Override
    public String toString() {
        return "WithdrawlsInfo{" +
                "tqyy='" + tqyy + '\'' +
                ", tqfs='" + tqfs + '\'' +
                ", ywmxlx='" + ywmxlx + '\'' +
                ", xhyy='" + xhyy + '\'' +
                ", blr='" + blr + '\'' +
                ", dlrxm='" + dlrxm + '\'' +
                ", dlrzjlx='" + dlrzjlx + '\'' +
                ", dlrzjhm='" + dlrzjhm + '\'' +
                ", fse=" + fse +
                ", fslxe=" + fslxe +
                ", grckzhhm='" + grckzhhm + '\'' +
                ", HuMing='" + HuMing + '\'' +
                ", grckzhkhyhmc='" + grckzhkhyhmc + '\'' +
                ", jzrq='" + jzrq + '\'' +
                ", yhke=" + yhke +
                ", xctqrq='" + xctqrq + '\'' +
                ", jqe=" + jqe +
                ", hkfs='" + hkfs + '\'' +
                '}';
    }
}

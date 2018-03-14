package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by tanyi on 2017/9/22.
 */

@Entity
@Table(name = "c_bank_contract")
@org.hibernate.annotations.Table(appliesTo = "c_bank_contract", comment = "公积金中心签约银行表")
public class CBankContract extends Common implements java.io.Serializable {

    private static final long serialVersionUID = 1258121048539693551L;
    @Column(name = "YHMC", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL COMMENT '银行名称'")
    private String yhmc;
    @Column(name = "YHDM", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '银行代码'")
    private String yhdm;
    @Column(name = "chgno", columnDefinition = "VARCHAR(12) DEFAULT NULL COMMENT '联行号'")
    private String chgno;
    @Column(name = "node", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '银行节点号'")
    private String node;
    @Column(name = "KHBH", nullable = false, columnDefinition = "VARCHAR(32) NOT NULL COMMENT '客户编号'")
    private String khbh;
    @Column(name = "PLXMBH", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '批量项目编号编号'")
    private String plxmbh;
    @Column(name = "XDCKJE", nullable = false, columnDefinition = "NUMERIC(18,2) NOT NULL COMMENT '协定存款金额'")
    private BigDecimal xdckje = BigDecimal.ZERO;
    @Column(name = "WDDZ", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '网点地址'")
    private String wddz;
    @Column(name = "LXR", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '联系人'")
    private String lxr;
    @Column(name = "LXDH", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '联系电话'")
    private String lxdh;
    @Column(name = "KHSFSS", columnDefinition = "BIT(1) DEFAULT NULL COMMENT '跨行是否实时'")
    private Boolean khsfss;
    @Column(name = "BeiZhu", columnDefinition = "TEXT DEFAULT NULL COMMENT '备注'")
    private String beizhu;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cBankContract")
    private List<StSettlementSpecialBankAccount> stSettlementSpecialBankAccounts;

    public CBankContract() {
    }

    public CBankContract(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted, String chgno,
                         String yhmc, String yhdm, String node, String khbh, String plxmbh, BigDecimal xdckje, String wddz,
                         String lxr, String lxdh, String beizhu, Boolean khsfss, List<StSettlementSpecialBankAccount> stSettlementSpecialBankAccounts) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.chgno = chgno;
        this.yhmc = yhmc;
        this.yhdm = yhdm;
        this.node = node;
        this.khbh = khbh;
        this.plxmbh = plxmbh;
        this.xdckje = xdckje;
        this.wddz = wddz;
        this.lxr = lxr;
        this.lxdh = lxdh;
        this.beizhu = beizhu;
        this.khsfss = khsfss;
        this.stSettlementSpecialBankAccounts = stSettlementSpecialBankAccounts;
    }

    public Boolean getKhsfss() {
        return khsfss;
    }

    public void setKhsfss(Boolean khsfss) {
        this.khsfss = khsfss;
    }

    public String getChgno() {
        return chgno;
    }

    public void setChgno(String chgno) {
        this.chgno = chgno;
    }

    public List<StSettlementSpecialBankAccount> getStSettlementSpecialBankAccounts() {
        return stSettlementSpecialBankAccounts;
    }

    public void setStSettlementSpecialBankAccounts(List<StSettlementSpecialBankAccount> stSettlementSpecialBankAccounts) {
        this.updated_at = new Date();
        this.stSettlementSpecialBankAccounts = stSettlementSpecialBankAccounts;
    }

    public String getYhmc() {
        return yhmc;
    }

    public void setYhmc(String yhmc) {
        this.updated_at = new Date();
        this.yhmc = yhmc;
    }

    public String getYhdm() {
        return yhdm;
    }

    public void setYhdm(String yhdm) {
        this.updated_at = new Date();
        this.yhdm = yhdm;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.updated_at = new Date();
        this.node = node;
    }

    public String getKhbh() {
        return khbh;
    }

    public void setKhbh(String khbh) {
        this.updated_at = new Date();
        this.khbh = khbh;
    }

    public String getPlxmbh() {
        return plxmbh;
    }

    public void setPlxmbh(String plxmbh) {
        this.updated_at = new Date();
        this.plxmbh = plxmbh;
    }

    public BigDecimal getXdckje() {
        return xdckje;
    }

    public void setXdckje(BigDecimal xdckje) {
        this.updated_at = new Date();
        this.xdckje = xdckje;
    }

    public String getWddz() {
        return wddz;
    }

    public void setWddz(String wddz) {
        this.updated_at = new Date();
        this.wddz = wddz;
    }

    public String getLxr() {
        return lxr;
    }

    public void setLxr(String lxr) {
        this.updated_at = new Date();
        this.lxr = lxr;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.updated_at = new Date();
        this.lxdh = lxdh;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.updated_at = new Date();
        this.beizhu = beizhu;
    }
}

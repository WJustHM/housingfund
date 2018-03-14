package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by tanyi on 2017/9/30.
 */
@Entity
@Table(name = "c_finance_temporary_record")
@org.hibernate.annotations.Table(appliesTo = "c_finance_temporary_record", comment = "暂收记录表")
public class CFinanceTemporaryRecord extends Common implements Serializable {

    private static final long serialVersionUID = 7267170401761601358L;

    @Column(name = "SKZH", columnDefinition = "VARCHAR(30) NOT NULL COMMENT '收款账号'")
    private String skzh;

    @Column(name = "SKHM", columnDefinition = "VARCHAR(60) NOT NULL COMMENT '收款户名'")
    private String skhm;

    @Column(name = "FKZH", columnDefinition = "VARCHAR(30) NOT NULL COMMENT '付款账号'")
    private String fkzh;

    @Column(name = "FKHM", columnDefinition = "VARCHAR(60) NOT NULL COMMENT '付款户名'")
    private String fkhm;

    @Column(name = "HRJE", columnDefinition = "NUMERIC(18,2) NOT NULL COMMENT '汇入金额'")
    private BigDecimal hrje = BigDecimal.ZERO;

    @Column(name = "HRSJ", columnDefinition = "DATETIME NOT NULL COMMENT '汇入时间,格式:YYYYMMDD'")
    private Date hrsj;

    @Column(name = "JZPZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '记账凭证号'")
    private String jzpzh;

    @Column(name = "YJZPZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '源记账凭证号'")
    private String yjzpzh;

    @Column(name = "ZhaiYao", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '摘要'")
    private String zhaiyao;

    @Column(name = "remark", columnDefinition = "TEXT DEFAULT NULL COMMENT '备注'")
    private String remark;

    @Column(name = "state", nullable = false, columnDefinition = "BIT(1) NOT NULL DEFAULT 0 COMMENT '状态（1：已分配，0：未分配）'")
    private Boolean state;

    public CFinanceTemporaryRecord() {
    }

    public CFinanceTemporaryRecord(String skzh, String skhm, String fkzh, String fkhm, BigDecimal hrje, Date hrsj, String jzpzh, String yjzpzh, String zhaiyao, String remark, Boolean state) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.skzh = skzh;
        this.skhm = skhm;
        this.fkzh = fkzh;
        this.fkhm = fkhm;
        this.hrje = hrje;
        this.hrsj = hrsj;
        this.jzpzh = jzpzh;
        this.yjzpzh = yjzpzh;
        this.zhaiyao = zhaiyao;
        this.remark = remark;
        this.state = state;
    }

    public String getYjzpzh() {
        return yjzpzh;
    }

    public void setYjzpzh(String yjzpzh) {
        this.yjzpzh = yjzpzh;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.updated_at = new Date();
        this.remark = remark;
    }

    public String getSkzh() {
        return skzh;
    }

    public void setSkzh(String skzh) {
        this.updated_at = new Date();
        this.skzh = skzh;
    }

    public String getSkhm() {
        return skhm;
    }

    public void setSkhm(String skhm) {
        this.updated_at = new Date();
        this.skhm = skhm;
    }

    public String getFkzh() {
        return fkzh;
    }

    public void setFkzh(String fkzh) {
        this.updated_at = new Date();
        this.fkzh = fkzh;
    }

    public String getFkhm() {
        return fkhm;
    }

    public void setFkhm(String fkhm) {
        this.updated_at = new Date();
        this.fkhm = fkhm;
    }

    public BigDecimal getHrje() {
        return hrje;
    }

    public void setHrje(BigDecimal hrje) {
        this.updated_at = new Date();
        this.hrje = hrje;
    }

    public Date getHrsj() {
        return hrsj;
    }

    public void setHrsj(Date hrsj) {
        this.updated_at = new Date();
        this.hrsj = hrsj;
    }

    public String getJzpzh() {
        return jzpzh;
    }

    public void setJzpzh(String jzpzh) {
        this.updated_at = new Date();
        this.jzpzh = jzpzh;
    }

    public String getZhaiyao() {
        return zhaiyao;
    }

    public void setZhaiyao(String zhaiyao) {
        this.updated_at = new Date();
        this.zhaiyao = zhaiyao;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.updated_at = new Date();
        this.state = state;
    }
}

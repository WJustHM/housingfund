package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

/**
 * Created by 凡 on 2017/10/9.
 */
@Entity
@Table(name = "c_collection_unit_deposit_inventory_batch_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_unit_deposit_inventory_batch_vice", comment = "单位缴存清册")
public class CCollectionUnitDepositInventoryBatchVice extends Common implements Serializable {

    @Column(name = "YWPCH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务批次号'")
    private String ywpch;
    @Column(name = "DWZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '单位账号'")
    private String dwzh;
    @Column(name = "JZNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '缴至年月'")
    private String jzny;
    @Column(name = "QCNYQ", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '清册年月起'")
    private String qcnyq;
    @Column(name = "QCNYZ", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '清册年月止'")
    private String qcnyz;
    @Column(name = "QCZFSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '清册总发生额'")
    private BigDecimal qczfse = BigDecimal.ZERO;
    @Column(name = "WFTYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '未分摊余额'")
    private BigDecimal wftye = BigDecimal.ZERO;
    @Column(name = "JKFS", columnDefinition = "char(2) DEFAULT NULL COMMENT '缴款方式'")
    private String jkfs;
    @Column(name = "DWZHYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '单位账户余额'")
    private BigDecimal dwzhye = BigDecimal.ZERO;

    @JoinColumn(name = "qcpc_id", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '清册详情'")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CCollectionUnitDepositInventoryVice> qclb;

    public String getYwpch() {
        return ywpch;
    }

    public void setYwpch(String ywpch) {
        this.ywpch = ywpch;
    }

    public String getDwzh() {
        return dwzh;
    }

    public void setDwzh(String dwzh) {
        this.dwzh = dwzh;
    }

    public String getJzny() {
        return jzny;
    }

    public void setJzny(String jzny) {
        this.jzny = jzny;
    }

    public String getQcnyq() {
        return qcnyq;
    }

    public void setQcnyq(String qcnyq) {
        this.qcnyq = qcnyq;
    }

    public String getQcnyz() {
        return qcnyz;
    }

    public void setQcnyz(String qcnyz) {
        this.qcnyz = qcnyz;
    }

    public BigDecimal getQczfse() {
        return qczfse;
    }

    public void setQczfse(BigDecimal qczfse) {
        this.qczfse = qczfse;
    }

    public BigDecimal getWftye() {
        return wftye;
    }

    public void setWftye(BigDecimal wftye) {
        this.wftye = wftye;
    }

    public String getJkfs() {
        return jkfs;
    }

    public void setJkfs(String jkfs) {
        this.jkfs = jkfs;
    }

    public Set<CCollectionUnitDepositInventoryVice> getQclb() {
        return qclb;
    }

    public void setQclb(Set<CCollectionUnitDepositInventoryVice> qclb) {
        this.qclb = qclb;
    }

    public BigDecimal getDwzhye() {
        return dwzhye;
    }

    public void setDwzhye(BigDecimal dwzhye) {
        this.dwzhye = dwzhye;
    }
}

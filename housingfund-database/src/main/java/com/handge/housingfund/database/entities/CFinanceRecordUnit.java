package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by tanyi on 2017/12/19.
 */
@Entity
@Table(name = "c_finance_record_unit")
@org.hibernate.annotations.Table(appliesTo = "c_finance_record_unit", comment = "单位未分摊余额记录表")
public class CFinanceRecordUnit extends Common implements Serializable {

    private static final long serialVersionUID = -397344448490087048L;

    @Column(name = "DWZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '单位账号'")
    private String dwzh;

    @Column(name = "FSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '发生额'")
    private BigDecimal fse = BigDecimal.ZERO;

    @Column(name = "WFTYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '未分摊余额'")
    private BigDecimal wftye = BigDecimal.ZERO;

    @Column(name = "remark", columnDefinition = "TEXT DEFAULT NULL COMMENT '备注'")
    private String remark;

    @Column(name = "summary", columnDefinition = "TEXT NOT NULL COMMENT '摘要'")
    private String summary;

    @Column(name = "JZPZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '记账凭证号'")
    private String jzpzh;

    @Column(name = "ZJLY", columnDefinition = "VARCHAR(20) DEFAULT '暂收' COMMENT '资金来源（见 WFTLY）'")
    private String zjly;


    public CFinanceRecordUnit() {
    }

    public CFinanceRecordUnit(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
                              String dwzh, BigDecimal fse, BigDecimal wftye, String remark, String summary, String jzpzh, String zjly) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.dwzh = dwzh;
        this.fse = fse;
        this.wftye = wftye;
        this.remark = remark;
        this.summary = summary;
        this.jzpzh = jzpzh;
        this.zjly = zjly;
    }

    public String getDwzh() {
        return dwzh;
    }

    public void setDwzh(String dwzh) {
        this.updated_at = new Date();
        this.dwzh = dwzh;
    }

    public BigDecimal getFse() {
        return fse;
    }

    public void setFse(BigDecimal fse) {
        this.updated_at = new Date();
        this.fse = fse;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.updated_at = new Date();
        this.remark = remark;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.updated_at = new Date();
        this.summary = summary;
    }

    public String getJzpzh() {
        return jzpzh;
    }

    public void setJzpzh(String jzpzh) {
        this.updated_at = new Date();
        this.jzpzh = jzpzh;
    }

    public String getZjly() {
        return zjly;
    }

    public void setZjly(String zjly) {
        this.updated_at = new Date();
        this.zjly = zjly;
    }

    public BigDecimal getWftye() {
        return wftye;
    }

    public void setWftye(BigDecimal wftye) {
        this.wftye = wftye;
    }
}

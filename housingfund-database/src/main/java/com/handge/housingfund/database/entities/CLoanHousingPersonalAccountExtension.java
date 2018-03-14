package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Funnyboy on 2017/9/29.
 */
@Entity
@Table(name = "c_loan_housing_personal_account_extension")
@org.hibernate.annotations.Table(appliesTo = "c_loan_housing_personal_account_extension", comment = "个人贷款账号拓展")
public class CLoanHousingPersonalAccountExtension  extends Common implements java.io.Serializable{

    private static final long serialVersionUID = -8075488323619111402L;

    @Column(name = "DKGBJHYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '贷款改变计划余额'")
    private BigDecimal dkgbjhye = BigDecimal.ZERO;
    @Column(name = "DKGBJHQS", columnDefinition = "NUMERIC(4,0) DEFAULT NULL COMMENT '贷款改变计划期数'")
    private BigDecimal dkgbjhqs = BigDecimal.ZERO;
    @Column(name = "DKXFFRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '贷款新发放日期'")
    private Date dkxffrq;
    @Column(name = "DQQC", columnDefinition = "NUMERIC(4,0) DEFAULT NULL COMMENT '当期期次'")
    private BigDecimal dqqc = BigDecimal.ZERO;
    @Column(name = "TQHKSJ", columnDefinition = "VARCHAR(8) DEFAULT NULL COMMENT '提前还款时间'")
    private String tqhksj;
    @Column(name = "DKYEZCBJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '贷款余额正常本金'")
    private BigDecimal dkyezcbj = BigDecimal.ZERO;


    public CLoanHousingPersonalAccountExtension() {
    }

    public CLoanHousingPersonalAccountExtension(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
                                                BigDecimal dkgbjhye, BigDecimal dkgbjhqs, Date dkxffrq, BigDecimal dqqc, String tqhksj,BigDecimal dkyezcbj) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.dkgbjhye = dkgbjhye;
        this.dkgbjhqs = dkgbjhqs;
        this.dkxffrq = dkxffrq;
        this.dqqc = dqqc;
        this.tqhksj = tqhksj;
        this.dkyezcbj=dkyezcbj;
    }

    public String getTqhksj() {
        return tqhksj;
    }

    public void setTqhksj(String tqhksj) {
        this.tqhksj = tqhksj;
    }

    public BigDecimal getDkgbjhye() {
        return dkgbjhye;
    }

    public void setDkgbjhye(BigDecimal dkgbjhye) {
        this.dkgbjhye = dkgbjhye;
    }

    public BigDecimal getDkgbjhqs() {
        return dkgbjhqs;
    }

    public void setDkgbjhqs(BigDecimal dkgbjhqs) {
        this.dkgbjhqs = dkgbjhqs;
    }

    public BigDecimal getDqqc() {
        return dqqc;
    }

    public void setDqqc(BigDecimal dqqc) {
        this.dqqc = dqqc;
    }

    public Date getDkxffrq() {
        return dkxffrq;
    }

    public void setDkxffrq(Date dkxffrq) {
        this.dkxffrq = dkxffrq;
    }

    public BigDecimal getDkyezcbj() {
        return dkyezcbj;
    }

    public void setDkyezcbj(BigDecimal dkyezcbj) {
        this.dkyezcbj = dkyezcbj;
    }
}

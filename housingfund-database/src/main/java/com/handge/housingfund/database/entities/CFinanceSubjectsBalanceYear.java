package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by tanyi on 2017/9/12.
 */
@Entity
@Table(name = "c_finance_subjects_balance_year")
@org.hibernate.annotations.Table(appliesTo = "c_finance_subjects_balance_year", comment = "科目余额年度表")
public class CFinanceSubjectsBalanceYear extends Common implements Serializable {

    private static final long serialVersionUID = -478359026263417276L;

    @Column(name = "KMBH", columnDefinition = "VARCHAR(19) DEFAULT NULL COMMENT '科目编号 附A.25'")
    private String kmbh;

    @Column(name = "KMMC", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '科目名称'")
    private String kmmc;

    @Column(name = "SNYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '上年余额'")
    private BigDecimal snye = BigDecimal.ZERO;

    @Column(name = "BNZJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '本年增加'")
    private BigDecimal bnzj = BigDecimal.ZERO;

    @Column(name = "BNJS", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '本年减少'")
    private BigDecimal bnjs = BigDecimal.ZERO;

    @Column(name = "BNYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '本年余额'")
    private BigDecimal bnye = BigDecimal.ZERO;

    @Column(name = "KMJB", columnDefinition = "NUMERIC(1,0) DEFAULT NULL COMMENT '科目级别'")
    private BigDecimal kmjb = BigDecimal.ZERO;

    @Column(name = "KMYEFX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '科目余额方向 附A.27'")
    private String kmyefx;

    @Column(name = "NianFen", columnDefinition = "VARCHAR(4) DEFAULT NULL COMMENT '年份2017'")
    private String nianfen;

    @Column(name = "Verify", columnDefinition = "BIT(1) DEFAULT FALSE COMMENT '本账是否核对'")
    private boolean verify;


    public CFinanceSubjectsBalanceYear(String kmbh, String kmmc, BigDecimal snye, BigDecimal bnzj, BigDecimal bnjs,
                                       BigDecimal bnye, BigDecimal kmjb, String kmyefx, String nianfen ) {

        this.kmmc = kmmc;
        this.kmbh = kmbh;
        this.snye = snye;
        this.bnzj = bnzj;
        this.bnjs = bnjs;
        this.bnye = bnye;
        this.kmjb = kmjb;
        this.kmyefx = kmyefx;
        this.nianfen = nianfen;
    }



    public String getNianfen() {
        return nianfen;
    }

    public void setNianfen(String nianfen) {
        this.updated_at = new Date();
        this.nianfen = nianfen;
    }

    public String getKmbh() {
        return kmbh;
    }

    public void setKmbh(String kmbh) {
        this.updated_at = new Date();
        this.kmbh = kmbh;
    }

    public String getKmmc() {
        return kmmc;
    }

    public void setKmmc(String kmmc) {
        this.updated_at = new Date();
        this.kmmc = kmmc;
    }

    public BigDecimal getSnye() {
        return snye;
    }

    public void setSnye(BigDecimal snye) {
        this.updated_at = new Date();
        this.snye = snye;
    }

    public BigDecimal getBnzj() {
        return bnzj;
    }

    public void setBnzj(BigDecimal bnzj) {
        this.updated_at = new Date();
        this.bnzj = bnzj;
    }

    public BigDecimal getBnjs() {
        return bnjs;
    }

    public void setBnjs(BigDecimal bnjs) {
        this.updated_at = new Date();
        this.bnjs = bnjs;
    }

    public BigDecimal getBnye() {
        return bnye;
    }

    public void setBnye(BigDecimal bnye) {
        this.updated_at = new Date();
        this.bnye = bnye;
    }

    public BigDecimal getKmjb() {
        return kmjb;
    }

    public void setKmjb(BigDecimal kmjb) {
        this.updated_at = new Date();
        this.kmjb = kmjb;
    }

    public String getKmyefx() {
        return kmyefx;
    }

    public void setKmyefx(String kmyefx) {
        this.updated_at = new Date();
        this.kmyefx = kmyefx;
    }

    public CFinanceSubjectsBalanceYear() {
        super();
    }

    public CFinanceSubjectsBalanceYear(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
                                       String kmbh, String kmmc, BigDecimal snye, BigDecimal bnzj, BigDecimal bnjs,
                                       BigDecimal bnye, BigDecimal kmjb, String kmyefx, String nianfen ,boolean verify) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.kmbh = kmbh;
        this.kmmc = kmmc;
        this.snye = snye;
        this.bnzj = bnzj;
        this.bnjs = bnjs;
        this.bnye = bnye;
        this.kmjb = kmjb;
        this.kmyefx = kmyefx;
        this.nianfen = nianfen;
        this.verify = verify;
    }

    public boolean isVerify() {
        return verify;
    }

    public void setVerify(boolean verify) {
        this.verify = verify;
    }
}

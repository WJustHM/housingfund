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
@Table(name = "c_finance_subjects_balance_quarter")
@org.hibernate.annotations.Table(appliesTo = "c_finance_subjects_balance_quarter", comment = "科目余额季度表")
public class CFinanceSubjectsBalanceQuarter extends Common implements Serializable {

    private static final long serialVersionUID = 4057074842821851805L;

    @Column(name = "KMBH", columnDefinition = "VARCHAR(19) DEFAULT NULL COMMENT '科目编号 附A.25'")
    private String kmbh;

    @Column(name = "KMMC", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '科目名称'")
    private String kmmc;

    @Column(name = "SJYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '上季余额'")
    private BigDecimal sjye = BigDecimal.ZERO;

    @Column(name = "BJZJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '本季增加'")
    private BigDecimal bjzj = BigDecimal.ZERO;

    @Column(name = "BJJS", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '本季减少'")
    private BigDecimal bjjs = BigDecimal.ZERO;

    @Column(name = "BJYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '本季余额'")
    private BigDecimal bjye = BigDecimal.ZERO;

    @Column(name = "KMJB", columnDefinition = "NUMERIC(1,0) DEFAULT NULL COMMENT '科目级别'")
    private BigDecimal kmjb = BigDecimal.ZERO;

    @Column(name = "KMYEFX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '科目余额方向 附A.27'")
    private String kmyefx;

    @Column(name = "NianFen", columnDefinition = "VARCHAR(4) DEFAULT NULL COMMENT '年份2017'")
    private String nianfen;

    @Column(name = "JiDu", columnDefinition = "VARCHAR(1) DEFAULT NULL COMMENT '季度1，2，3，4'")
    private String jidu;

    @Column(name = "Verify", columnDefinition = "BIT(1) DEFAULT FALSE COMMENT '本账是否核对'")
    private boolean verify;


    public CFinanceSubjectsBalanceQuarter() {
        super();
    }

    public CFinanceSubjectsBalanceQuarter( String kmbh, String kmmc, BigDecimal sjye, BigDecimal bjzj, BigDecimal bjjs,
                                           BigDecimal bjye, BigDecimal kmjb, String kmyefx, String nianfen, String jidu){
        this.kmbh = kmbh;
        this.kmmc = kmmc;
        this.sjye = sjye;
        this.bjzj = bjzj;
        this.bjjs = bjjs;
        this.bjye = bjye;
        this.kmjb = kmjb;
        this.kmyefx = kmyefx;
        this.nianfen = nianfen;
        this.jidu = jidu;
    }

    public CFinanceSubjectsBalanceQuarter(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
                                          String kmbh, String kmmc, BigDecimal sjye, BigDecimal bjzj, BigDecimal bjjs,
                                          BigDecimal bjye, BigDecimal kmjb, String kmyefx, String nianfen, String jidu , boolean verify) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.kmbh = kmbh;
        this.kmmc = kmmc;
        this.sjye = sjye;
        this.bjzj = bjzj;
        this.bjjs = bjjs;
        this.bjye = bjye;
        this.kmjb = kmjb;
        this.kmyefx = kmyefx;
        this.nianfen = nianfen;
        this.jidu = jidu;
        this.verify = verify;
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

    public BigDecimal getSjye() {
        return sjye;
    }

    public void setSjye(BigDecimal sjye) {
        this.updated_at = new Date();
        this.sjye = sjye;
    }

    public BigDecimal getBjzj() {
        return bjzj;
    }

    public void setBjzj(BigDecimal bjzj) {
        this.updated_at = new Date();
        this.bjzj = bjzj;
    }

    public BigDecimal getBjjs() {
        return bjjs;
    }

    public void setBjjs(BigDecimal bjjs) {
        this.updated_at = new Date();
        this.bjjs = bjjs;
    }

    public BigDecimal getBjye() {
        return bjye;
    }

    public void setBjye(BigDecimal bjye) {
        this.updated_at = new Date();
        this.bjye = bjye;
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

    public String getNianfen() {
        return nianfen;
    }

    public void setNianfen(String nianfen) {
        this.updated_at = new Date();
        this.nianfen = nianfen;
    }

    public String getJidu() {
        return jidu;
    }

    public void setJidu(String jidu) {
        this.updated_at = new Date();
        this.jidu = jidu;
    }

    public boolean isVerify() {
        return verify;
    }

    public void setVerify(boolean verify) {
        this.verify = verify;
    }
}

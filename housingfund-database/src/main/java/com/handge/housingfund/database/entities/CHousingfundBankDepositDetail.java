package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by tanyi on 2017/9/22.
 */
@Entity
@Table(name = "c_housingfund_bank_deposit_detail")
@org.hibernate.annotations.Table(appliesTo = "c_housingfund_bank_deposit_detail", comment = "住房公积金银行存款情况表")
public class CHousingfundBankDepositDetail extends Common implements Serializable {

    private static final long serialVersionUID = 1771560287291025091L;
    @Column(name = "CKJRJG", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '存款金融机构'")
    private String ckjrjg;
    @Column(name = "ZHXZ", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '专户性质'")
    private String zhxz;
    @Column(name = "CKLX", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '存款类型'")
    private String cklx;
    @Column(name = "ZJYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '资金余额'")
    private BigDecimal zjye = BigDecimal.ZERO;
    @Column(name = "LiLv", columnDefinition = "NUMERIC(8,7) DEFAULT NULL COMMENT '利率'")
    private BigDecimal lilv = BigDecimal.ZERO;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cFinanceAccountPeriod", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '会计期间'")
    private CFinanceAccountPeriod cFinanceAccountPeriod;

    public CHousingfundBankDepositDetail() {
    }

    public CHousingfundBankDepositDetail(String ckjrjg, String zhxz, String cklx, BigDecimal zjye, BigDecimal lilv, CFinanceAccountPeriod cFinanceAccountPeriod) {
        this.ckjrjg = ckjrjg;
        this.zhxz = zhxz;
        this.cklx = cklx;
        this.zjye = zjye;
        this.lilv = lilv;
        this.cFinanceAccountPeriod = cFinanceAccountPeriod;
    }

    public String getCkjrjg() {
        return ckjrjg;
    }

    public void setCkjrjg(String ckjrjg) {
        this.updated_at = new Date();
        this.ckjrjg = ckjrjg;
    }

    public String getZhxz() {
        return zhxz;
    }

    public void setZhxz(String zhxz) {
        this.updated_at = new Date();
        this.zhxz = zhxz;
    }

    public String getCklx() {
        return cklx;
    }

    public void setCklx(String cklx) {
        this.updated_at = new Date();
        this.cklx = cklx;
    }

    public BigDecimal getZjye() {
        return zjye;
    }

    public void setZjye(BigDecimal zjye) {
        this.updated_at = new Date();
        this.zjye = zjye;
    }

    public BigDecimal getLilv() {
        return lilv;
    }

    public void setLilv(BigDecimal lilv) {
        this.updated_at = new Date();
        this.lilv = lilv;
    }

    public CFinanceAccountPeriod getcFinanceAccountPeriod() {
        return cFinanceAccountPeriod;
    }

    public void setcFinanceAccountPeriod(CFinanceAccountPeriod cFinanceAccountPeriod) {
        this.updated_at = new Date();
        this.cFinanceAccountPeriod = cFinanceAccountPeriod;
    }
}

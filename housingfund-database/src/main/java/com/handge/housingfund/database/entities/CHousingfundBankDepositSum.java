package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by gxy on 17-10-9.
 */
@Entity
@Table(name = "c_housingfund_bank_deposit_sum")
@org.hibernate.annotations.Table(appliesTo = "c_housingfund_bank_deposit_sum", comment = "住房公积金银行存款情况汇总表")
public class CHousingfundBankDepositSum extends Common implements Serializable {
    private static final long serialVersionUID = 7209202068210339707L;

    @Column(name = "ZHXZ", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '专户性质'")
    private String zhxz;
    @Column(name = "CKLX", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '存款类型'")
    private String cklx;
    @Column(name = "ZJYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '资金余额'")
    private BigDecimal zjye  = BigDecimal.ZERO;
    @Column(name = "LiLv", columnDefinition = "NUMERIC(8,7) DEFAULT NULL COMMENT '利率'")
    private BigDecimal lilv  = BigDecimal.ZERO;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cFinanceAccountPeriod", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '会计期间'")
    private CFinanceAccountPeriod cFinanceAccountPeriod;

    public CHousingfundBankDepositSum() {
    }

    public CHousingfundBankDepositSum(String zhxz, String cklx, BigDecimal zjye, BigDecimal lilv) {
        this.zhxz = zhxz;
        this.cklx = cklx;
        this.zjye = zjye;
        this.lilv = lilv;
    }

    public CHousingfundBankDepositSum(String zhxz, String cklx, BigDecimal zjye, BigDecimal lilv, CFinanceAccountPeriod cFinanceAccountPeriod) {
        this.zhxz = zhxz;
        this.cklx = cklx;
        this.zjye = zjye;
        this.lilv = lilv;
        this.cFinanceAccountPeriod = cFinanceAccountPeriod;
    }

    public String getZhxz() {
        return zhxz;
    }

    public CFinanceAccountPeriod getcFinanceAccountPeriod() {
        return cFinanceAccountPeriod;
    }

    public void setcFinanceAccountPeriod(CFinanceAccountPeriod cFinanceAccountPeriod) {
        this.cFinanceAccountPeriod = cFinanceAccountPeriod;
    }

    public void setZhxz(String zhxz) {
        this.zhxz = zhxz;
    }

    public String getCklx() {
        return cklx;
    }

    public void setCklx(String cklx) {
        this.cklx = cklx;
    }

    public BigDecimal getZjye() {
        return zjye;
    }

    public void setZjye(BigDecimal zjye) {
        this.zjye = zjye;
    }

    public BigDecimal getLilv() {
        return lilv;
    }

    public void setLilv(BigDecimal lilv) {
        this.lilv = lilv;
    }
}

package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "c_finance_account_period")
@org.hibernate.annotations.Table(appliesTo = "c_finance_account_period", comment = "会计期间")
public class CFinanceAccountPeriod extends Common implements Serializable {

    private static final long serialVersionUID = 3440697197759519326L;

    @Column(name = "KJND", columnDefinition = "VARCHAR(4) DEFAULT NULL COMMENT '会计年度'")
    private String kjnd;
    @Column(name = "KJQJ", columnDefinition = "VARCHAR(6) NOT NULL COMMENT '会计期间'")
    private String kjqj;
    @Column(name = "QSRQ", columnDefinition = "DATETIME NOT NULL COMMENT '起始日期'")
    private Date qsrq;
    @Column(name = "JieZRQ", columnDefinition = "DATETIME NOT NULL COMMENT '截至日期'")
    private Date jiezrq;
    @Column(name = "SFJS", columnDefinition = "BIT(1) DEFAULT 0 NOT NULL COMMENT '是否结算'")
    private boolean sfjs;
    @Column(name = "JZR", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '结账人'")
    private String jzr;
    @Column(name = "JZRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '结账日期'")
    private Date jzrq;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cFinanceAccountPeriod")
    private List<CFinanceSubjectsBalance> cFinanceSubjectsBalances;//科目余额表

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cFinanceAccountPeriod")
    private List<CHousingfundDepositDetail> cHousingfundDepositDetails;//住房公积金缴存使用情况表

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cFinanceAccountPeriod")
    private List<CHousingfundBankDepositDetail> cHousingfundBankDepositDetails;//住房公积金银行存款情况表

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cFinanceAccountPeriod")
    private List<CHousingfundBankDepositSum> cHousingfundBankDepositSums;//住房公积金银行存款情况汇总表

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cFinanceAccountSets", columnDefinition = "varchar(32) DEFAULT NULL COMMENT '账套'")
    private CFinanceAccountSets cFinanceAccountSets;


    public String getKjnd() {
        return kjnd;
    }

    public void setKjnd(String kjnd) {
        this.updated_at = new Date();
        this.kjnd = kjnd;
    }

    public String getKjqj() {
        return kjqj;
    }

    public void setKjqj(String kjqj) {
        this.updated_at = new Date();
        this.kjqj = kjqj;
    }

    public boolean isSfjs() {
        return sfjs;
    }

    public void setSfjs(boolean sfjs) {
        this.updated_at = new Date();
        this.sfjs = sfjs;
    }

    public String getJzr() {
        return jzr;
    }

    public void setJzr(String jzr) {
        this.updated_at = new Date();
        this.jzr = jzr;
    }

    public Date getJzrq() {
        return jzrq;
    }

    public void setJzrq(Date jzrq) {
        this.updated_at = new Date();
        this.jzrq = jzrq;
    }

    public List<CFinanceSubjectsBalance> getcFinanceSubjectsBalances() {
        return cFinanceSubjectsBalances;
    }

    public void setcFinanceSubjectsBalances(List<CFinanceSubjectsBalance> cFinanceSubjectsBalances) {
        this.updated_at = new Date();
        if (cFinanceSubjectsBalances != null) {
            for (CFinanceSubjectsBalance balance : cFinanceSubjectsBalances) {
                balance.setcFinanceAccountPeriod(this);
            }
        }
        this.cFinanceSubjectsBalances = cFinanceSubjectsBalances;
    }

    public Date getQsrq() {
        return qsrq;
    }

    public void setQsrq(Date qsrq) {
        this.updated_at = new Date();
        this.qsrq = qsrq;
    }

    public Date getJiezrq() {
        return jiezrq;
    }

    public void setJiezrq(Date jiezrq) {
        this.updated_at = new Date();
        this.jiezrq = jiezrq;
    }

    public CFinanceAccountSets getcFinanceAccountSets() {
        return cFinanceAccountSets;
    }

    public void setcFinanceAccountSets(CFinanceAccountSets cFinanceAccountSets) {
        this.updated_at = new Date();
        this.cFinanceAccountSets = cFinanceAccountSets;
    }

    public List<CHousingfundBankDepositDetail> getcHousingfundBankDepositDetails() {
        return cHousingfundBankDepositDetails;
    }

    public void setcHousingfundBankDepositDetails(List<CHousingfundBankDepositDetail> cHousingfundBankDepositDetails) {
        this.updated_at = new Date();
        if (cHousingfundBankDepositDetails != null) {
            for (CHousingfundBankDepositDetail detail : cHousingfundBankDepositDetails) {
                detail.setcFinanceAccountPeriod(this);
            }
        }
        this.cHousingfundBankDepositDetails = cHousingfundBankDepositDetails;
    }

    public List<CHousingfundBankDepositSum> getcHousingfundBankDepositSums() {
        return cHousingfundBankDepositSums;
    }

    public void setcHousingfundBankDepositSums(List<CHousingfundBankDepositSum> cHousingfundBankDepositSums) {
        this.updated_at = new Date();
        if (cHousingfundBankDepositSums != null) {
            for (CHousingfundBankDepositSum sum : cHousingfundBankDepositSums) {
                sum.setcFinanceAccountPeriod(this);
            }
        }
        this.cHousingfundBankDepositSums = cHousingfundBankDepositSums;
    }

    public List<CHousingfundDepositDetail> getcHousingfundDepositDetails() {
        return cHousingfundDepositDetails;
    }

    public void setcHousingfundDepositDetails(List<CHousingfundDepositDetail> cHousingfundDepositDetails) {
        this.updated_at = new Date();
        if (cHousingfundDepositDetails != null) {
            for (CHousingfundDepositDetail detail : cHousingfundDepositDetails) {
                detail.setcFinanceAccountPeriod(this);
            }
        }
        this.cHousingfundDepositDetails = cHousingfundDepositDetails;
    }


    public CFinanceAccountPeriod() {
        super();
    }

    public CFinanceAccountPeriod(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
                                 String kjnd, String kjqj, Date qsrq, Date jiezrq, boolean sfjs, String jzr, Date jzrq,
                                 List<CFinanceSubjectsBalance> cFinanceSubjectsBalances,
                                 List<CHousingfundDepositDetail> cHousingfundDepositDetails, CFinanceAccountSets cFinanceAccountSets,
                                 List<CHousingfundBankDepositDetail> cHousingfundBankDepositDetails) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.kjnd = kjnd;
        this.kjqj = kjqj;
        this.qsrq = qsrq;
        this.jiezrq = jiezrq;
        this.sfjs = sfjs;
        this.jzr = jzr;
        this.jzrq = jzrq;
        this.cFinanceSubjectsBalances = cFinanceSubjectsBalances;
        this.cHousingfundDepositDetails = cHousingfundDepositDetails;
        this.cFinanceAccountSets = cFinanceAccountSets;
        this.cHousingfundBankDepositDetails = cHousingfundBankDepositDetails;
    }
}

package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "st_settlement_special_bank_account")
@org.hibernate.annotations.Table(appliesTo = "st_settlement_special_bank_account", comment = "银行专户信息表 表9.0.3")
public class StSettlementSpecialBankAccount extends Common implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8578327138510774L;
    @Column(name = "KMBH", columnDefinition = "VARCHAR(19) DEFAULT NULL COMMENT '科目编号'")
    private String kmbh;
    @Column(name = "YHZHHM", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '银行专户号码'")
    private String yhzhhm;
    @Column(name = "YHZHMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '银行专户名称'")
    private String yhzhmc;
    @Column(name = "YHDM", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '银行代码'")
    private String yhdm;
    @Column(name = "YHMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '银行名称'")
    private String yhmc;
    @Column(name = "KHRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '开户日期'")
    private Date khrq;
    @Column(name = "ZHXZ", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '专户性质'")
    private String zhxz;
    @Column(name = "XHRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '销户日期'")
    private Date xhrq;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "extension", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '银行专户信息扩展'")
    private CSettlementSpecialBankAccountExtension cSettlementSpecialBankAccountExtension;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cBankContract", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '签约银行'")
    private CBankContract cBankContract;

    public StSettlementSpecialBankAccount() {
        super();
    }

    public StSettlementSpecialBankAccount(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
                                          String kmbh, String yhzhhm, String yhzhmc, String yhdm,
                                          String yhmc, Date khrq, String zhxz, Date xhrq, CSettlementSpecialBankAccountExtension cSettlementSpecialBankAccountExtension,
                                          CBankContract cBankContract) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.kmbh = kmbh;
        this.yhzhhm = yhzhhm;
        this.yhzhmc = yhzhmc;
        this.yhdm = yhdm;
        this.yhmc = yhmc;
        this.khrq = khrq;
        this.zhxz = zhxz;
        this.xhrq = xhrq;
        this.cSettlementSpecialBankAccountExtension = cSettlementSpecialBankAccountExtension;
        this.cBankContract = cBankContract;
    }

    public String getKmbh() {
        return this.kmbh;
    }

    public void setKmbh(String kmbh) {
        this.updated_at = new Date();
        this.kmbh = kmbh;
    }

    public String getYhzhhm() {
        return this.yhzhhm;
    }

    public void setYhzhhm(String yhzhhm) {
        this.updated_at = new Date();
        this.yhzhhm = yhzhhm;
    }

    public String getYhzhmc() {
        return this.yhzhmc;
    }

    public void setYhzhmc(String yhzhmc) {
        this.updated_at = new Date();
        this.yhzhmc = yhzhmc;
    }

    public String getYhdm() {
        return this.yhdm;
    }

    public void setYhdm(String yhdm) {
        this.updated_at = new Date();
        this.yhdm = yhdm;
    }

    public String getYhmc() {
        return this.yhmc;
    }

    public void setYhmc(String yhmc) {
        this.updated_at = new Date();
        this.yhmc = yhmc;
    }

    public Date getKhrq() {
        return this.khrq;
    }

    public void setKhrq(Date khrq) {
        this.updated_at = new Date();
        this.khrq = khrq;
    }

    public String getZhxz() {
        return this.zhxz;
    }

    public void setZhxz(String zhxz) {
        this.updated_at = new Date();
        this.zhxz = zhxz;
    }

    public Date getXhrq() {
        return this.xhrq;
    }

    public void setXhrq(Date xhrq) {
        this.updated_at = new Date();
        this.xhrq = xhrq;
    }

    public CSettlementSpecialBankAccountExtension getcSettlementSpecialBankAccountExtension() {
        return cSettlementSpecialBankAccountExtension;
    }

    public void setcSettlementSpecialBankAccountExtension(CSettlementSpecialBankAccountExtension cSettlementSpecialBankAccountExtension) {
        this.updated_at = new Date();
        this.cSettlementSpecialBankAccountExtension = cSettlementSpecialBankAccountExtension;
    }

    public CBankContract getcBankContract() {
        return cBankContract;
    }

    public void setcBankContract(CBankContract cBankContract) {
        this.updated_at = new Date();
        this.cBankContract = cBankContract;
    }
}

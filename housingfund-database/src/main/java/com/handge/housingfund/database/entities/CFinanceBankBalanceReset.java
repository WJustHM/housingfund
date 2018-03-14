package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by tanyi on 2017/10/25.
 */
@Entity
@Table(name = "c_finance_bank_balance_reset")
@org.hibernate.annotations.Table(appliesTo = "c_finance_bank_balance_reset", comment = "银行余额调节表")
public class CFinanceBankBalanceReset extends Common implements Serializable {

    private static final long serialVersionUID = 6092130962555714163L;

    @Column(name = "TJRQ", columnDefinition = "VARCHAR(8) DEFAULT NULL COMMENT '调节日期'")
    private String tjrq;

    @Column(name = "ZHHM", columnDefinition = "VARCHAR(32) NOT NULL COMMENT '专户号码'")
    private String zhhm;

    @Column(name = "node", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '银行节点号'")
    private String node;

    @Column(name = "KHYHM", columnDefinition = "VARCHAR(60) DEFAULT NULL COMMENT '开户银行名称'")
    private String khyhm;

    @Column(name = "ZXCKYE", columnDefinition = "NUMERIC(18,2) NOT NULL COMMENT '中心存款余额'")
    private BigDecimal zxckye = BigDecimal.ZERO;
    @Column(name = "ZXJIA", columnDefinition = "NUMERIC(18,2) NOT NULL COMMENT '加：银行已收，中心未收款'")
    private BigDecimal zxjia = BigDecimal.ZERO;
    @Column(name = "ZXJ", columnDefinition = "NUMERIC(18,2) NOT NULL COMMENT '减：银行已付，中心未付款'")
    private BigDecimal zxj = BigDecimal.ZERO;
    @Column(name = "ZXYE", columnDefinition = "NUMERIC(18,2) NOT NULL COMMENT '中心调节后的存款余额'")
    private BigDecimal zxye = BigDecimal.ZERO;

    @Column(name = "YHDZYE", columnDefinition = "NUMERIC(18,2) NOT NULL COMMENT '银行对账余额'")
    private BigDecimal yhdzye = BigDecimal.ZERO;
    @Column(name = "YHJIA", columnDefinition = "NUMERIC(18,2) NOT NULL COMMENT '加：中心已收，银行未收款'")
    private BigDecimal yhjia = BigDecimal.ZERO;
    @Column(name = "YHJ", columnDefinition = "NUMERIC(18,2) NOT NULL COMMENT '减：企业已付，银行未付款'")
    private BigDecimal yhj = BigDecimal.ZERO;
    @Column(name = "YHYE", columnDefinition = "NUMERIC(18,2) NOT NULL COMMENT '银行调节后的存款余额'")
    private BigDecimal yhye = BigDecimal.ZERO;

    @Column(name = "ZBR", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '制表人'")
    private String zbr;

    @Column(name = "ZBSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '制表时间'")
    private Date zbsj;

    public CFinanceBankBalanceReset() {
    }

    public CFinanceBankBalanceReset(String id, Date created_at, Date updated_at, Date deleted_at, Boolean deleted,
                                    String tjrq, String zhhm, String node, BigDecimal zxckye, BigDecimal zxjia,
                                    BigDecimal zxj, BigDecimal zxye, BigDecimal yhdzye, BigDecimal yhjia,
                                    BigDecimal yhj, BigDecimal yhye, String zbr, Date zbsj, String khyhm) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.deleted = deleted;
        this.tjrq = tjrq;
        this.zhhm = zhhm;
        this.node = node;
        this.zxckye = zxckye;
        this.zxjia = zxjia;
        this.khyhm = khyhm;
        this.zxj = zxj;
        this.zxye = zxye;
        this.yhdzye = yhdzye;
        this.yhjia = yhjia;
        this.yhj = yhj;
        this.yhye = yhye;
        this.zbr = zbr;
        this.zbsj = zbsj;
    }

    public String getTjrq() {
        return tjrq;
    }

    public void setTjrq(String tjrq) {
        this.updated_at = new Date();
        this.tjrq = tjrq;
    }

    public String getZhhm() {
        return zhhm;
    }

    public void setZhhm(String zhhm) {
        this.updated_at = new Date();
        this.zhhm = zhhm;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.updated_at = new Date();
        this.node = node;
    }

    public BigDecimal getZxckye() {
        return zxckye;
    }

    public void setZxckye(BigDecimal zxckye) {
        this.updated_at = new Date();
        this.zxckye = zxckye;
    }

    public BigDecimal getZxjia() {
        return zxjia;
    }

    public void setZxjia(BigDecimal zxjia) {
        this.updated_at = new Date();
        this.zxjia = zxjia;
    }

    public BigDecimal getZxj() {
        return zxj;
    }

    public void setZxj(BigDecimal zxj) {
        this.updated_at = new Date();
        this.zxj = zxj;
    }

    public BigDecimal getZxye() {
        return zxye;
    }

    public void setZxye(BigDecimal zxye) {
        this.updated_at = new Date();
        this.zxye = zxye;
    }

    public BigDecimal getYhdzye() {
        return yhdzye;
    }

    public void setYhdzye(BigDecimal yhdzye) {
        this.updated_at = new Date();
        this.yhdzye = yhdzye;
    }

    public BigDecimal getYhjia() {
        return yhjia;
    }

    public void setYhjia(BigDecimal yhjia) {
        this.updated_at = new Date();
        this.yhjia = yhjia;
    }

    public BigDecimal getYhj() {
        return yhj;
    }

    public void setYhj(BigDecimal yhj) {
        this.updated_at = new Date();
        this.yhj = yhj;
    }

    public BigDecimal getYhye() {
        return yhye;
    }

    public void setYhye(BigDecimal yhye) {
        this.updated_at = new Date();
        this.yhye = yhye;
    }

    public String getZbr() {
        return zbr;
    }

    public void setZbr(String zbr) {
        this.updated_at = new Date();
        this.zbr = zbr;
    }

    public Date getZbsj() {
        return zbsj;
    }

    public void setZbsj(Date zbsj) {
        this.updated_at = new Date();
        this.zbsj = zbsj;
    }

    public String getKhyhm() {
        return khyhm;
    }

    public void setKhyhm(String khyhm) {
        this.updated_at = new Date();
        this.khyhm = khyhm;
    }
}

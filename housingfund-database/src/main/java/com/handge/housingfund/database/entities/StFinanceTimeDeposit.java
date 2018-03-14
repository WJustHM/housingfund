package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_finance_time_deposit")
@org.hibernate.annotations.Table(appliesTo = "st_finance_time_deposit", comment = "定期存款明细信息 表 8.0.7")
public class StFinanceTimeDeposit extends Common implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7528846780207701518L;
	@Column(name = "DQCKBH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '定期存款编号'")
	private String dqckbh;
	@Column(name = "ZHHM", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '专户号码'")
	private String zhhm;
	@Column(name = "ZHMC", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '账户名称'")
	private String zhmc;
	@Column(name = "KHYHMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '开户银行名称'")
	private String khyhmc;
	@Column(name = "LiLv", columnDefinition = "NUMERIC(8,7) DEFAULT NULL COMMENT '利率'")
	private BigDecimal liLv = BigDecimal.ZERO;
	@Column(name = "BJJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '本金金额'")
	private BigDecimal bjje = BigDecimal.ZERO;
	@Column(name = "CRRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '存入日期'")
	private Date crrq;
	@Column(name = "DQRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '到期日期'")
	private Date dqrq;
	@Column(name = "CKQX", columnDefinition = "NUMERIC(5,0) DEFAULT NULL COMMENT '存款期限'")
	private BigDecimal ckqx = BigDecimal.ZERO;
	@Column(name = "ZQQK", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '支取情况'")
	private String zqqk;
	@Column(name = "QKRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '取款日期'")
	private Date qkrq;
	@Column(name = "LXSR", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '利息收入'")
	private BigDecimal lxsr = BigDecimal.ZERO;

	public StFinanceTimeDeposit() {
		super();

	}

	public StFinanceTimeDeposit(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String dqckbh, String zhhm, String zhmc, String khyhmc, BigDecimal liLv, BigDecimal bjje, Date crrq,
			Date dqrq, BigDecimal ckqx, String zqqk, Date qkrq, BigDecimal lxsr) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.dqckbh = dqckbh;
		this.zhhm = zhhm;
		this.zhmc = zhmc;
		this.khyhmc = khyhmc;
		this.liLv = liLv;
		this.bjje = bjje;
		this.crrq = crrq;
		this.dqrq = dqrq;
		this.ckqx = ckqx;
		this.zqqk = zqqk;
		this.qkrq = qkrq;
		this.lxsr = lxsr;
	}

	public String getDqckbh() {
		return this.dqckbh;
	}

	public void setDqckbh(String dqckbh) {
		this.updated_at = new Date();
		this.dqckbh = dqckbh;
	}

	public String getZhhm() {
		return this.zhhm;
	}

	public void setZhhm(String zhhm) {
		this.updated_at = new Date();
		this.zhhm = zhhm;
	}

	public String getZhmc() {
		return this.zhmc;
	}

	public void setZhmc(String zhmc) {
		this.updated_at = new Date();
		this.zhmc = zhmc;
	}

	public String getKhyhmc() {
		return this.khyhmc;
	}

	public void setKhyhmc(String khyhmc) {
		this.updated_at = new Date();
		this.khyhmc = khyhmc;
	}

	public BigDecimal getLiLv() {
		return this.liLv;
	}

	public void setLiLv(BigDecimal liLv) {
		this.updated_at = new Date();
		this.liLv = liLv;
	}

	public BigDecimal getBjje() {
		return this.bjje;
	}

	public void setBjje(BigDecimal bjje) {
		this.updated_at = new Date();
		this.bjje = bjje;
	}

	public Date getCrrq() {
		return this.crrq;
	}

	public void setCrrq(Date crrq) {
		this.updated_at = new Date();
		this.crrq = crrq;
	}

	public Date getDqrq() {
		return this.dqrq;
	}

	public void setDqrq(Date dqrq) {
		this.updated_at = new Date();
		this.dqrq = dqrq;
	}

	public BigDecimal getCkqx() {
		return this.ckqx;
	}

	public void setCkqx(BigDecimal ckqx) {
		this.updated_at = new Date();
		this.ckqx = ckqx;
	}

	public String getZqqk() {
		return this.zqqk;
	}

	public void setZqqk(String zqqk) {
		this.updated_at = new Date();
		this.zqqk = zqqk;
	}

	public Date getQkrq() {
		return this.qkrq;
	}

	public void setQkrq(Date qkrq) {
		this.updated_at = new Date();
		this.qkrq = qkrq;
	}

	public BigDecimal getLxsr() {
		return this.lxsr;
	}

	public void setLxsr(BigDecimal lxsr) {
		this.updated_at = new Date();
		this.lxsr = lxsr;
	}

}

package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_housing_personal_account", indexes = {
		@Index(name = "INDEX_DKZH", columnList = "DKZH", unique = true) })
@org.hibernate.annotations.Table(appliesTo = "st_housing_personal_account", comment = "个人住房贷款账户信息 表6.0.5")
public class StHousingPersonalAccount extends Common implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2916900568210862552L;
	@Column(name = "DKZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '贷款账号'")
	private String dkzh;
	@Column(name = "JKHTBH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '借款合同编号'")
	private String jkhtbh;
	@Column(name = "DKFXDJ", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '贷款风险等级 '")
	private String dkfxdj;
	@Column(name = "DKFFE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '贷款发放额'")
	private BigDecimal dkffe = BigDecimal.ZERO;
	@Column(name = "DKFFRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '贷款发放日期'")
	private Date dkffrq;
	@Column(name = "DKYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '贷款余额'")
	private BigDecimal dkye = BigDecimal.ZERO;
	@Column(name = "DKLL", columnDefinition = "NUMERIC(8,7) DEFAULT NULL COMMENT '贷款利率'")
	private BigDecimal dkll = BigDecimal.ZERO;
	@Column(name = "LLFDBL", columnDefinition = "NUMERIC(4,2) DEFAULT NULL COMMENT '利率浮动比例'")
	private BigDecimal llfdbl = BigDecimal.ZERO;
	@Column(name = "DKQS", columnDefinition = "NUMERIC(4,0) DEFAULT NULL COMMENT '贷款期数'")
	private BigDecimal dkqs = BigDecimal.ZERO;
	@Column(name = "DQJHHKJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '当期计划还款金额'")
	private BigDecimal dqjhhkje = BigDecimal.ZERO;
	@Column(name = "DQJHGHBJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '当期计划归还本金'")
	private BigDecimal dqjhghbj = BigDecimal.ZERO;
	@Column(name = "DQJHGHLX", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '当期计划归还利息'")
	private BigDecimal dqjhghlx = BigDecimal.ZERO;
	@Column(name = "DQYHJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '当前应还金额'")
	private BigDecimal dqyhje = BigDecimal.ZERO;
	@Column(name = "DQYHBJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '当前应还本金'")
	private BigDecimal dqyhbj = BigDecimal.ZERO;
	@Column(name = "DQYHLX", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '当前应还利息'")
	private BigDecimal dqyhlx = BigDecimal.ZERO;
	@Column(name = "DQYHFX", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '当前应还罚息'")
	private BigDecimal dqyhfx = BigDecimal.ZERO;
	@Column(name = "DKJQRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '贷款结清日期'")
	private Date dkjqrq;
	@Column(name = "HSBJZE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '回收本金总额'")
	private BigDecimal hsbjze = BigDecimal.ZERO;
	@Column(name = "HSLXZE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '回收利息总额'")
	private BigDecimal hslxze = BigDecimal.ZERO;
	@Column(name = "FXZE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '罚息总额'")
	private BigDecimal fxze = BigDecimal.ZERO;
	@Column(name = "TQGHBJZE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '提前归还本金总额'")
	private BigDecimal tqghbjze = BigDecimal.ZERO;
	@Column(name = "YQBJZE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '逾期本金总额'")
	private BigDecimal yqbjze = BigDecimal.ZERO;
	@Column(name = "YQLXZE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '逾期利息总额'")
	private BigDecimal yqlxze = BigDecimal.ZERO;
	@Column(name = "LJYQQS", columnDefinition = "NUMERIC(4,0) DEFAULT NULL COMMENT '累计逾期期数'")
	private BigDecimal ljyqqs = BigDecimal.ZERO;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "contract", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '个人住房贷款借款合同信息'")
	private StHousingPersonalLoan stHousingPersonalLoan;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "extenstion", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '个人贷款账户拓展'")
	private CLoanHousingPersonalAccountExtension cLoanHousingPersonalAccountExtension;

	public StHousingPersonalAccount() {
	}

	public StHousingPersonalAccount(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String dkzh, String jkhtbh, String dkfxdj, BigDecimal dkffe, Date dkffrq, BigDecimal dkye, BigDecimal dkll,
			BigDecimal llfdbl, BigDecimal dkqs, BigDecimal dqjhhkje, BigDecimal dqjhghbj, BigDecimal dqjhghlx,
			BigDecimal dqyhje, BigDecimal dqyhbj, BigDecimal dqyhlx, BigDecimal dqyhfx, Date dkjqrq, BigDecimal hsbjze,
			BigDecimal hslxze, BigDecimal fxze, BigDecimal tqghbjze, BigDecimal yqbjze, BigDecimal yqlxze,
			BigDecimal ljyqqs, StHousingPersonalLoan stHousingPersonalLoan,CLoanHousingPersonalAccountExtension cLoanHousingPersonalAccountExtension) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.dkzh = dkzh;
		this.jkhtbh = jkhtbh;
		this.dkfxdj = dkfxdj;
		this.dkffe = dkffe;
		this.dkffrq = dkffrq;
		this.dkye = dkye;
		this.dkll = dkll;
		this.llfdbl = llfdbl;
		this.dkqs = dkqs;
		this.dqjhhkje = dqjhhkje;
		this.dqjhghbj = dqjhghbj;
		this.dqjhghlx = dqjhghlx;
		this.dqyhje = dqyhje;
		this.dqyhbj = dqyhbj;
		this.dqyhlx = dqyhlx;
		this.dqyhfx = dqyhfx;
		this.dkjqrq = dkjqrq;
		this.hsbjze = hsbjze;
		this.hslxze = hslxze;
		this.fxze = fxze;
		this.tqghbjze = tqghbjze;
		this.yqbjze = yqbjze;
		this.yqlxze = yqlxze;
		this.ljyqqs = ljyqqs;
		this.stHousingPersonalLoan = stHousingPersonalLoan;
		this.cLoanHousingPersonalAccountExtension=cLoanHousingPersonalAccountExtension;
	}

	public StHousingPersonalLoan getStHousingPersonalLoan() {
		return stHousingPersonalLoan;
	}

	public void setStHousingPersonalLoan(StHousingPersonalLoan stHousingPersonalLoan) {
		this.updated_at = new Date();
		this.stHousingPersonalLoan = stHousingPersonalLoan;
	}

	public String getDkzh() {
		return this.dkzh;
	}

	public void setDkzh(String dkzh) {
		this.updated_at = new Date();
		this.dkzh = dkzh;
	}

	public String getJkhtbh() {
		return this.jkhtbh;
	}

	public void setJkhtbh(String jkhtbh) {
		this.updated_at = new Date();
		this.jkhtbh = jkhtbh;
	}

	public String getDkfxdj() {
		return this.dkfxdj;
	}

	public void setDkfxdj(String dkfxdj) {
		this.updated_at = new Date();
		this.dkfxdj = dkfxdj;
	}

	public BigDecimal getDkffe() {
		return this.dkffe;
	}

	public void setDkffe(BigDecimal dkffe) {
		this.updated_at = new Date();
		this.dkffe = dkffe;
	}

	public Date getDkffrq() {
		return this.dkffrq;
	}

	public void setDkffrq(Date dkffrq) {
		this.updated_at = new Date();
		this.dkffrq = dkffrq;
	}

	public BigDecimal getDkye() {
		return this.dkye;
	}

	public void setDkye(BigDecimal dkye) {
		this.updated_at = new Date();
		this.dkye = dkye;
	}

	public BigDecimal getDkll() {
		return this.dkll;
	}

	public void setDkll(BigDecimal dkll) {
		this.updated_at = new Date();
		this.dkll = dkll;
	}

	public BigDecimal getLlfdbl() {
		return this.llfdbl;
	}

	public void setLlfdbl(BigDecimal llfdbl) {
		this.updated_at = new Date();
		this.llfdbl = llfdbl;
	}

	public BigDecimal getDkqs() {
		return this.dkqs;
	}

	public void setDkqs(BigDecimal dkqs) {
		this.updated_at = new Date();
		this.dkqs = dkqs;
	}

	public BigDecimal getDqjhhkje() {
		return this.dqjhhkje;
	}

	public void setDqjhhkje(BigDecimal dqjhhkje) {
		this.updated_at = new Date();
		this.dqjhhkje = dqjhhkje;
	}

	public BigDecimal getDqjhghbj() {
		return this.dqjhghbj;
	}

	public void setDqjhghbj(BigDecimal dqjhghbj) {
		this.updated_at = new Date();
		this.dqjhghbj = dqjhghbj;
	}

	public BigDecimal getDqjhghlx() {
		return dqjhghlx;
	}

	public void setDqjhghlx(BigDecimal dqjhghlx) {
		this.updated_at = new Date();
		this.dqjhghlx = dqjhghlx;
	}

	public BigDecimal getDqyhje() {
		return dqyhje;
	}

	public void setDqyhje(BigDecimal dqyhje) {
		this.updated_at = new Date();
		this.dqyhje = dqyhje;
	}

	public BigDecimal getDqyhbj() {
		return dqyhbj;
	}

	public void setDqyhbj(BigDecimal dqyhbj) {
		this.updated_at = new Date();
		this.dqyhbj = dqyhbj;
	}

	public BigDecimal getDqyhlx() {
		return dqyhlx;
	}

	public void setDqyhlx(BigDecimal dqyhlx) {
		this.updated_at = new Date();
		this.dqyhlx = dqyhlx;
	}

	public BigDecimal getDqyhfx() {
		return dqyhfx;
	}

	public void setDqyhfx(BigDecimal dqyhfx) {
		this.updated_at = new Date();
		this.dqyhfx = dqyhfx;
	}

	public Date getDkjqrq() {
		return dkjqrq;
	}

	public void setDkjqrq(Date dkjqrq) {
		this.updated_at = new Date();
		this.dkjqrq = dkjqrq;
	}

	public BigDecimal getHsbjze() {
		return hsbjze;
	}

	public void setHsbjze(BigDecimal hsbjze) {
		this.updated_at = new Date();
		this.hsbjze = hsbjze;
	}

	public BigDecimal getHslxze() {
		return hslxze;
	}

	public void setHslxze(BigDecimal hslxze) {
		this.updated_at = new Date();
		this.hslxze = hslxze;
	}

	public BigDecimal getFxze() {
		return fxze;
	}

	public void setFxze(BigDecimal fxze) {
		this.updated_at = new Date();
		this.fxze = fxze;
	}

	public BigDecimal getTqghbjze() {
		return tqghbjze;
	}

	public void setTqghbjze(BigDecimal tqghbjze) {
		this.updated_at = new Date();
		this.tqghbjze = tqghbjze;
	}

	public BigDecimal getYqbjze() {
		return yqbjze;
	}

	public void setYqbjze(BigDecimal yqbjze) {
		this.updated_at = new Date();
		this.yqbjze = yqbjze;
	}

	public BigDecimal getYqlxze() {
		return yqlxze;
	}

	public void setYqlxze(BigDecimal yqlxze) {
		this.updated_at = new Date();
		this.yqlxze = yqlxze;
	}

	public BigDecimal getLjyqqs() {
		return ljyqqs;
	}

	public void setLjyqqs(BigDecimal ljyqqs) {
		this.updated_at = new Date();
		this.ljyqqs = ljyqqs;
	}

	public CLoanHousingPersonalAccountExtension getcLoanHousingPersonalAccountExtension() {
		return cLoanHousingPersonalAccountExtension;
	}

	public void setcLoanHousingPersonalAccountExtension(CLoanHousingPersonalAccountExtension cLoanHousingPersonalAccountExtension) {
		this.cLoanHousingPersonalAccountExtension = cLoanHousingPersonalAccountExtension;
	}
}

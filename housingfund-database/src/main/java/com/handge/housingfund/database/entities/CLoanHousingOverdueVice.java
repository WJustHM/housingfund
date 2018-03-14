package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "c_loan_housing_overdue_vice")
@org.hibernate.annotations.Table(appliesTo = "c_loan_housing_overdue_vice", comment = "还款-逾期信息表")
public class CLoanHousingOverdueVice extends Common implements java.io.Serializable {

	private static final long serialVersionUID = -948094161737121012L;
	@Column(name = "YWLSH", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '业务流水号'")
	private String ywlsh;
	@Column(name = "DKZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '贷款账号'")
	private String dkzh;
	@Column(name = "HKQC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '还款期次'")
	private String hkqc;
	@Column(name = "SSRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '实收日期'")
	private Date ssrq;
	@Column(name = "SSYQBJJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '实收逾期本金金额'")
	private BigDecimal ssyqbjje = BigDecimal.ZERO;
	@Column(name = "SSYQFXJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '实收逾期罚息金额'")
	private BigDecimal ssyqfxje = BigDecimal.ZERO;
	@Column(name = "SSYQLXJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '实收逾期利息金额'")
	private BigDecimal ssyqlxje = BigDecimal.ZERO;
	@Column(name = "YQBJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '逾期本金'")
	private BigDecimal yqbj = BigDecimal.ZERO;
	@Column(name = "YQFX", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '逾期罚息'")
	private BigDecimal yqfx = BigDecimal.ZERO;
	@Column(name = "YQLX", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '逾期利息'")
	private BigDecimal yqlx = BigDecimal.ZERO;
	@Column(name = "YQQC", columnDefinition = "NUMERIC(4,0) DEFAULT NULL COMMENT '逾期期次'")
	private BigDecimal yqqc = BigDecimal.ZERO;
	@Column(name = "SFHK", columnDefinition = "BIT(1) DEFAULT NULL COMMENT '是否还款（0：否 1：是）'")
	private Boolean sfhk;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "grywmx", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '还款申请表'")
	private CLoanApplyRepaymentVice grywmx;

	public CLoanHousingOverdueVice(String id, Date created_at, boolean deleted, Date deleted_at, Date updated_at,
			String ywlsh, String dkzh, String hkqc, Date ssrq, BigDecimal ssyqbjje, BigDecimal ssyqfxje,
			BigDecimal ssyqlxje, BigDecimal yqbj, BigDecimal yqfx, BigDecimal yqlx, BigDecimal yqqc, Boolean sfhk,
			CLoanApplyRepaymentVice cLoanApplyRepaymentVice) {
		this.id = id;
		this.created_at = created_at;
		this.deleted = deleted;
		this.deleted_at = deleted_at;
		this.updated_at = updated_at;
		this.ywlsh = ywlsh;
		this.dkzh = dkzh;
		this.hkqc = hkqc;
		this.ssrq = ssrq;
		this.ssyqbjje = ssyqbjje;
		this.ssyqfxje = ssyqfxje;
		this.ssyqlxje = ssyqlxje;
		this.yqbj = yqbj;
		this.yqfx = yqfx;
		this.yqlx = yqlx;
		this.yqqc = yqqc;
		this.sfhk = sfhk;
		this.grywmx = cLoanApplyRepaymentVice;
	}

	public CLoanHousingOverdueVice() {
	}

	public String getYwlsh() {
		return ywlsh;
	}

	public void setYwlsh(String ywlsh) {
		this.updated_at = new Date();
		this.ywlsh = ywlsh;
	}

	public String getDkzh() {
		return dkzh;
	}

	public void setDkzh(String dkzh) {
		this.updated_at = new Date();
		this.dkzh = dkzh;
	}

	public String getHkqc() {
		return hkqc;
	}

	public void setHkqc(String hkqc) {
		this.updated_at = new Date();
		this.hkqc = hkqc;
	}

	public Date getSsrq() {
		return ssrq;
	}

	public void setSsrq(Date ssrq) {
		this.updated_at = new Date();
		this.ssrq = ssrq;
	}

	public BigDecimal getSsyqbjje() {
		return ssyqbjje;
	}

	public void setSsyqbjje(BigDecimal ssyqbjje) {
		this.updated_at = new Date();
		this.ssyqbjje = ssyqbjje;
	}

	public BigDecimal getSsyqfxje() {
		return ssyqfxje;
	}

	public void setSsyqfxje(BigDecimal ssyqfxje) {
		this.updated_at = new Date();
		this.ssyqfxje = ssyqfxje;
	}

	public BigDecimal getSsyqlxje() {
		return ssyqlxje;
	}

	public void setSsyqlxje(BigDecimal ssyqlxje) {
		this.updated_at = new Date();
		this.ssyqlxje = ssyqlxje;
	}

	public BigDecimal getYqbj() {
		return yqbj;
	}

	public void setYqbj(BigDecimal yqbj) {
		this.updated_at = new Date();
		this.yqbj = yqbj;
	}

	public BigDecimal getYqfx() {
		return yqfx;
	}

	public void setYqfx(BigDecimal yqfx) {
		this.updated_at = new Date();
		this.yqfx = yqfx;
	}

	public BigDecimal getYqlx() {
		return yqlx;
	}

	public void setYqlx(BigDecimal yqlx) {
		this.updated_at = new Date();
		this.yqlx = yqlx;
	}

	public BigDecimal getYqqc() {
		return yqqc;
	}

	public void setYqqc(BigDecimal yqqc) {
		this.updated_at = new Date();
		this.yqqc = yqqc;
	}

	public Boolean getSfhk() {
		return sfhk;
	}

	public void setSfhk(Boolean sfhk) {
		this.updated_at = new Date();
		this.sfhk = sfhk;
	}

	public CLoanApplyRepaymentVice getcLoanApplyRepaymentVice() {
		return grywmx;
	}

	public void setcLoanApplyRepaymentVice(CLoanApplyRepaymentVice cLoanApplyRepaymentVice) {
		this.updated_at = new Date();
		this.grywmx = cLoanApplyRepaymentVice;
	}

	public CLoanApplyRepaymentVice getGrywmx() {
		return grywmx;
	}

	public void setGrywmx(CLoanApplyRepaymentVice grywmx) {
		this.updated_at = new Date();
		this.grywmx = grywmx;
	}
}

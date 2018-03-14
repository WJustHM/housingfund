package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_housing_overdue_registration")
@org.hibernate.annotations.Table(appliesTo = "st_housing_overdue_registration", comment = "个人住房贷款逾期登记信息 表6.0.7")
public class StHousingOverdueRegistration extends Common implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4963421617971353291L;
	@Column(name = "DKZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '贷款账号'")
	private String dkzh;
	@Column(name = "YQQC", columnDefinition = "NUMERIC(4,0) DEFAULT NULL COMMENT '逾期期次'")
	private BigDecimal yqqc = BigDecimal.ZERO;
	@Column(name = "YQBJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '逾期本金'")
	private BigDecimal yqbj = BigDecimal.ZERO;
	@Column(name = "YQLX", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '逾期利息'")
	private BigDecimal yqlx = BigDecimal.ZERO;
	@Column(name = "YQFX", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '逾期罚息'")
	private BigDecimal yqfx = BigDecimal.ZERO;
	@Column(name = "SSRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '实收日期'")
	private Date ssrq;
	@Column(name = "HKQC", columnDefinition = "NUMERIC(4,0) DEFAULT NULL COMMENT '还款期次'")
	private BigDecimal hkqc = BigDecimal.ZERO;
	@Column(name = "SSYQBJJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '实收逾期本金金额'")
	private BigDecimal ssyqbjje = BigDecimal.ZERO;
	@Column(name = "SSYQLXJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '实收逾期利息金额'")
	private BigDecimal ssyqlxje = BigDecimal.ZERO;
	@Column(name = "SSYQFXJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '实收逾期罚息金额'")
	private BigDecimal ssyqfxje = BigDecimal.ZERO;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "extenstion", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '逾期还款拓展'")
	private CHousingOverdueRegistrationExtension cHousingOverdueRegistrationExtension;

	public StHousingOverdueRegistration() {
	}

	public StHousingOverdueRegistration(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String dkzh, BigDecimal yqqc, BigDecimal yqbj, BigDecimal yqlx, BigDecimal yqfx, Date ssrq, BigDecimal hkqc,
			BigDecimal ssyqbjje, BigDecimal ssyqlxje, BigDecimal ssyqfxje) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.dkzh = dkzh;
		this.yqqc = yqqc;
		this.yqbj = yqbj;
		this.yqlx = yqlx;
		this.yqfx = yqfx;
		this.ssrq = ssrq;
		this.hkqc = hkqc;
		this.ssyqbjje = ssyqbjje;
		this.ssyqlxje = ssyqlxje;
		this.ssyqfxje = ssyqfxje;
	}

	public String getDkzh() {
		return this.dkzh;
	}

	public void setDkzh(String dkzh) {
		this.updated_at = new Date();
		this.dkzh = dkzh;
	}

	public BigDecimal getYqqc() {
		return this.yqqc;
	}

	public void setYqqc(BigDecimal yqqc) {
		this.updated_at = new Date();
		this.yqqc = yqqc;
	}

	public BigDecimal getYqbj() {
		return this.yqbj;
	}

	public void setYqbj(BigDecimal yqbj) {
		this.updated_at = new Date();
		this.yqbj = yqbj;
	}

	public BigDecimal getYqlx() {
		return this.yqlx;
	}

	public void setYqlx(BigDecimal yqlx) {
		this.updated_at = new Date();
		this.yqlx = yqlx;
	}

	public BigDecimal getYqfx() {
		return this.yqfx;
	}

	public void setYqfx(BigDecimal yqfx) {
		this.updated_at = new Date();
		this.yqfx = yqfx;
	}

	public Date getSsrq() {
		return this.ssrq;
	}

	public void setSsrq(Date ssrq) {
		this.updated_at = new Date();
		this.ssrq = ssrq;
	}

	public BigDecimal getHkqc() {
		return this.hkqc;
	}

	public void setHkqc(BigDecimal hkqc) {
		this.updated_at = new Date();
		this.hkqc = hkqc;
	}

	public BigDecimal getSsyqbjje() {
		return this.ssyqbjje;
	}

	public void setSsyqbjje(BigDecimal ssyqbjje) {
		this.updated_at = new Date();
		this.ssyqbjje = ssyqbjje;
	}

	public BigDecimal getSsyqlxje() {
		return this.ssyqlxje;
	}

	public void setSsyqlxje(BigDecimal ssyqlxje) {
		this.updated_at = new Date();
		this.ssyqlxje = ssyqlxje;
	}

	public BigDecimal getSsyqfxje() {
		return this.ssyqfxje;
	}

	public void setSsyqfxje(BigDecimal ssyqfxje) {
		this.updated_at = new Date();
		this.ssyqfxje = ssyqfxje;
	}
	public CHousingOverdueRegistrationExtension getcHousingOverdueRegistrationExtension() {
		return cHousingOverdueRegistrationExtension;
	}

	public void setcHousingOverdueRegistrationExtension(CHousingOverdueRegistrationExtension cHousingOverdueRegistrationExtension) {
		this.updated_at = new Date();
		this.cHousingOverdueRegistrationExtension = cHousingOverdueRegistrationExtension;
	}
}

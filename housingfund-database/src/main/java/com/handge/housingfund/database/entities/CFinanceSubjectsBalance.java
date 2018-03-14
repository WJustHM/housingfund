package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by tanyi on 2017/9/1.
 */
@Entity
@Table(name = "c_finance_subjects_balance")
@org.hibernate.annotations.Table(appliesTo = "c_finance_subjects_balance", comment = "科目余额表")
public class CFinanceSubjectsBalance extends Common implements Serializable {

	private static final long serialVersionUID = -6163263129291824294L;

	@Column(name = "KMBH", columnDefinition = "VARCHAR(19) DEFAULT NULL COMMENT '科目编号 附A.25'")
	private String kmbh;

	@Column(name = "KMMC", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '科目名称'")
	private String kmmc;

	@Column(name = "SYYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '上月余额'")
	private BigDecimal syye  = BigDecimal.ZERO;

	@Column(name = "BYZJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '本月增加'")
	private BigDecimal byzj   = BigDecimal.ZERO;

	@Column(name = "BYJS", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '本月减少'")
	private BigDecimal byjs   = BigDecimal.ZERO;

	@Column(name = "BYYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '本月余额'")
	private BigDecimal byye   = BigDecimal.ZERO;

	@Column(name = "KMJB", columnDefinition = "NUMERIC(1,0) DEFAULT NULL COMMENT '科目级别'")
	private BigDecimal kmjb = BigDecimal.ZERO;

	@Column(name = "KMYEFX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '科目余额方向 附A.27'")
	private String kmyefx;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "cFinanceAccountPeriod", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '会计期间'")
	private CFinanceAccountPeriod cFinanceAccountPeriod;

	public String getKmyefx() {
		return kmyefx;
	}

	public void setKmyefx(String kmyefx) {
		this.updated_at = new Date();
		this.kmyefx = kmyefx;
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

	public BigDecimal getSyye() {
		return syye;
	}

	public void setSyye(BigDecimal syye) {

		this.updated_at = new Date();
		this.syye = syye;
	}

	public BigDecimal getByzj() {
		return byzj;
	}

	public void setByzj(BigDecimal byzj) {
		this.updated_at = new Date();
		this.byzj = byzj;
	}

	public BigDecimal getByjs() {
		return byjs;
	}

	public void setByjs(BigDecimal byjs) {

		this.updated_at = new Date();
		this.byjs = byjs;
	}

	public BigDecimal getByye() {
		return byye;
	}

	public void setByye(BigDecimal byye) {

		this.updated_at = new Date();
		this.byye = byye;
	}

	public CFinanceAccountPeriod getcFinanceAccountPeriod() {
		return cFinanceAccountPeriod;
	}

	public void setcFinanceAccountPeriod(CFinanceAccountPeriod cFinanceAccountPeriod) {
		this.updated_at = new Date();
		this.cFinanceAccountPeriod = cFinanceAccountPeriod;
	}

	public BigDecimal getKmjb() {
		return kmjb;
	}

	public void setKmjb(BigDecimal kmjb) {

		this.updated_at = new Date();
		this.kmjb = kmjb;
	}

	public CFinanceSubjectsBalance(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String kmbh, String kmmc, BigDecimal syye, BigDecimal byzj, BigDecimal byjs, BigDecimal byye,
			BigDecimal kmjb, String kmyefx, CFinanceAccountPeriod cFinanceAccountPeriod) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.kmbh = kmbh;
		this.kmmc = kmmc;
		this.syye = syye;
		this.byzj = byzj;
		this.byjs = byjs;
		this.byye = byye;
		this.kmjb = kmjb;
		this.kmyefx = kmyefx;
		this.cFinanceAccountPeriod = cFinanceAccountPeriod;
	}

	public CFinanceSubjectsBalance() {
		super();

	}

}

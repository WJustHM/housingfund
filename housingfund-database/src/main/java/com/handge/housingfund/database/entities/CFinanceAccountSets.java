package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "c_finance_account_sets")
@org.hibernate.annotations.Table(appliesTo = "c_finance_account_sets", comment = "账套表")
public class CFinanceAccountSets extends Common implements Serializable {

	private static final long serialVersionUID = 7990419728037052662L;

	@Column(name = "ZTMC", columnDefinition = "VARCHAR(255) NOT NULL COMMENT '账套名称'")
	private String ztmc;
	@Column(name = "KJZG", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '会计主管'")
	private String kjzg;
	@Column(name = "QYRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '启用日期'")
	private Date ztqyrq;
	@Column(name = "QYQJ", columnDefinition = "VARCHAR(6) NOT NULL COMMENT '启用期间'")
	private String qyqj;
	@Column(name = "JZRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '建账日期'")
	private Date jzrq;
	@Column(name = "JZR", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '建账人'")
	private String jzr;
	@Column(name = "ZTBZ", columnDefinition = "TEXT DEFAULT NULL COMMENT '备注'")
	private String ztbz;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cFinanceAccountSets")
	private List<CFinanceAccountPeriod> cFinanceAccountPeriods;

	public String getZtmc() {
		return ztmc;
	}

	public void setZtmc(String ztmc) {
		this.updated_at = new Date();
		this.ztmc = ztmc;
	}

	public String getKjzg() {
		return kjzg;
	}

	public void setKjzg(String kjzg) {
		this.updated_at = new Date();
		this.kjzg = kjzg;
	}

	public Date getZtqyrq() {
		return ztqyrq;
	}

	public void setZtqyrq(Date ztqyrq) {
		this.updated_at = new Date();
		this.ztqyrq = ztqyrq;
	}

	public String getQyqj() {
		return qyqj;
	}

	public void setQyqj(String qyqj) {
		this.qyqj = qyqj;
	}

	public Date getJzrq() {
		return jzrq;
	}

	public void setJzrq(Date jzrq) {
		this.updated_at = new Date();
		this.jzrq = jzrq;
	}

	public String getJzr() {
		return jzr;
	}

	public void setJzr(String jzr) {
		this.updated_at = new Date();
		this.jzr = jzr;
	}

	public String getZtbz() {
		return ztbz;
	}

	public void setZtbz(String ztbz) {
		this.updated_at = new Date();
		this.ztbz = ztbz;
	}

	public List<CFinanceAccountPeriod> getcFinanceAccountPeriods() {
		return cFinanceAccountPeriods;
	}

	public void setcFinanceAccountPeriods(List<CFinanceAccountPeriod> cFinanceAccountPeriods) {
		this.updated_at = new Date();
		this.cFinanceAccountPeriods = cFinanceAccountPeriods;
	}

	public CFinanceAccountSets() {
		super();

	}

	public CFinanceAccountSets(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String ztmc, String kjzg, Date ztqyrq, String qyqj, Date jzrq, String jzr, String ztbz,
			List<CFinanceAccountPeriod> cFinanceAccountPeriods) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.ztmc = ztmc;
		this.kjzg = kjzg;
		this.ztqyrq = ztqyrq;
		this.qyqj = qyqj;
		this.jzrq = jzrq;
		this.jzr = jzr;
		this.ztbz = ztbz;
		this.cFinanceAccountPeriods = cFinanceAccountPeriods;
	}
}

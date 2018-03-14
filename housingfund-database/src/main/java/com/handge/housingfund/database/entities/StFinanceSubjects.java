package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_finance_subjects")
@org.hibernate.annotations.Table(appliesTo = "st_finance_subjects", comment = "科目信息 表8.0.2")
public class StFinanceSubjects extends Common implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -396346621284650470L;
	@Column(name = "KMBH", columnDefinition = "VARCHAR(19) DEFAULT NULL COMMENT '科目编号'")
	private String kmbh;
	@Column(name = "KMMC", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '科目名称'")
	private String kmmc;
	@Column(name = "KMJB", columnDefinition = "NUMERIC(1,0) DEFAULT NULL COMMENT '科目级别'")
	private BigDecimal kmjb = BigDecimal.ZERO;
	@Column(name = "KMSX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '科目属性'")
	private String kmsx;
	@Column(name = "KMYEFX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '科目余额方向'")
	private String kmyefx;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "extension", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '科目信息扩展'")
	private CFinanceSubjectsExtension cFinanceSubjectsExtension;

	public StFinanceSubjects() {
		super();

	}

	public StFinanceSubjects(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted, String kmbh,
			String kmmc, BigDecimal kmjb, String kmsx, String kmyefx,
			CFinanceSubjectsExtension cFinanceSubjectsExtension) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.kmbh = kmbh;
		this.kmmc = kmmc;
		this.kmjb = kmjb;
		this.kmsx = kmsx;
		this.kmyefx = kmyefx;
		this.cFinanceSubjectsExtension = cFinanceSubjectsExtension;
	}

	public CFinanceSubjectsExtension getcFinanceSubjectsExtension() {
		return cFinanceSubjectsExtension;
	}

	public void setcFinanceSubjectsExtension(CFinanceSubjectsExtension cFinanceSubjectsExtension) {
		this.updated_at = new Date();
		this.cFinanceSubjectsExtension = cFinanceSubjectsExtension;
	}

	public String getKmbh() {
		return this.kmbh;
	}

	public void setKmbh(String kmbh) {
		this.updated_at = new Date();
		this.kmbh = kmbh;
	}

	public String getKmmc() {
		return this.kmmc;
	}

	public void setKmmc(String kmmc) {
		this.updated_at = new Date();
		this.kmmc = kmmc;
	}

	public BigDecimal getKmjb() {
		return kmjb;
	}

	public void setKmjb(BigDecimal kmjb) {
		this.updated_at = new Date();
		this.kmjb = kmjb;
	}

	public String getKmsx() {
		return this.kmsx;
	}

	public void setKmsx(String kmsx) {
		this.updated_at = new Date();
		this.kmsx = kmsx;
	}

	public String getKmyefx() {
		return this.kmyefx;
	}

	public void setKmyefx(String kmyefx) {
		this.updated_at = new Date();
		this.kmyefx = kmyefx;
	}

}

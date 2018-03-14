package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_project_loan_details")
@org.hibernate.annotations.Table(appliesTo = "st_project_loan_details", comment = "项目贷款业务明细信息 表7.0.6")
public class StProjectLoanDetails extends Common implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7568501093126709981L;
	@Column(name = "DKZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '贷款账号'")
	private String dkzh;
	@Column(name = "YWLSH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务流水号'")
	private String ywlsh;
	@Column(name = "YWMXLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '业务明细类型'")
	private String ywmxlx;
	@Column(name = "FSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '发生额'")
	private BigDecimal fse = BigDecimal.ZERO;
	@Column(name = "BJJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '本金金额'")
	private BigDecimal bjje = BigDecimal.ZERO;
	@Column(name = "LXJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '利息金额'")
	private BigDecimal lxje = BigDecimal.ZERO;
	@Column(name = "YQFXJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '逾期罚息金额'")
	private BigDecimal yqfxje = BigDecimal.ZERO;
	@Column(name = "NYFXJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '挪用罚息金额'")
	private BigDecimal nyfxje = BigDecimal.ZERO;
	@Column(name = "JZRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '记账日期'")
	private Date jzrq;

	public StProjectLoanDetails(){
       super();


	}

	public StProjectLoanDetails(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String dkzh, String ywlsh, String ywmxlx, BigDecimal fse, BigDecimal bjje, BigDecimal lxje,
			BigDecimal yqfxje, BigDecimal nyfxje, Date jzrq) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.dkzh = dkzh;
		this.ywlsh = ywlsh;
		this.ywmxlx = ywmxlx;
		this.fse = fse;
		this.bjje = bjje;
		this.lxje = lxje;
		this.yqfxje = yqfxje;
		this.nyfxje = nyfxje;
		this.jzrq = jzrq;
	}

	public String getDkzh() {
		return this.dkzh;
	}

	public void setDkzh(String dkzh) {
       this.updated_at = new Date();
		this.dkzh = dkzh;
	}

	public String getYwlsh() {
		return this.ywlsh;
	}

	public void setYwlsh(String ywlsh) {
       this.updated_at = new Date();
		this.ywlsh = ywlsh;
	}

	public String getYwmxlx() {
		return this.ywmxlx;
	}

	public void setYwmxlx(String ywmxlx) {
       this.updated_at = new Date();
		this.ywmxlx = ywmxlx;
	}

	public BigDecimal getFse() {
		return this.fse;
	}

	public void setFse(BigDecimal fse) {
       this.updated_at = new Date();
		this.fse = fse;
	}

	public BigDecimal getBjje() {
		return this.bjje;
	}

	public void setBjje(BigDecimal bjje) {
       this.updated_at = new Date();
		this.bjje = bjje;
	}

	public BigDecimal getLxje() {
		return this.lxje;
	}

	public void setLxje(BigDecimal lxje) {
       this.updated_at = new Date();
		this.lxje = lxje;
	}

	public BigDecimal getYqfxje() {
		return this.yqfxje;
	}

	public void setYqfxje(BigDecimal yqfxje) {
       this.updated_at = new Date();
		this.yqfxje = yqfxje;
	}

	public BigDecimal getNyfxje() {
		return this.nyfxje;
	}

	public void setNyfxje(BigDecimal nyfxje) {
       this.updated_at = new Date();
		this.nyfxje = nyfxje;
	}

	public Date getJzrq() {
		return this.jzrq;
	}

	public void setJzrq(Date jzrq) {
       this.updated_at = new Date();
		this.jzrq = jzrq;
	}

}

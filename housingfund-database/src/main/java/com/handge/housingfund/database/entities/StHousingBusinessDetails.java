package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_housing_business_details")
@org.hibernate.annotations.Table(appliesTo = "st_housing_business_details", comment = "个人住房贷款业务明细信息 表6.0.6")
public class StHousingBusinessDetails extends Common implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 45225687559292160L;
	@Column(name = "DKZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '贷款账号'")
	private String dkzh;
	@Column(name = "YWLSH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务流水号'")
	private String ywlsh;
	@Column(name = "DKYWMXLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '贷款业务明细类型'")
	private String dkywmxlx;
	@Column(name = "YWFSRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '业务发生日期'")
	private Date ywfsrq;
	@Column(name = "DKYHDM", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '贷款银行代码'")
	private String dkyhdm;
	@Column(name = "FSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '发生额'")
	private BigDecimal fse = BigDecimal.ZERO;
	@Column(name = "BJJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '本金金额'")
	private BigDecimal bjje = BigDecimal.ZERO;
	@Column(name = "LXJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '利息金额'")
	private BigDecimal lxje = BigDecimal.ZERO;
	@Column(name = "FXJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '罚息金额'")
	private BigDecimal fxje = BigDecimal.ZERO;
	@Column(name = "DQQC", columnDefinition = "NUMERIC(4,0) DEFAULT NULL COMMENT '当期期次'")
	private BigDecimal dqqc = BigDecimal.ZERO;
	@Column(name = "ZCZYQBJJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '正常转逾期本金金额'")
	private BigDecimal zczyqbjje = BigDecimal.ZERO;
	@Column(name = "YQZZCBJJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '逾期转正常本金金额'")
	private BigDecimal yqzzcbjje = BigDecimal.ZERO;
	@Column(name = "JZRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '记账日期'")
	private Date jzrq;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "grywmx", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '还款业务副表'")
	private CLoanHousingBusinessProcess grywmx;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "extenstion", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '个人贷款合同拓展'")
	private CHousingBusinessDetailsExtension cHousingBusinessDetailsExtension;

	public StHousingBusinessDetails() {
	}

	public StHousingBusinessDetails(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String dkzh, String ywlsh, String dkywmxlx, Date ywfsrq, String dkyhdm, BigDecimal fse, BigDecimal bjje,
			BigDecimal lxje, BigDecimal fxje, BigDecimal dqqc, BigDecimal zczyqbjje, BigDecimal yqzzcbjje, Date jzrq,
			CLoanHousingBusinessProcess grywmx, CHousingBusinessDetailsExtension cHousingBusinessDetailsExtension) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.dkzh = dkzh;
		this.ywlsh = ywlsh;
		this.dkywmxlx = dkywmxlx;
		this.ywfsrq = ywfsrq;
		this.dkyhdm = dkyhdm;
		this.fse = fse;
		this.bjje = bjje;
		this.lxje = lxje;
		this.fxje = fxje;
		this.dqqc = dqqc;
		this.zczyqbjje = zczyqbjje;
		this.yqzzcbjje = yqzzcbjje;
		this.jzrq = jzrq;
		this.grywmx = grywmx;
		this.cHousingBusinessDetailsExtension = cHousingBusinessDetailsExtension;
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

	public String getDkywmxlx() {
		return this.dkywmxlx;
	}

	public void setDkywmxlx(String dkywmxlx) {
		this.updated_at = new Date();
		this.dkywmxlx = dkywmxlx;
	}

	public Date getYwfsrq() {
		return this.ywfsrq;
	}

	public void setYwfsrq(Date ywfsrq) {
		this.updated_at = new Date();
		this.ywfsrq = ywfsrq;
	}

	public String getDkyhdm() {
		return this.dkyhdm;
	}

	public void setDkyhdm(String dkyhdm) {
		this.updated_at = new Date();
		this.dkyhdm = dkyhdm;
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

	public BigDecimal getFxje() {
		return this.fxje;
	}

	public void setFxje(BigDecimal fxje) {
		this.updated_at = new Date();
		this.fxje = fxje;
	}

	public BigDecimal getDqqc() {
		return this.dqqc;
	}

	public void setDqqc(BigDecimal dqqc) {
		this.updated_at = new Date();
		this.dqqc = dqqc;
	}

	public BigDecimal getZczyqbjje() {
		return this.zczyqbjje;
	}

	public void setZczyqbjje(BigDecimal zczyqbjje) {
		this.updated_at = new Date();
		this.zczyqbjje = zczyqbjje;
	}

	public BigDecimal getYqzzcbjje() {
		return this.yqzzcbjje;
	}

	public void setYqzzcbjje(BigDecimal yqzzcbjje) {
		this.updated_at = new Date();
		this.yqzzcbjje = yqzzcbjje;
	}

	public Date getJzrq() {
		return this.jzrq;
	}

	public void setJzrq(Date jzrq) {
		this.updated_at = new Date();
		this.jzrq = jzrq;
	}

	public CLoanHousingBusinessProcess getGrywmx() {
		return grywmx;
	}

	public void setGrywmx(CLoanHousingBusinessProcess grywmx) {
		this.updated_at = new Date();
		this.grywmx = grywmx;
	}

	public CHousingBusinessDetailsExtension getcHousingBusinessDetailsExtension() {
		return cHousingBusinessDetailsExtension;
	}

	public void setcHousingBusinessDetailsExtension(CHousingBusinessDetailsExtension cHousingBusinessDetailsExtension) {
		this.updated_at = new Date();
		this.cHousingBusinessDetailsExtension = cHousingBusinessDetailsExtension;
	}

}

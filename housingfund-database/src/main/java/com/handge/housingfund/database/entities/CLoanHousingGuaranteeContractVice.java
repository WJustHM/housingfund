package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "c_loan_housing_guarantee_contract_vice")
@org.hibernate.annotations.Table(appliesTo = "c_loan_housing_guarantee_contract_vice", comment = "担保合同信息副表")
public class CLoanHousingGuaranteeContractVice extends Common implements java.io.Serializable {

	private static final long serialVersionUID = 2277572000251847367L;

//	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinColumn(name = "GRYWMX", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '个人业务明细'")
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "loanHousingGuaranteeContractVice")
	private CLoanHousingBusinessProcess grywmx;
	@Column(name = "DBHTBH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '担保合同编号'")
	private String dbhtbh;
	@Column(name = "JKHTBH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '借款合同编号'")
	private String jkhtbh;
	@Column(name = "DKDBLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '贷款担保类型'")
	private String dkdblx;
	@Column(name = "DBJGMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '担保机构名称'")
	private String dbjgmc;
	@Column(name = "DYWQZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '抵押物权证号'")
	private String dywqzh;
	@Column(name = "DYWTXQZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '抵押物他项权证号'")
	private String dywtxqzh;
	@Column(name = "DYWFWZL", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '抵押物房屋坐落'")
	private String dywfwzl;
	@Column(name = "DYQJLRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '抵押权简历日期'")
	private Date dyqjlrq;
	@Column(name = "DYQJCRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '抵押物解除日期'")
	private Date dyqjcrq;
	@Column(name = "DYWPGJZ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '抵押物评估价值'")
	private BigDecimal dywpgjz = BigDecimal.ZERO;
	@Column(name = "BZHTBH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '保证合同编号'")
	private String bzhtbh;
	@Column(name = "BZJGMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '保证机构名称'")
	private String bzjgmc;
	@Column(name = "DKBZJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '贷款保证金'")
	private BigDecimal dkbzj = BigDecimal.ZERO;
	@Column(name = "FHBZJRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '返还保证金日期 YYYYMMDD'")
	private Date fhbzjrq;
	@Column(name = "ZYHTBH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '质押合同编号'")
	private String zyhtbh;
	@Column(name = "ZYWBH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '质押物编号'")
	private String zywbh;
	@Column(name = "ZYWMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '质押物名称'")
	private String zywmc;
	@Column(name = "ZYWJZ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '质押物价值'")
	private BigDecimal zywjz = BigDecimal.ZERO;
	@Column(name = "ZYHTKSRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '质押合同开始日期 YYYYMMDD'")
	private Date zyhtksrq;
	@Column(name = "ZYHTJSRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '质押合同结束日期 YYYYMMDD'")
	private Date zyhtjsrq;

	@Column(name = "BLZL", columnDefinition = "TEXT DEFAULT NULL COMMENT '办理资料'")
	private String blzl;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "guaranteeVices", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '保证副表'")
	private List<CLoanGuaranteeVice> guaranteeVices;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "guaranteeMortgageVices", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '抵押副表'")
	private List<CLoanGuaranteeMortgageVice> guaranteeMortgageVices;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "guaranteePledgeVices", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '质押副表'")
	private List<CLoanGuaranteePledgeVice> guaranteePledgeVices;

	public CLoanHousingGuaranteeContractVice() {
	}

	public CLoanHousingGuaranteeContractVice(String id, Date created_at, Date updated_at, Date deleted_at,
			boolean deleted, String dbhtbh, String jkhtbh, String dkdblx, String dbjgmc, String dywqzh, String dywtxqzh,
			String dywfwzl, Date dyqjlrq, Date dyqjcrq, BigDecimal dywpgjz, String bzhtbh, String bzjgmc,
			BigDecimal dkbzj, Date fhbzjrq, String zyhtbh, String zywbh, String zywmc, BigDecimal zywjz, Date zyhtksrq,
			Date zyhtjsrq, String blzl, List<CLoanGuaranteeVice> guaranteeVices,
			List<CLoanGuaranteeMortgageVice> guaranteeMortgageVices,
			List<CLoanGuaranteePledgeVice> guaranteePledgeVices) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.dbhtbh = dbhtbh;
		this.jkhtbh = jkhtbh;
		this.dkdblx = dkdblx;
		this.dbjgmc = dbjgmc;
		this.dywqzh = dywqzh;
		this.dywtxqzh = dywtxqzh;
		this.dywfwzl = dywfwzl;
		this.dyqjlrq = dyqjlrq;
		this.dyqjcrq = dyqjcrq;
		this.dywpgjz = dywpgjz;
		this.bzhtbh = bzhtbh;
		this.bzjgmc = bzjgmc;
		this.dkbzj = dkbzj;
		this.fhbzjrq = fhbzjrq;
		this.zyhtbh = zyhtbh;
		this.zywbh = zywbh;
		this.zywmc = zywmc;
		this.zywjz = zywjz;
		this.zyhtksrq = zyhtksrq;
		this.zyhtjsrq = zyhtjsrq;
		this.blzl = blzl;
		this.guaranteeVices = guaranteeVices;
		this.guaranteeMortgageVices = guaranteeMortgageVices;
		this.guaranteePledgeVices = guaranteePledgeVices;
	}

	public String getBlzl() {
		return blzl;
	}

	public void setBlzl(String blzl) {
		this.updated_at = new Date();
		this.blzl = blzl;
	}

	public List<CLoanGuaranteeVice> getGuaranteeVices() {
		return guaranteeVices;
	}

	public void setGuaranteeVices(List<CLoanGuaranteeVice> guaranteeVices) {
		this.updated_at = new Date();
		this.guaranteeVices = guaranteeVices;
	}

	public List<CLoanGuaranteeMortgageVice> getGuaranteeMortgageVices() {
		return guaranteeMortgageVices;
	}

	public void setGuaranteeMortgageVices(List<CLoanGuaranteeMortgageVice> guaranteeMortgageVices) {
		this.updated_at = new Date();
		this.guaranteeMortgageVices = guaranteeMortgageVices;
	}

	public List<CLoanGuaranteePledgeVice> getGuaranteePledgeVices() {
		return guaranteePledgeVices;
	}

	public void setGuaranteePledgeVices(List<CLoanGuaranteePledgeVice> guaranteePledgeVices) {
		this.updated_at = new Date();
		this.guaranteePledgeVices = guaranteePledgeVices;
	}

	public String getDbhtbh() {
		return this.dbhtbh;
	}

	public void setDbhtbh(String dbhtbh) {
		this.updated_at = new Date();
		this.dbhtbh = dbhtbh;
	}

	public String getJkhtbh() {
		return this.jkhtbh;
	}

	public void setJkhtbh(String jkhtbh) {
		this.updated_at = new Date();
		this.jkhtbh = jkhtbh;
	}

	public String getDkdblx() {
		return this.dkdblx;
	}

	public void setDkdblx(String dkdblx) {
		this.updated_at = new Date();
		this.dkdblx = dkdblx;
	}

	public String getDbjgmc() {
		return this.dbjgmc;
	}

	public void setDbjgmc(String dbjgmc) {
		this.updated_at = new Date();
		this.dbjgmc = dbjgmc;
	}

	public String getDywqzh() {
		return this.dywqzh;
	}

	public void setDywqzh(String dywqzh) {
		this.updated_at = new Date();
		this.dywqzh = dywqzh;
	}

	public String getDywtxqzh() {
		return this.dywtxqzh;
	}

	public void setDywtxqzh(String dywtxqzh) {
		this.updated_at = new Date();
		this.dywtxqzh = dywtxqzh;
	}

	public String getDywfwzl() {
		return this.dywfwzl;
	}

	public void setDywfwzl(String dywfwzl) {
		this.updated_at = new Date();
		this.dywfwzl = dywfwzl;
	}

	public Date getDyqjlrq() {
		return this.dyqjlrq;
	}

	public void setDyqjlrq(Date dyqjlrq) {
		this.updated_at = new Date();
		this.dyqjlrq = dyqjlrq;
	}

	public Date getDyqjcrq() {
		return this.dyqjcrq;
	}

	public void setDyqjcrq(Date dyqjcrq) {
		this.updated_at = new Date();
		this.dyqjcrq = dyqjcrq;
	}

	public BigDecimal getDywpgjz() {
		return this.dywpgjz;
	}

	public void setDywpgjz(BigDecimal dywpgjz) {
		this.updated_at = new Date();
		this.dywpgjz = dywpgjz;
	}

	public String getBzhtbh() {
		return this.bzhtbh;
	}

	public void setBzhtbh(String bzhtbh) {
		this.updated_at = new Date();
		this.bzhtbh = bzhtbh;
	}

	public CLoanHousingBusinessProcess getGrywmx() {
		return grywmx;
	}

	public void setGrywmx(CLoanHousingBusinessProcess grywmx) {
		this.updated_at = new Date();
		this.grywmx = grywmx;
	}

	public String getBzjgmc() {
		return bzjgmc;
	}

	public void setBzjgmc(String bzjgmc) {
		this.updated_at = new Date();
		this.bzjgmc = bzjgmc;
	}

	public BigDecimal getDkbzj() {
		return dkbzj;
	}

	public void setDkbzj(BigDecimal dkbzj) {
		this.updated_at = new Date();
		this.dkbzj = dkbzj;
	}

	public Date getFhbzjrq() {
		return fhbzjrq;
	}

	public void setFhbzjrq(Date fhbzjrq) {
		this.updated_at = new Date();
		this.fhbzjrq = fhbzjrq;
	}

	public String getZyhtbh() {
		return zyhtbh;
	}

	public void setZyhtbh(String zyhtbh) {
		this.updated_at = new Date();
		this.zyhtbh = zyhtbh;
	}

	public String getZywbh() {
		return zywbh;
	}

	public void setZywbh(String zywbh) {
		this.updated_at = new Date();
		this.zywbh = zywbh;
	}

	public String getZywmc() {
		return zywmc;
	}

	public void setZywmc(String zywmc) {
		this.updated_at = new Date();
		this.zywmc = zywmc;
	}

	public BigDecimal getZywjz() {
		return zywjz;
	}

	public void setZywjz(BigDecimal zywjz) {
		this.updated_at = new Date();
		this.zywjz = zywjz;
	}

	public Date getZyhtksrq() {
		return zyhtksrq;
	}

	public void setZyhtksrq(Date zyhtksrq) {
		this.updated_at = new Date();
		this.zyhtksrq = zyhtksrq;
	}

	public Date getZyhtjsrq() {
		return zyhtjsrq;
	}

	public void setZyhtjsrq(Date zyhtjsrq) {
		this.updated_at = new Date();
		this.zyhtjsrq = zyhtjsrq;
	}

}

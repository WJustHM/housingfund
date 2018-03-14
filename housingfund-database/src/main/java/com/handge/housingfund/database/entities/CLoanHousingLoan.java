package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by tanyi on 2017/8/14.
 */
@Entity
@Table(name = "c_loan_housing_loan")
@org.hibernate.annotations.Table(appliesTo = "c_loan_housing_loan", comment = "放款记录表")
public class CLoanHousingLoan extends Common implements Serializable {

	private static final long serialVersionUID = -198066773339134789L;
	@Column(name = "YWLSH", nullable = false, columnDefinition = "VARCHAR(20) NOT NULL COMMENT '业务流水号'")
	private String ywlsh;
	@Column(name = "JKHTBH", nullable = false, columnDefinition = "VARCHAR(30) NOT NULL COMMENT '借款合同编号'")
	private String jkhtbh;
	@Column(name = "DKZH", nullable = false, columnDefinition = "VARCHAR(30) NOT NULL COMMENT '贷款账号'")
	private String dkzh;
	@Column(name = "JKRXM", nullable = false, columnDefinition = "VARCHAR(120) NOT NULL COMMENT '借款人姓名'")
	private String jkrxm;
	@Column(name = "JKRZJLX", nullable = false, columnDefinition = "VARCHAR(2) NOT NULL COMMENT '借款人证件类型'")
	private String jkrzjlx;
	@Column(name = "JKRZJHM", nullable = false, columnDefinition = "VARCHAR(18) NOT NULL COMMENT '借款人证件号码'")
	private String jkrzjhm;
	@Column(name = "FKYHMC", nullable = false, columnDefinition = "VARCHAR(120) NOT NULL COMMENT '放款银行名称'")
	private String fkyhmc;
	@Column(name = "FKZH", nullable = false, columnDefinition = "VARCHAR(30) NOT NULL COMMENT '放款账户'")
	private String fkzh;
	@Column(name = "FKZHHM", nullable = false, columnDefinition = "VARCHAR(120) NOT NULL COMMENT '放款账户户名'")
	private String fkzhhm;
	@Column(name = "SKYHMC", nullable = false, columnDefinition = "VARCHAR(120) NOT NULL COMMENT '收款银行名称'")
	private String skyhmc;
	@Column(name = "SKZH", nullable = false, columnDefinition = "VARCHAR(30) NOT NULL COMMENT '收款账户'")
	private String skzh;
	@Column(name = "SKZHHM", nullable = false, columnDefinition = "VARCHAR(120) NOT NULL COMMENT '收款账户户名'")
	private String skzhhm;
	@Column(name = "DKFFE", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '贷款发放额'")
	private String dkffe;
	@Column(name = "DKFFRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '贷款发放日期'")
	private Date dkffrq;
	@Column(name = "CZY", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '操作员'")
	private String czy;
	@Column(name = "YWWD", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '业务网点'")
	private String ywwd;
	@Column(name = "state", nullable = false, columnDefinition = "INTEGER(4) DEFAULT 0 COMMENT '状态（0、待入账，1、已入账）'")
	private Integer state;
	@Column(name = "SBYY", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '失败原因'")
	private String sbyy;

	public CLoanHousingLoan() {
	}

	public CLoanHousingLoan(String id, Date created_at, Boolean deleted, Date deleted_at, Date updated_at, String ywlsh,
			String jkhtbh, String dkzh, String jkrxm, String jkrzjlx, String jkrzjhm, String fkyhmc, String fkzh,
			String fkzhhm, String skyhmc, String skzh, String skzhhm, String dkffe, Date dkffrq, String czy,
			String ywwd, Integer state, String sbyy) {
		this.id = id;
		this.created_at = created_at;
		this.deleted = deleted;
		this.deleted_at = deleted_at;
		this.updated_at = updated_at;
		this.ywlsh = ywlsh;
		this.jkhtbh = jkhtbh;
		this.dkzh = dkzh;
		this.jkrxm = jkrxm;
		this.jkrzjlx = jkrzjlx;
		this.jkrzjhm = jkrzjhm;
		this.fkyhmc = fkyhmc;
		this.fkzh = fkzh;
		this.fkzhhm = fkzhhm;
		this.skyhmc = skyhmc;
		this.skzh = skzh;
		this.skzhhm = skzhhm;
		this.dkffe = dkffe;
		this.dkffrq = dkffrq;
		this.czy = czy;
		this.ywwd = ywwd;
		this.state = state;
		this.sbyy = sbyy;
	}

	public String getSbyy() {
		return sbyy;
	}

	public void setSbyy(String sbyy) {
		this.sbyy = sbyy;
	}

	public String getCzy() {
		return czy;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.updated_at = new Date();
		this.state = state;
	}

	public void setCzy(String czy) {
		this.updated_at = new Date();
		this.czy = czy;
	}

	public String getYwwd() {
		return ywwd;
	}

	public void setYwwd(String ywwd) {
		this.updated_at = new Date();
		this.ywwd = ywwd;
	}

	public Date getDkffrq() {
		return dkffrq;
	}

	public void setDkffrq(Date dkffrq) {
		this.updated_at = new Date();
		this.dkffrq = dkffrq;
	}

	public String getYwlsh() {
		return ywlsh;
	}

	public void setYwlsh(String ywlsh) {
		this.updated_at = new Date();
		this.ywlsh = ywlsh;
	}

	public String getJkhtbh() {
		return jkhtbh;
	}

	public void setJkhtbh(String jkhtbh) {
		this.updated_at = new Date();
		this.jkhtbh = jkhtbh;
	}

	public String getDkzh() {
		return dkzh;
	}

	public void setDkzh(String dkzh) {
		this.updated_at = new Date();
		this.dkzh = dkzh;
	}

	public String getJkrxm() {
		return jkrxm;
	}

	public void setJkrxm(String jkrxm) {
		this.updated_at = new Date();
		this.jkrxm = jkrxm;
	}

	public String getJkrzjlx() {
		return jkrzjlx;
	}

	public void setJkrzjlx(String jkrzjlx) {
		this.updated_at = new Date();
		this.jkrzjlx = jkrzjlx;
	}

	public String getJkrzjhm() {
		return jkrzjhm;
	}

	public void setJkrzjhm(String jkrzjhm) {
		this.updated_at = new Date();
		this.jkrzjhm = jkrzjhm;
	}

	public String getFkyhmc() {
		return fkyhmc;
	}

	public void setFkyhmc(String fkyhmc) {
		this.updated_at = new Date();
		this.fkyhmc = fkyhmc;
	}

	public String getFkzh() {
		return fkzh;
	}

	public void setFkzh(String fkzh) {
		this.updated_at = new Date();
		this.fkzh = fkzh;
	}

	public String getFkzhhm() {
		return fkzhhm;
	}

	public String getSkyhmc() {
		return skyhmc;
	}

	public void setSkyhmc(String skyhmc) {
		this.updated_at = new Date();
		this.skyhmc = skyhmc;
	}

	public String getSkzh() {
		return skzh;
	}

	public void setSkzh(String skzh) {
		this.updated_at = new Date();
		this.skzh = skzh;
	}

	public String getSkzhhm() {
		return skzhhm;
	}

	public void setSkzhhm(String skzhhm) {
		this.updated_at = new Date();
		this.skzhhm = skzhhm;
	}

	public String getDkffe() {
		return dkffe;
	}

	public void setDkffe(String dkffe) {
		this.updated_at = new Date();
		this.dkffe = dkffe;
	}

	public void setFkzhhm(String fkzhhm) {
		this.updated_at = new Date();
		this.fkzhhm = fkzhhm;
	}

}

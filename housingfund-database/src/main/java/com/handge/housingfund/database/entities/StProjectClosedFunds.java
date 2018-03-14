package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_project_closed_funds")
@org.hibernate.annotations.Table(appliesTo = "st_project_closed_funds", comment = "资金封闭管理信息 表7.0.8")
public class StProjectClosedFunds extends Common implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3986800015134667606L;
	@Column(name = "XMBH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '项目编号'")
	private String xmbh;
	@Column(name = "YWLSH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务流水号'")
	private String ywlsh;
	@Column(name = "ZJJGZHHM", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '资金监管账户号码'")
	private String zjjgzhhm;
	@Column(name = "ZJJGZHQCYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '资金监管账户期初余额'")
	private BigDecimal zjjgzhqcye = BigDecimal.ZERO;
	@Column(name = "QCYEFX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '期初余额方向'")
	private String qcyefx;
	@Column(name = "JFFSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '借方发生额'")
	private BigDecimal jffse = BigDecimal.ZERO;
	@Column(name = "DFFSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '贷方发生额'")
	private BigDecimal dffse = BigDecimal.ZERO;
	@Column(name = "ZJJGZHQMYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '资金监管账户期末余额'")
	private BigDecimal zjjgzhqmye = BigDecimal.ZERO;
	@Column(name = "QMYEFX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '期末余额方向'")
	private String qmyefx;
	@Column(name = "ZFYT", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '支付用途'")
	private String zfyt;
	@Column(name = "ZJLRLY", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '资金流入来源'")
	private String zjlrly;
	@Column(name = "JYDSZHMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '交易对手账户名称'")
	private String jydszhmc;
	@Column(name = "JYDSZHHM", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '交易对手账户号码'")
	private String jydszhhm;
	@Column(name = "JYDSZHYHDM", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '交易对手账户银行代码'")
	private String jydszhyhdm;
	@Column(name = "JZRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '记账日期'")
	private Date jzrq;

	public StProjectClosedFunds() {
		super();

	}

	public StProjectClosedFunds(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String xmbh, String ywlsh, String zjjgzhhm, BigDecimal zjjgzhqcye, String qcyefx, BigDecimal jffse,
			BigDecimal dffse, BigDecimal zjjgzhqmye, String qmyefx, String zfyt, String zjlrly, String jydszhmc,
			String jydszhhm, String jydszhyhdm, Date jzrq) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.xmbh = xmbh;
		this.ywlsh = ywlsh;
		this.zjjgzhhm = zjjgzhhm;
		this.zjjgzhqcye = zjjgzhqcye;
		this.qcyefx = qcyefx;
		this.jffse = jffse;
		this.dffse = dffse;
		this.zjjgzhqmye = zjjgzhqmye;
		this.qmyefx = qmyefx;
		this.zfyt = zfyt;
		this.zjlrly = zjlrly;
		this.jydszhmc = jydszhmc;
		this.jydszhhm = jydszhhm;
		this.jydszhyhdm = jydszhyhdm;
		this.jzrq = jzrq;
	}

	public String getXmbh() {
		return this.xmbh;
	}

	public void setXmbh(String xmbh) {
		this.updated_at = new Date();
		this.xmbh = xmbh;
	}

	public String getYwlsh() {
		return this.ywlsh;
	}

	public void setYwlsh(String ywlsh) {
		this.updated_at = new Date();
		this.ywlsh = ywlsh;
	}

	public String getZjjgzhhm() {
		return this.zjjgzhhm;
	}

	public void setZjjgzhhm(String zjjgzhhm) {
		this.updated_at = new Date();
		this.zjjgzhhm = zjjgzhhm;
	}

	public BigDecimal getZjjgzhqcye() {
		return this.zjjgzhqcye;
	}

	public void setZjjgzhqcye(BigDecimal zjjgzhqcye) {
		this.updated_at = new Date();
		this.zjjgzhqcye = zjjgzhqcye;
	}

	public String getQcyefx() {
		return this.qcyefx;
	}

	public void setQcyefx(String qcyefx) {
		this.updated_at = new Date();
		this.qcyefx = qcyefx;
	}

	public BigDecimal getJffse() {
		return this.jffse;
	}

	public void setJffse(BigDecimal jffse) {
		this.updated_at = new Date();
		this.jffse = jffse;
	}

	public BigDecimal getDffse() {
		return this.dffse;
	}

	public void setDffse(BigDecimal dffse) {
		this.updated_at = new Date();
		this.dffse = dffse;
	}

	public BigDecimal getZjjgzhqmye() {
		return this.zjjgzhqmye;
	}

	public void setZjjgzhqmye(BigDecimal zjjgzhqmye) {
		this.updated_at = new Date();
		this.zjjgzhqmye = zjjgzhqmye;
	}

	public String getQmyefx() {
		return this.qmyefx;
	}

	public void setQmyefx(String qmyefx) {
		this.updated_at = new Date();
		this.qmyefx = qmyefx;
	}

	public String getZfyt() {
		return this.zfyt;
	}

	public void setZfyt(String zfyt) {
		this.updated_at = new Date();
		this.zfyt = zfyt;
	}

	public String getZjlrly() {
		return this.zjlrly;
	}

	public void setZjlrly(String zjlrly) {
		this.updated_at = new Date();
		this.zjlrly = zjlrly;
	}

	public String getJydszhmc() {
		return this.jydszhmc;
	}

	public void setJydszhmc(String jydszhmc) {
		this.updated_at = new Date();
		this.jydszhmc = jydszhmc;
	}

	public String getJydszhhm() {
		return this.jydszhhm;
	}

	public void setJydszhhm(String jydszhhm) {
		this.updated_at = new Date();
		this.jydszhhm = jydszhhm;
	}

	public String getJydszhyhdm() {
		return this.jydszhyhdm;
	}

	public void setJydszhyhdm(String jydszhyhdm) {
		this.updated_at = new Date();
		this.jydszhyhdm = jydszhyhdm;
	}

	public Date getJzrq() {
		return this.jzrq;
	}

	public void setJzrq(Date jzrq) {
		this.updated_at = new Date();
		this.jzrq = jzrq;
	}

}

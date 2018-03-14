package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_settlement_daybook")
@org.hibernate.annotations.Table(appliesTo = "st_settlement_daybook", comment = "银行结算流水信息")
public class StSettlementDaybook extends Common implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1751861484945933729L;

	@Column(name = "YWLSH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务流水号'")
	private String ywlsh;
	@Column(name = "ZJYWLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '资金业务类型'")
	private String zjywlx;
	@Column(name = "YWPZHM", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务凭证号码'")
	private String ywpzhm;
	@Column(name = "YHJSLSH", columnDefinition = "VARCHAR(40) DEFAULT NULL COMMENT '银行结算流水号'")
	private String yhjslsh;
	@Column(name = "FSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '发生额'")
	private BigDecimal fse = BigDecimal.ZERO;
	@Column(name = "JSFSRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '结算发生日期'")
	private Date jsfsrq;
	@Column(name = "JSYHDM", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '结算银行代码'")
	private String jsyhdm;
	@Column(name = "FKYHDM", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '付款银行代码'")
	private String fkyhdm;
	@Column(name = "FKZHHM", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '付款账户号码'")
	private String fkzhhm;
	@Column(name = "FKZHMC", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '付款账户名称'")
	private String fkzhmc;
	@Column(name = "SKYHDM", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '收款银行代码'")
	private String skyhdm;
	@Column(name = "SKZHHM", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '收款账户号码'")
	private String skzhhm;
	@Column(name = "SKZHMC", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '收款账户名称'")
	private String skzhmc;
	@Column(name = "ZhaiYao", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '摘要'")
	private String zhaiYao;

	public StSettlementDaybook(){
       super();


	}

	public StSettlementDaybook(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String ywlsh, String zjywlx, String ywpzhm, String yhjslsh, BigDecimal fse, Date jsfsrq, String jsyhdm,
			String fkyhdm, String fkzhhm, String fkzhmc, String skyhdm, String skzhhm, String skzhmc, String zhaiYao) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.ywlsh = ywlsh;
		this.zjywlx = zjywlx;
		this.ywpzhm = ywpzhm;
		this.yhjslsh = yhjslsh;
		this.fse = fse;
		this.jsfsrq = jsfsrq;
		this.jsyhdm = jsyhdm;
		this.fkyhdm = fkyhdm;
		this.fkzhhm = fkzhhm;
		this.fkzhmc = fkzhmc;
		this.skyhdm = skyhdm;
		this.skzhhm = skzhhm;
		this.skzhmc = skzhmc;
		this.zhaiYao = zhaiYao;
	}

	public String getYwlsh() {
		return this.ywlsh;
	}

	public void setYwlsh(String ywlsh) {
       this.updated_at = new Date();
		this.ywlsh = ywlsh;
	}

	public String getZjywlx() {
		return this.zjywlx;
	}

	public void setZjywlx(String zjywlx) {
       this.updated_at = new Date();
		this.zjywlx = zjywlx;
	}

	public String getYwpzhm() {
		return this.ywpzhm;
	}

	public void setYwpzhm(String ywpzhm) {
       this.updated_at = new Date();
		this.ywpzhm = ywpzhm;
	}

	public String getYhjslsh() {
		return this.yhjslsh;
	}

	public void setYhjslsh(String yhjslsh) {
       this.updated_at = new Date();
		this.yhjslsh = yhjslsh;
	}

	public BigDecimal getFse() {
		return this.fse;
	}

	public void setFse(BigDecimal fse) {
       this.updated_at = new Date();
		this.fse = fse;
	}

	public Date getJsfsrq() {
		return this.jsfsrq;
	}

	public void setJsfsrq(Date jsfsrq) {
       this.updated_at = new Date();
		this.jsfsrq = jsfsrq;
	}

	public String getJsyhdm() {
		return this.jsyhdm;
	}

	public void setJsyhdm(String jsyhdm) {
       this.updated_at = new Date();
		this.jsyhdm = jsyhdm;
	}

	public String getFkyhdm() {
		return this.fkyhdm;
	}

	public void setFkyhdm(String fkyhdm) {
       this.updated_at = new Date();
		this.fkyhdm = fkyhdm;
	}

	public String getFkzhhm() {
		return this.fkzhhm;
	}

	public void setFkzhhm(String fkzhhm) {
       this.updated_at = new Date();
		this.fkzhhm = fkzhhm;
	}

	public String getFkzhmc() {
		return this.fkzhmc;
	}

	public void setFkzhmc(String fkzhmc) {
       this.updated_at = new Date();
		this.fkzhmc = fkzhmc;
	}

	public String getSkyhdm() {
		return this.skyhdm;
	}

	public void setSkyhdm(String skyhdm) {
       this.updated_at = new Date();
		this.skyhdm = skyhdm;
	}

	public String getSkzhhm() {
		return this.skzhhm;
	}

	public void setSkzhhm(String skzhhm) {
       this.updated_at = new Date();
		this.skzhhm = skzhhm;
	}

	public String getSkzhmc() {
		return this.skzhmc;
	}

	public void setSkzhmc(String skzhmc) {
       this.updated_at = new Date();
		this.skzhmc = skzhmc;
	}

	public String getZhaiYao() {
		return this.zhaiYao;
	}

	public void setZhaiYao(String zhaiYao) {
       this.updated_at = new Date();
		this.zhaiYao = zhaiYao;
	}

}

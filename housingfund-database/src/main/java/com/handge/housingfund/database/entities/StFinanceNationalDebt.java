package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_finance_national_debt")
@org.hibernate.annotations.Table(appliesTo = "st_finance_national_debt", comment = "国债明细信息 表8.0.8")

public class StFinanceNationalDebt extends Common implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 566701018757239896L;
	@Column(name = "GZBH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '国债编号'")
	private String gzbh;
	@Column(name = "GZZL", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '国债种类'")
	private String gzzl;
	@Column(name = "GMQD", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '购买渠道'")
	private String gmqd;
	@Column(name = "GZPZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '国债凭证号'")
	private String gzpzh;
	@Column(name = "GZMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '国债名称'")
	private String gzmc;
	@Column(name = "STYHDM", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '受托银行代码'")
	private String styhdm;
	@Column(name = "YHZHHM", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '银行专户号码'")
	private String yhzhhm;
	@Column(name = "LiLv", columnDefinition = "NUMERIC(8,7) DEFAULT NULL COMMENT '利率'")
	private BigDecimal liLv = BigDecimal.ZERO;
	@Column(name = "GMJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '购买金额'")
	private BigDecimal gmje = BigDecimal.ZERO;
	@Column(name = "MianZhi", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '面值'")
	private BigDecimal mianZhi = BigDecimal.ZERO;
	@Column(name = "ShuLiang", columnDefinition = "NUMERIC(8,0) DEFAULT NULL COMMENT '数量'")
	private BigDecimal shuLiang = BigDecimal.ZERO;
	@Column(name = "GMRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '购买日期'")
	private Date gmrq;
	@Column(name = "QXRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '起息日期'")
	private Date qxrq;
	@Column(name = "DQRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '到期日期'")
	private Date dqrq;
	@Column(name = "QiXian", columnDefinition = "NUMERIC(2,0) DEFAULT NULL COMMENT '期限'")
	private BigDecimal qiXian = BigDecimal.ZERO;
	@Column(name = "DFBJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '兑付本金'")
	private BigDecimal dfbj = BigDecimal.ZERO;
	@Column(name = "DFRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '兑付日期'")
	private Date dfrq;
	@Column(name = "LXSR", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '利息收入'")
	private BigDecimal lxsr = BigDecimal.ZERO;

	public StFinanceNationalDebt() {
		super();

	}

	public StFinanceNationalDebt(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String gzbh, String gzzl, String gmqd, String gzpzh, String gzmc, String styhdm, String yhzhhm,
			BigDecimal liLv, BigDecimal gmje, BigDecimal mianZhi, BigDecimal shuLiang, Date gmrq, Date qxrq, Date dqrq,
			BigDecimal qiXian, BigDecimal dfbj, Date dfrq, BigDecimal lxsr) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.gzbh = gzbh;
		this.gzzl = gzzl;
		this.gmqd = gmqd;
		this.gzpzh = gzpzh;
		this.gzmc = gzmc;
		this.styhdm = styhdm;
		this.yhzhhm = yhzhhm;
		this.liLv = liLv;
		this.gmje = gmje;
		this.mianZhi = mianZhi;
		this.shuLiang = shuLiang;
		this.gmrq = gmrq;
		this.qxrq = qxrq;
		this.dqrq = dqrq;
		this.qiXian = qiXian;
		this.dfbj = dfbj;
		this.dfrq = dfrq;
		this.lxsr = lxsr;
	}

	public String getGzbh() {
		return this.gzbh;
	}

	public void setGzbh(String gzbh) {
		this.updated_at = new Date();
		this.gzbh = gzbh;
	}

	public String getGzzl() {
		return this.gzzl;
	}

	public void setGzzl(String gzzl) {
		this.updated_at = new Date();
		this.gzzl = gzzl;
	}

	public String getGmqd() {
		return this.gmqd;
	}

	public void setGmqd(String gmqd) {
		this.updated_at = new Date();
		this.gmqd = gmqd;
	}

	public String getGzpzh() {
		return this.gzpzh;
	}

	public void setGzpzh(String gzpzh) {
		this.updated_at = new Date();
		this.gzpzh = gzpzh;
	}

	public String getGzmc() {
		return this.gzmc;
	}

	public void setGzmc(String gzmc) {
		this.updated_at = new Date();
		this.gzmc = gzmc;
	}

	public String getStyhdm() {
		return this.styhdm;
	}

	public void setStyhdm(String styhdm) {
		this.updated_at = new Date();
		this.styhdm = styhdm;
	}

	public String getYhzhhm() {
		return this.yhzhhm;
	}

	public void setYhzhhm(String yhzhhm) {
		this.updated_at = new Date();
		this.yhzhhm = yhzhhm;
	}

	public BigDecimal getLiLv() {
		return this.liLv;
	}

	public void setLiLv(BigDecimal liLv) {
		this.updated_at = new Date();
		this.liLv = liLv;
	}

	public BigDecimal getGmje() {
		return this.gmje;
	}

	public void setGmje(BigDecimal gmje) {
		this.updated_at = new Date();
		this.gmje = gmje;
	}

	public BigDecimal getMianZhi() {
		return this.mianZhi;
	}

	public void setMianZhi(BigDecimal mianZhi) {
		this.updated_at = new Date();
		this.mianZhi = mianZhi;
	}

	public BigDecimal getShuLiang() {
		return this.shuLiang;
	}

	public void setShuLiang(BigDecimal shuLiang) {
		this.updated_at = new Date();
		this.shuLiang = shuLiang;
	}

	public Date getGmrq() {
		return this.gmrq;
	}

	public void setGmrq(Date gmrq) {
		this.updated_at = new Date();
		this.gmrq = gmrq;
	}

	public Date getQxrq() {
		return this.qxrq;
	}

	public void setQxrq(Date qxrq) {
		this.updated_at = new Date();
		this.qxrq = qxrq;
	}

	public Date getDqrq() {
		return this.dqrq;
	}

	public void setDqrq(Date dqrq) {
		this.updated_at = new Date();
		this.dqrq = dqrq;
	}

	public BigDecimal getQiXian() {
		return this.qiXian;
	}

	public void setQiXian(BigDecimal qiXian) {
		this.updated_at = new Date();
		this.qiXian = qiXian;
	}

	public BigDecimal getDfbj() {
		return this.dfbj;
	}

	public void setDfbj(BigDecimal dfbj) {
		this.updated_at = new Date();
		this.dfbj = dfbj;
	}

	public Date getDfrq() {
		return this.dfrq;
	}

	public void setDfrq(Date dfrq) {
		this.updated_at = new Date();
		this.dfrq = dfrq;
	}

	public BigDecimal getLxsr() {
		return this.lxsr;
	}

	public void setLxsr(BigDecimal lxsr) {
		this.updated_at = new Date();
		this.lxsr = lxsr;
	}

}

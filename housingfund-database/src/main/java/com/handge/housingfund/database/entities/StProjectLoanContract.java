package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_project_loan_contract")
@org.hibernate.annotations.Table(appliesTo = "st_project_loan_contract", comment = "项目贷款借款合同信息 表7.0.3")
public class StProjectLoanContract extends Common implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7370000644905991706L;
	@Column(name = "JKHTBH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '借款合同编号'")
	private String jkhtbh;
	@Column(name = "XMBH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '项目编号'")
	private String xmbh;
	@Column(name = "DKJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '贷款金额'")
	private BigDecimal dkje = BigDecimal.ZERO;
	@Column(name = "DKQX", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '贷款期限'")
	private BigDecimal dkqx = BigDecimal.ZERO;
	@Column(name = "DKLL", columnDefinition = "NUMERIC(8,7) DEFAULT NULL COMMENT '贷款利率'")
	private BigDecimal dkll = BigDecimal.ZERO;
	@Column(name = "YQFXLL", columnDefinition = "NUMERIC(8,7) DEFAULT NULL COMMENT '逾期罚息利率'")
	private BigDecimal yqfxll = BigDecimal.ZERO;
	@Column(name = "NYFXLL", columnDefinition = "NUMERIC(8,7) DEFAULT NULL COMMENT '挪用罚息利率'")
	private BigDecimal nyfxll = BigDecimal.ZERO;
	@Column(name = "ZJJGZHHM", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '资金监管账户号码'")
	private String zjjgzhhm;
	@Column(name = "YHDM", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '银行代码'")
	private String yhdm;
	@Column(name = "DKFFFS", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '贷款发放方式'")
	private String dkfffs;
	@Column(name = "DKHBFS", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '贷款还本方式'")
	private String dkhbfs;
	@Column(name = "WTR", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '委托人'")
	private String wtr;
	@Column(name = "WTRQYDB", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '委托人签约代表'")
	private String wtrqydb;
	@Column(name = "WTRQYSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '委托人签约时间'")
	private Date wtrqysj;
	@Column(name = "DKR", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '贷款人'")
	private String dkr;
	@Column(name = "DKRQYDB", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '贷款人签约代表'")
	private String dkrqydb;
	@Column(name = "DKRQYSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '贷款人签约时间'")
	private Date dkrqysj;
	@Column(name = "JKR", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '借款人'")
	private String jkr;
	@Column(name = "JKRQYDB", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '借款人签约代表'")
	private String jkrqydb;
	@Column(name = "JKRQYSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '借款人签约时间'")
	private Date jkrqysj;

	public StProjectLoanContract() {
		super();

	}

	public StProjectLoanContract(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String jkhtbh, String xmbh, BigDecimal dkje, BigDecimal dkqx, BigDecimal dkll, BigDecimal yqfxll,
			BigDecimal nyfxll, String zjjgzhhm, String yhdm, String dkfffs, String dkhbfs, String wtr, String wtrqydb,
			Date wtrqysj, String dkr, String dkrqydb, Date dkrqysj, String jkr, String jkrqydb, Date jkrqysj) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.jkhtbh = jkhtbh;
		this.xmbh = xmbh;
		this.dkje = dkje;
		this.dkqx = dkqx;
		this.dkll = dkll;
		this.yqfxll = yqfxll;
		this.nyfxll = nyfxll;
		this.zjjgzhhm = zjjgzhhm;
		this.yhdm = yhdm;
		this.dkfffs = dkfffs;
		this.dkhbfs = dkhbfs;
		this.wtr = wtr;
		this.wtrqydb = wtrqydb;
		this.wtrqysj = wtrqysj;
		this.dkr = dkr;
		this.dkrqydb = dkrqydb;
		this.dkrqysj = dkrqysj;
		this.jkr = jkr;
		this.jkrqydb = jkrqydb;
		this.jkrqysj = jkrqysj;
	}

	public String getJkhtbh() {
		return this.jkhtbh;
	}

	public void setJkhtbh(String jkhtbh) {
		this.updated_at = new Date();
		this.jkhtbh = jkhtbh;
	}

	public String getXmbh() {
		return this.xmbh;
	}

	public void setXmbh(String xmbh) {
		this.updated_at = new Date();
		this.xmbh = xmbh;
	}

	public BigDecimal getDkje() {
		return this.dkje;
	}

	public void setDkje(BigDecimal dkje) {
		this.updated_at = new Date();
		this.dkje = dkje;
	}

	public BigDecimal getDkqx() {
		return this.dkqx;
	}

	public void setDkqx(BigDecimal dkqx) {
		this.updated_at = new Date();
		this.dkqx = dkqx;
	}

	public BigDecimal getDkll() {
		return this.dkll;
	}

	public void setDkll(BigDecimal dkll) {
		this.updated_at = new Date();
		this.dkll = dkll;
	}

	public BigDecimal getYqfxll() {
		return this.yqfxll;
	}

	public void setYqfxll(BigDecimal yqfxll) {
		this.updated_at = new Date();
		this.yqfxll = yqfxll;
	}

	public BigDecimal getNyfxll() {
		return this.nyfxll;
	}

	public void setNyfxll(BigDecimal nyfxll) {
		this.updated_at = new Date();
		this.nyfxll = nyfxll;
	}

	public String getZjjgzhhm() {
		return this.zjjgzhhm;
	}

	public void setZjjgzhhm(String zjjgzhhm) {
		this.updated_at = new Date();
		this.zjjgzhhm = zjjgzhhm;
	}

	public String getYhdm() {
		return this.yhdm;
	}

	public void setYhdm(String yhdm) {
		this.updated_at = new Date();
		this.yhdm = yhdm;
	}

	public String getDkfffs() {
		return this.dkfffs;
	}

	public void setDkfffs(String dkfffs) {
		this.updated_at = new Date();
		this.dkfffs = dkfffs;
	}

	public String getDkhbfs() {
		return this.dkhbfs;
	}

	public void setDkhbfs(String dkhbfs) {
		this.updated_at = new Date();
		this.dkhbfs = dkhbfs;
	}

	public String getWtr() {
		return this.wtr;
	}

	public void setWtr(String wtr) {
		this.updated_at = new Date();
		this.wtr = wtr;
	}

	public String getWtrqydb() {
		return this.wtrqydb;
	}

	public void setWtrqydb(String wtrqydb) {
		this.updated_at = new Date();
		this.wtrqydb = wtrqydb;
	}

	public Date getWtrqysj() {
		return this.wtrqysj;
	}

	public void setWtrqysj(Date wtrqysj) {
		this.updated_at = new Date();
		this.wtrqysj = wtrqysj;
	}

	public String getDkr() {
		return this.dkr;
	}

	public void setDkr(String dkr) {
		this.updated_at = new Date();
		this.dkr = dkr;
	}

	public String getDkrqydb() {
		return this.dkrqydb;
	}

	public void setDkrqydb(String dkrqydb) {
		this.updated_at = new Date();
		this.dkrqydb = dkrqydb;
	}

	public Date getDkrqysj() {
		return this.dkrqysj;
	}

	public void setDkrqysj(Date dkrqysj) {
		this.updated_at = new Date();
		this.dkrqysj = dkrqysj;
	}

	public String getJkr() {
		return this.jkr;
	}

	public void setJkr(String jkr) {
		this.updated_at = new Date();
		this.jkr = jkr;
	}

	public String getJkrqydb() {
		return this.jkrqydb;
	}

	public void setJkrqydb(String jkrqydb) {
		this.updated_at = new Date();
		this.jkrqydb = jkrqydb;
	}

	public Date getJkrqysj() {
		return this.jkrqysj;
	}

	public void setJkrqysj(Date jkrqysj) {
		this.updated_at = new Date();
		this.jkrqysj = jkrqysj;
	}

}

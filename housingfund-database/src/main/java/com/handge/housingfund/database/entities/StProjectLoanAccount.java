package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_project_loan_account")
@org.hibernate.annotations.Table(appliesTo = "st_project_loan_account", comment = "项目贷款账户信息 表7.0.5")
public class StProjectLoanAccount extends Common implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6998017411798360091L;
	@Column(name = "DKZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '贷款账号'")
	private String dkzh;
	@Column(name = "JKHTBH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '借款合同编号'")
	private String jkhtbh;
	@Column(name = "DKYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '贷款余额'")
	private BigDecimal dkye = BigDecimal.ZERO;
	@Column(name = "DKLL", columnDefinition = "NUMERIC(8,7) DEFAULT NULL COMMENT '贷款利率'")
	private BigDecimal dkll = BigDecimal.ZERO;
	@Column(name = "YQFXLL", columnDefinition = "NUMERIC(8,7) DEFAULT NULL COMMENT '逾期罚息利率'")
	private BigDecimal yqfxll = BigDecimal.ZERO;
	@Column(name = "NYFXLL", columnDefinition = "NUMERIC(8,7) DEFAULT NULL COMMENT '挪用罚息利率'")
	private BigDecimal nyfxll = BigDecimal.ZERO;
	@Column(name = "DKFXDJ", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '贷款风险等级'")
	private String dkfxdj;
	@Column(name = "DKJQRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '贷款结清日期'")
	private Date dkjqrq;
	@Column(name = "HSBJZE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '回收本金总额'")
	private BigDecimal hsbjze = BigDecimal.ZERO;
	@Column(name = "HSLXZE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '回收利息总额'")
	private BigDecimal hslxze = BigDecimal.ZERO;
	@Column(name = "FXZE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '罚息总额'")
	private BigDecimal fxze = BigDecimal.ZERO;
	@Column(name = "TQHKBJZE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '提前还款本金总额'")
	private BigDecimal tqhkbjze = BigDecimal.ZERO;
	@Column(name = "YQBJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '逾期本金'")
	private BigDecimal yqbj = BigDecimal.ZERO;
	@Column(name = "YQLX", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '逾期利息'")
	private BigDecimal yqlx = BigDecimal.ZERO;

	public StProjectLoanAccount(){
       super();


	}

	public StProjectLoanAccount(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String dkzh, String jkhtbh, BigDecimal dkye, BigDecimal dkll, BigDecimal yqfxll, BigDecimal nyfxll,
			String dkfxdj, Date dkjqrq, BigDecimal hsbjze, BigDecimal hslxze, BigDecimal fxze, BigDecimal tqhkbjze,
			BigDecimal yqbj, BigDecimal yqlx) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.dkzh = dkzh;
		this.jkhtbh = jkhtbh;
		this.dkye = dkye;
		this.dkll = dkll;
		this.yqfxll = yqfxll;
		this.nyfxll = nyfxll;
		this.dkfxdj = dkfxdj;
		this.dkjqrq = dkjqrq;
		this.hsbjze = hsbjze;
		this.hslxze = hslxze;
		this.fxze = fxze;
		this.tqhkbjze = tqhkbjze;
		this.yqbj = yqbj;
		this.yqlx = yqlx;
	}

	public String getDkzh() {
		return this.dkzh;
	}

	public void setDkzh(String dkzh) {
       this.updated_at = new Date();
		this.dkzh = dkzh;
	}

	public String getJkhtbh() {
		return this.jkhtbh;
	}

	public void setJkhtbh(String jkhtbh) {
       this.updated_at = new Date();
		this.jkhtbh = jkhtbh;
	}

	public BigDecimal getDkye() {
		return this.dkye;
	}

	public void setDkye(BigDecimal dkye) {
       this.updated_at = new Date();
		this.dkye = dkye;
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

	public String getDkfxdj() {
		return this.dkfxdj;
	}

	public void setDkfxdj(String dkfxdj) {
       this.updated_at = new Date();
		this.dkfxdj = dkfxdj;
	}

	public Date getDkjqrq() {
		return this.dkjqrq;
	}

	public void setDkjqrq(Date dkjqrq) {
       this.updated_at = new Date();
		this.dkjqrq = dkjqrq;
	}

	public BigDecimal getHsbjze() {
		return this.hsbjze;
	}

	public void setHsbjze(BigDecimal hsbjze) {
       this.updated_at = new Date();
		this.hsbjze = hsbjze;
	}

	public BigDecimal getHslxze() {
		return this.hslxze;
	}

	public void setHslxze(BigDecimal hslxze) {
       this.updated_at = new Date();
		this.hslxze = hslxze;
	}

	public BigDecimal getFxze() {
		return this.fxze;
	}

	public void setFxze(BigDecimal fxze) {
       this.updated_at = new Date();
		this.fxze = fxze;
	}

	public BigDecimal getTqhkbjze() {
		return this.tqhkbjze;
	}

	public void setTqhkbjze(BigDecimal tqhkbjze) {
       this.updated_at = new Date();
		this.tqhkbjze = tqhkbjze;
	}

	public BigDecimal getYqbj() {
		return this.yqbj;
	}

	public void setYqbj(BigDecimal yqbj) {
       this.updated_at = new Date();
		this.yqbj = yqbj;
	}

	public BigDecimal getYqlx() {
		return this.yqlx;
	}

	public void setYqlx(BigDecimal yqlx) {
       this.updated_at = new Date();
		this.yqlx = yqlx;
	}

}

package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_collection_personal_account", indexes = {
		@Index(name = "INDEX_GRZH", columnList = "GRZH", unique = true) })
@org.hibernate.annotations.Table(appliesTo = "st_collection_personal_account", comment = "个人账户信息 表5.0.4")
public class StCollectionPersonalAccount extends Common implements java.io.Serializable,Comparable<StCollectionPersonalAccount> {
	private static final long serialVersionUID = -2597449812583778864L;
	@Column(name = "GRZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '个人账号'")
	private String grzh;
	@Column(name = "GRJCJS", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '个人缴存基数'")
	private BigDecimal grjcjs = BigDecimal.ZERO;
	@Column(name = "GRZHZT", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '个人账户状态'")
	private String grzhzt;
	@Column(name = "KHRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '开户日期'")
	private Date khrq;
	@Column(name = "GRZHYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '个人账户余额'")
	private BigDecimal grzhye = BigDecimal.ZERO;
	@Column(name = "GRZHSNJZYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '个人账户上年结转余额'")
	private BigDecimal grzhsnjzye = BigDecimal.ZERO;
	@Column(name = "GRZHDNGJYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '个人账户当年归集余额'")
	private BigDecimal grzhdngjye = BigDecimal.ZERO;
	@Column(name = "GRYJCE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '个人月缴存额'")
	private BigDecimal gryjce = BigDecimal.ZERO;
	@Column(name = "DWYJCE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '单位月缴存额'")
	private BigDecimal dwyjce = BigDecimal.ZERO;
	@Column(name = "XHRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '销户日期'")
	private Date xhrq;
	@Column(name = "XHYY", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '销户原因'")
	private String xhyy;
	@Column(name = "GRCKZHHM", length = 30, columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '个人存款账户号码'")
	private String grckzhhm;
	@Column(name = "GRCKZHKHYHMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '个人存款账户开户银行名称'")
	private String grckzhkhyhmc;
	@Column(name = "GRCKZHKHYHDM", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '个人存款账户开户银行代码'")
	private String grckzhkhyhdm;

	public StCollectionPersonalAccount(){
       super();


	}

	public StCollectionPersonalAccount(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String grzh, BigDecimal grjcjs, String grzhzt, Date khrq, BigDecimal grzhye, BigDecimal grzhsnjzye,
			BigDecimal grzhdngjye, BigDecimal gryjce, BigDecimal dwyjce, Date xhrq, String xhyy, String grckzhhm,
			String grckzhkhyhmc, String grckzhkhyhdm) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.grzh = grzh;
		this.grjcjs = grjcjs;
		this.grzhzt = grzhzt;
		this.khrq = khrq;
		this.grzhye = grzhye;
		this.grzhsnjzye = grzhsnjzye;
		this.grzhdngjye = grzhdngjye;
		this.gryjce = gryjce;
		this.dwyjce = dwyjce;
		this.xhrq = xhrq;
		this.xhyy = xhyy;
		this.grckzhhm = grckzhhm;
		this.grckzhkhyhmc = grckzhkhyhmc;
		this.grckzhkhyhdm = grckzhkhyhdm;
	}

	public String getGrzh() {
		return this.grzh;
	}

	public void setGrzh(String grzh) {
       this.updated_at = new Date();
		this.grzh = grzh;
	}

	public BigDecimal getGrjcjs() {
		return this.grjcjs;
	}

	public void setGrjcjs(BigDecimal grjcjs) {
       this.updated_at = new Date();
		this.grjcjs = grjcjs;
	}

	public String getGrzhzt() {
		return this.grzhzt;
	}

	public void setGrzhzt(String grzhzt) {
       this.updated_at = new Date();
		this.grzhzt = grzhzt;
	}

	public Date getKhrq() {
		return this.khrq;
	}

	public void setKhrq(Date khrq) {
       this.updated_at = new Date();
		this.khrq = khrq;
	}

	public BigDecimal getGrzhye() {
		return this.grzhye;
	}

	public void setGrzhye(BigDecimal grzhye) {
       this.updated_at = new Date();
		this.grzhye = grzhye;
	}

	public BigDecimal getGrzhsnjzye() {
		return this.grzhsnjzye;
	}

	public void setGrzhsnjzye(BigDecimal grzhsnjzye) {
       this.updated_at = new Date();
		this.grzhsnjzye = grzhsnjzye;
	}

	public BigDecimal getGrzhdngjye() {
		return this.grzhdngjye;
	}

	public void setGrzhdngjye(BigDecimal grzhdngjye) {
       this.updated_at = new Date();
		this.grzhdngjye = grzhdngjye;
	}

	public BigDecimal getGryjce() {
		return this.gryjce;
	}

	public void setGryjce(BigDecimal gryjce) {
       this.updated_at = new Date();
		this.gryjce = gryjce;
	}

	public BigDecimal getDwyjce() {
		return this.dwyjce;
	}

	public void setDwyjce(BigDecimal dwyjce) {
       this.updated_at = new Date();
		this.dwyjce = dwyjce;
	}

	public Date getXhrq() {
		return this.xhrq;
	}

	public void setXhrq(Date xhrq) {
       this.updated_at = new Date();
		this.xhrq = xhrq;
	}

	public String getXhyy() {
		return this.xhyy;
	}

	public void setXhyy(String xhyy) {
       this.updated_at = new Date();
		this.xhyy = xhyy;
	}

	public String getGrckzhhm() {
		return this.grckzhhm;
	}

	public void setGrckzhhm(String grckzhhm) {
       this.updated_at = new Date();
		this.grckzhhm = grckzhhm;
	}

	public String getGrckzhkhyhmc() {
		return this.grckzhkhyhmc;
	}

	public void setGrckzhkhyhmc(String grckzhkhyhmc) {
       this.updated_at = new Date();
		this.grckzhkhyhmc = grckzhkhyhmc;
	}

	public String getGrckzhkhyhdm() {
		return this.grckzhkhyhdm;
	}

	public void setGrckzhkhyhdm(String grckzhkhyhdm) {
       this.updated_at = new Date();
		this.grckzhkhyhdm = grckzhkhyhdm;
	}

	@Override
	public int compareTo(StCollectionPersonalAccount o) {
		return this.getGrzh().compareTo(o.getGrzh());
	}
}

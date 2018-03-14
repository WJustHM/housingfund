package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_housing_coborrower")
@org.hibernate.annotations.Table(appliesTo = "st_housing_coborrower", comment = "共同借款人信息 表6.0.4")
public class StHousingCoborrower extends Common implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3481739467016593826L;
	@Column(name = "GTJKRGJJZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '共同借款人公积金账号'")
	private String gtjkrgjjzh;
	@Column(name = "JKHTBH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '借款合同编号'")
	private String jkhtbh;
	@Column(name = "GTJKRXM", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '共同借款人姓名'")
	private String gtjkrxm;
	@Column(name = "GTJKRZJLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '共同借款人证件类型'")
	private String gtjkrzjlx;
	@Column(name = "GTJKRZJHM", columnDefinition = "VARCHAR(18) DEFAULT NULL COMMENT '共同借款人证件号码'")
	private String gtjkrzjhm;
	@Column(name = "YSR", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '月收入'")
	private BigDecimal ysr = BigDecimal.ZERO;
	@Column(name = "CDGX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '参贷关系'")
	private String cdgx;
	@Column(name = "GDDHHM", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '固定电话号码'")
	private String gddhhm;
	@Column(name = "SJHM", columnDefinition = "VARCHAR(11) DEFAULT NULL COMMENT '手机号码'")
	private String sjhm;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "extenstion", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '共同借款人拓展表'")
	private CLoanHousingCoborrowerExtension extension;

	public StHousingCoborrower() {
		super();

	}

	public StHousingCoborrower(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String gtjkrgjjzh, String jkhtbh, String gtjkrxm, String gtjkrzjlx, String gtjkrzjhm, BigDecimal ysr,
			String cdgx, String gddhhm, String sjhm, CLoanHousingCoborrowerExtension extension) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.gtjkrgjjzh = gtjkrgjjzh;
		this.jkhtbh = jkhtbh;
		this.gtjkrxm = gtjkrxm;
		this.gtjkrzjlx = gtjkrzjlx;
		this.gtjkrzjhm = gtjkrzjhm;
		this.ysr = ysr;
		this.cdgx = cdgx;
		this.gddhhm = gddhhm;
		this.sjhm = sjhm;
		this.extension = extension;
	}

	public CLoanHousingCoborrowerExtension getExtension() {
		return extension;
	}

	public void setcExtension(CLoanHousingCoborrowerExtension cLoanHousingCoborrowerExtension) {
		this.updated_at = new Date();
		this.extension = cLoanHousingCoborrowerExtension;
	}

	public String getGtjkrgjjzh() {
		return this.gtjkrgjjzh;
	}

	public void setGtjkrgjjzh(String gtjkrgjjzh) {
		this.updated_at = new Date();
		this.gtjkrgjjzh = gtjkrgjjzh;
	}

	public String getJkhtbh() {
		return this.jkhtbh;
	}

	public void setJkhtbh(String jkhtbh) {
		this.updated_at = new Date();
		this.jkhtbh = jkhtbh;
	}

	public String getGtjkrxm() {
		return this.gtjkrxm;
	}

	public void setGtjkrxm(String gtjkrxm) {
		this.updated_at = new Date();
		this.gtjkrxm = gtjkrxm;
	}

	public String getGtjkrzjlx() {
		return this.gtjkrzjlx;
	}

	public void setGtjkrzjlx(String gtjkrzjlx) {
		this.updated_at = new Date();
		this.gtjkrzjlx = gtjkrzjlx;
	}

	public String getGtjkrzjhm() {
		return this.gtjkrzjhm;
	}

	public void setGtjkrzjhm(String gtjkrzjhm) {
		this.updated_at = new Date();
		this.gtjkrzjhm = gtjkrzjhm;
	}

	public BigDecimal getYsr() {
		return this.ysr;
	}

	public void setYsr(BigDecimal ysr) {
		this.updated_at = new Date();
		this.ysr = ysr;
	}

	public String getCdgx() {
		return this.cdgx;
	}

	public void setCdgx(String cdgx) {
		this.updated_at = new Date();
		this.cdgx = cdgx;
	}

	public String getGddhhm() {
		return this.gddhhm;
	}

	public void setGddhhm(String gddhhm) {
		this.updated_at = new Date();
		this.gddhhm = gddhhm;
	}

	public String getSjhm() {
		return this.sjhm;
	}

	public void setSjhm(String sjhm) {
		this.updated_at = new Date();
		this.sjhm = sjhm;
	}

}

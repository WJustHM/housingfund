package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Funnyboy on 2017/8/29.
 */
@Entity
@Table(name = "c_housing_business_details_extension")
@org.hibernate.annotations.Table(appliesTo = "c_housing_business_details_extension", comment = "个人贷款业务明细拓展")
public class CHousingBusinessDetailsExtension extends Common implements java.io.Serializable {
	private static final long serialVersionUID = 827443890293623330L;

	@Column(name = "YWZT", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务状态'")
	private String ywzt;
	@Column(name = "SBYY", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '失败原因'")
	private String sbyy;
	@Column(name = "RGCL", columnDefinition = "VARCHAR(1) DEFAULT NULL COMMENT '是否人工处理 0:是 1:否'")
	private String rgcl;
	@Column(name = "XQFSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '下期发生额'")
	private BigDecimal xqfse = BigDecimal.ZERO;
	@Column(name = "XQBJJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '下期本金金额'")
	private BigDecimal xqbjje = BigDecimal.ZERO;
	@Column(name = "XQLXJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '下期利息金额'")
	private BigDecimal xqlxje = BigDecimal.ZERO;
	@Column(name = "XQDKYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '下期贷款余额'")
	private BigDecimal xqdkye = BigDecimal.ZERO;
	@Column(name = "JKRXM", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '借款人姓名'")
	private String jkrxm;
	@Column(name = "HKZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '还款账号'")
	private String hkzh;
	@Column(name = "ZHKHYHMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '账户开户银行名称'")
	private String zhkhyhmc;
	@Column(name = "ZHKHYHDM", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '账户开户银行代码'")
	private String zhkhyhdm;
	@Column(name = "YWWD", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '业务网点'")
	private String ywwd;
	@Column(name = "PCH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '批次号'")
	private String pch;
	@Column(name = "HKXH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '还款序号'")
	private String hkxh;



	public CHousingBusinessDetailsExtension() {
		super();
	}

	public CHousingBusinessDetailsExtension(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
											String ywzt, String sbyy, String rgcl, BigDecimal xqfse, BigDecimal xqbjje,
											BigDecimal xqlxje, BigDecimal xqdkye, String jkrxm, String hkzh,String zhkhyhmc,String zhkhyhdm,String ywwd,String pch,String hkxh) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.ywzt = ywzt;
		this.sbyy = sbyy;
		this.rgcl = rgcl;
		this.xqfse = xqfse;
		this.xqbjje = xqbjje;
		this.xqlxje = xqlxje;
		this.xqdkye = xqdkye;
		this.jkrxm = jkrxm;
		this.hkzh = hkzh;
		this.zhkhyhmc=zhkhyhmc;
		this.zhkhyhdm=zhkhyhdm;
		this.ywwd=ywwd;
		this.pch=pch;
		this.hkxh=hkxh;
	}

	public String getRgcl() {
		return rgcl;
	}

	public void setRgcl(String rgcl) {
		this.rgcl = rgcl;
	}

	public String getJkrxm() {
		return jkrxm;
	}

	public void setJkrxm(String jkrxm) {
		this.jkrxm = jkrxm;
	}

	public String getYwzt() {
		return ywzt;
	}

	public void setYwzt(String ywzt) {
		this.ywzt = ywzt;
	}

	public String getSbyy() {
		return sbyy;
	}

	public void setSbyy(String sbyy) {
		this.sbyy = sbyy;
	}

	public BigDecimal getXqfse() {
		return xqfse;
	}

	public void setXqfse(BigDecimal xqfse) {
		this.xqfse = xqfse;
	}

	public BigDecimal getXqbjje() {
		return xqbjje;
	}

	public void setXqbjje(BigDecimal xqbjje) {
		this.xqbjje = xqbjje;
	}

	public BigDecimal getXqlxje() {
		return xqlxje;
	}

	public void setXqlxje(BigDecimal xqlxje) {
		this.xqlxje = xqlxje;
	}

	public BigDecimal getXqdkye() {
		return xqdkye;
	}

	public void setXqdkye(BigDecimal xqdkye) {
		this.xqdkye = xqdkye;
	}

	public String getHkzh() {
		return hkzh;
	}

	public void setHkzh(String hkzh) {
		this.hkzh = hkzh;
	}

	public String getZhkhyhmc() {
		return zhkhyhmc;
	}

	public void setZhkhyhmc(String zhkhyhmc) {
		this.zhkhyhmc = zhkhyhmc;
	}

	public String getZhkhyhdm() {
		return zhkhyhdm;
	}

	public void setZhkhyhdm(String zhkhyhdm) {
		this.zhkhyhdm = zhkhyhdm;
	}

	public String getYwwd() {
		return ywwd;
	}

	public void setYwwd(String ywwd) {
		this.ywwd = ywwd;
	}

	public String getPch() {
		return pch;
	}

	public void setPch(String pch) {
		this.pch = pch;
	}


	public String getHkxh() {
		return hkxh;
	}

	public void setHkxh(String hkxh) {
		this.hkxh = hkxh;
	}
}

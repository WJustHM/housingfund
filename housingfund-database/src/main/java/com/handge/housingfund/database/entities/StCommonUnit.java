package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "st_common_unit")
@org.hibernate.annotations.Table(appliesTo = "st_common_unit", comment = "缴存单位信息 表4.0.2")
public class StCommonUnit extends Common implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8395175647952592535L;
	@Column(name = "DWMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '单位名称'")
	private String dwmc;
	@Column(name = "DWZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '单位账号'")
	private String dwzh;
	@Column(name = "DWDZ", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '单位地址'")
	private String dwdz;
	@Column(name = "DWFRDBXM", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '单位法人代表姓名'")
	private String dwfrdbxm;
	@Column(name = "DWFRDBZJLX", columnDefinition = "VARCHAR(2) DEFAULT '1' COMMENT '单位法人代表证件类型'")
	private String dwfrdbzjlx;
	@Column(name = "DWFRDBZJHM", columnDefinition = "VARCHAR(18) DEFAULT NULL COMMENT '单位法人代表证件号码'")
	private String dwfrdbzjhm;
	@Column(name = "DWLSGX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '单位隶属关系'")
	private String dwlsgx;
	@Column(name = "DWJJLX", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '单位经济类型'")
	private String dwjjlx;
	@Column(name = "DWSSHY", columnDefinition = "VARCHAR(4) DEFAULT NULL COMMENT '单位所属行业'")
	private String dwsshy;
	@Column(name = "DWYB", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '单位邮编'")
	private String dwyb;
	@Column(name = "DWDZXX", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '单位电子信箱'")
	private String dwdzxx;
	@Column(name = "DWFXR", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '单位发薪日'")
	private String dwfxr;
	@Column(name = "JBRXM", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '经办人姓名'")
	private String jbrxm;
	@Column(name = "JBRGDDHHM", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '经办人固定电话号码'")
	private String jbrgddhhm;
	@Column(name = "JBRSJHM", columnDefinition = "VARCHAR(11) DEFAULT NULL COMMENT '经办人手机号码'")
	private String jbrsjhm;
	@Column(name = "JBRZJLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '经办人证件类型'")
	private String jbrzjlx;
	@Column(name = "JBRZJHM", columnDefinition = "VARCHAR(18) DEFAULT NULL COMMENT '经办人证件号码'")
	private String jbrzjhm;
	@Column(name = "ZZJGDM", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '组织机构代码'")
	private String zzjgdm;
	@Column(name = "DWSLRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '单位设立日期'")
	private Date dwslrq;
	@Column(name = "DWKHRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '单位开户日期'")
	private Date dwkhrq;
	@Column(name = "STYHMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '受托银行名称'")
	private String styhmc;
	@Column(name = "STYHDM", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '受托银行代码'")
	private String styhdm;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "CollectionUnitAccount", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '单位账户'")
	private StCollectionUnitAccount collectionUnitAccount;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "extension", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '缴存单位基础信息扩展'")
	private CCommonUnitExtension cCommonUnitExtension;

	public StCommonUnit() {
		super();

	}

	public StCommonUnit(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted, String dwmc,
			String dwzh, String dwdz, String dwfrdbxm, String dwfrdbzjlx, String dwfrdbzjhm, String dwlsgx,
			String dwjjlx, String dwsshy, String dwyb, String dwdzxx, String dwfxr, String jbrxm, String jbrgddhhm,
			String jbrsjhm, String jbrzjlx, String jbrzjhm, String zzjgdm, Date dwslrq, Date dwkhrq, String styhmc,
			String styhdm, StCollectionUnitAccount collectionUnitAccount, CCommonUnitExtension extension) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.dwmc = dwmc;
		this.dwzh = dwzh;
		this.dwdz = dwdz;
		this.dwfrdbxm = dwfrdbxm;
		this.dwfrdbzjlx = dwfrdbzjlx;
		this.dwfrdbzjhm = dwfrdbzjhm;
		this.dwlsgx = dwlsgx;
		this.dwjjlx = dwjjlx;
		this.dwsshy = dwsshy;
		this.dwyb = dwyb;
		this.dwdzxx = dwdzxx;
		this.dwfxr = dwfxr;
		this.jbrxm = jbrxm;
		this.jbrgddhhm = jbrgddhhm;
		this.jbrsjhm = jbrsjhm;
		this.jbrzjlx = jbrzjlx;
		this.jbrzjhm = jbrzjhm;
		this.zzjgdm = zzjgdm;
		this.dwslrq = dwslrq;
		this.dwkhrq = dwkhrq;
		this.styhmc = styhmc;
		this.styhdm = styhdm;
		this.collectionUnitAccount = collectionUnitAccount;
		this.cCommonUnitExtension = extension;
	}

	public StCollectionUnitAccount getCollectionUnitAccount() {
		return collectionUnitAccount;
	}

	public void setCollectionUnitAccount(StCollectionUnitAccount collectionUnitAccount) {
		this.updated_at = new Date();
		this.collectionUnitAccount = collectionUnitAccount;
	}

	public CCommonUnitExtension getExtension() {
		return cCommonUnitExtension;
	}

	public void setExtension(CCommonUnitExtension extension) {
		this.updated_at = new Date();
		this.cCommonUnitExtension = extension;
	}

	public String getDwmc() {
		return this.dwmc;
	}

	public void setDwmc(String dwmc) {
		this.updated_at = new Date();
		this.dwmc = dwmc;
	}

	public String getDwzh() {
		return this.dwzh;
	}

	public void setDwzh(String dwzh) {
		this.updated_at = new Date();
		this.dwzh = dwzh;
	}

	public String getDwdz() {
		return this.dwdz;
	}

	public void setDwdz(String dwdz) {
		this.updated_at = new Date();
		this.dwdz = dwdz;
	}

	public String getDwfrdbxm() {
		return this.dwfrdbxm;
	}

	public void setDwfrdbxm(String dwfrdbxm) {
		this.updated_at = new Date();
		this.dwfrdbxm = dwfrdbxm;
	}

	public String getDwfrdbzjlx() {
		return this.dwfrdbzjlx;
	}

	public void setDwfrdbzjlx(String dwfrdbzjlx) {
		this.updated_at = new Date();
		this.dwfrdbzjlx = dwfrdbzjlx;
	}

	public String getDwfrdbzjhm() {
		return this.dwfrdbzjhm;
	}

	public void setDwfrdbzjhm(String dwfrdbzjhm) {
		this.updated_at = new Date();
		this.dwfrdbzjhm = dwfrdbzjhm;
	}

	public String getDwlsgx() {
		return this.dwlsgx;
	}

	public void setDwlsgx(String dwlsgx) {
		this.updated_at = new Date();
		this.dwlsgx = dwlsgx;
	}

	public String getDwjjlx() {
		return this.dwjjlx;
	}

	public void setDwjjlx(String dwjjlx) {
		this.updated_at = new Date();
		this.dwjjlx = dwjjlx;
	}

	public String getDwsshy() {
		return this.dwsshy;
	}

	public void setDwsshy(String dwsshy) {
		this.updated_at = new Date();
		this.dwsshy = dwsshy;
	}

	public String getDwyb() {
		return this.dwyb;
	}

	public void setDwyb(String dwyb) {
		this.updated_at = new Date();
		this.dwyb = dwyb;
	}

	public String getDwdzxx() {
		return this.dwdzxx;
	}

	public void setDwdzxx(String dwdzxx) {
		this.updated_at = new Date();
		this.dwdzxx = dwdzxx;
	}

	public String getDwfxr() {
		return this.dwfxr;
	}

	public void setDwfxr(String dwfxr) {
		this.updated_at = new Date();
		this.dwfxr = dwfxr;
	}

	public String getJbrxm() {
		return this.jbrxm;
	}

	public void setJbrxm(String jbrxm) {
		this.updated_at = new Date();
		this.jbrxm = jbrxm;
	}

	public String getJbrgddhhm() {
		return this.jbrgddhhm;
	}

	public void setJbrgddhhm(String jbrgddhhm) {
		this.updated_at = new Date();
		this.jbrgddhhm = jbrgddhhm;
	}

	public String getJbrsjhm() {
		return this.jbrsjhm;
	}

	public void setJbrsjhm(String jbrsjhm) {
		this.updated_at = new Date();
		this.jbrsjhm = jbrsjhm;
	}

	public String getJbrzjlx() {
		return this.jbrzjlx;
	}

	public void setJbrzjlx(String jbrzjlx) {
		this.updated_at = new Date();
		this.jbrzjlx = jbrzjlx;
	}

	public String getJbrzjhm() {
		return this.jbrzjhm;
	}

	public void setJbrzjhm(String jbrzjhm) {
		this.updated_at = new Date();
		this.jbrzjhm = jbrzjhm;
	}

	public String getZzjgdm() {
		return this.zzjgdm;
	}

	public void setZzjgdm(String zzjgdm) {
		this.updated_at = new Date();
		this.zzjgdm = zzjgdm;
	}

	public Date getDwslrq() {
		return this.dwslrq;
	}

	public void setDwslrq(Date dwslrq) {
		this.updated_at = new Date();
		this.dwslrq = dwslrq;
	}

	public Date getDwkhrq() {
		return this.dwkhrq;
	}

	public void setDwkhrq(Date dwkhrq) {
		this.updated_at = new Date();
		this.dwkhrq = dwkhrq;
	}

	public String getStyhmc() {
		return this.styhmc;
	}

	public void setStyhmc(String styhmc) {
		this.updated_at = new Date();
		this.styhmc = styhmc;
	}

	public String getStyhdm() {
		return this.styhdm;
	}

	public void setStyhdm(String styhdm) {
		this.updated_at = new Date();
		this.styhdm = styhdm;
	}

}

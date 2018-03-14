package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_common_person")
@org.hibernate.annotations.Table(appliesTo = "st_common_person", comment = "个人信息 表4.0.3")
public class StCommonPerson extends Common implements java.io.Serializable,Comparable<StCommonPerson> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9015602871234210947L;
	@Column(name = "GRZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '个人账号'")
	private String grzh;
	@Column(name = "XingMing", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '姓名'")
	private String xingMing;
	@Column(name = "XMQP", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '姓名全拼'")
	private String xmqp;
	@Column(name = "XingBie", columnDefinition = "VARCHAR(1) DEFAULT NULL COMMENT '性别'")
	private Character xingBie;
	@Column(name = "GDDHHM", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '固定电话号码'")
	private String gddhhm;
	@Column(name = "SJHM", columnDefinition = "VARCHAR(11) DEFAULT NULL COMMENT '手机号码'")
	private String sjhm;
	@Column(name = "ZJLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '证件类型'")
	private String zjlx;
	@Column(name = "ZJHM", columnDefinition = "VARCHAR(18) DEFAULT NULL COMMENT '证件号码'")
	private String zjhm;
	@Column(name = "CSNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '出生年月'")
	private String csny;
	@Column(name = "HYZK", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '婚姻状况'")
	private String hyzk;
	@Column(name = "ZhiYe", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '职业'")
	private String zhiYe;
	@Column(name = "ZhiChen", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '职称'")
	private String zhiChen;
	@Column(name = "ZhiWu", columnDefinition = "VARCHAR(4) DEFAULT NULL COMMENT '职务'")
	private String zhiWu;
	@Column(name = "XueLi", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '学历'")
	private String xueLi;
	@Column(name = "YZBM", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '邮政编码'")
	private String yzbm;
	@Column(name = "JTZZ", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '家庭住址'")
	private String jtzz;
	@Column(name = "JTYSR", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '家庭月收入'")
	private BigDecimal jtysr = BigDecimal.ZERO;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "PersonalAccount", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '个人账户信息'")
	private StCollectionPersonalAccount collectionPersonalAccount;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "Unit", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '单位基础信息'")
	private StCommonUnit unit;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "extension", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '个人基础信息扩展'")
	private CCommonPersonExtension cCommonPersonExtension;

	public StCommonPerson() {
		super();

	}

	public StCommonPerson(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted, String grzh,
			String xingMing, String xmqp, Character xingBie, String gddhhm, String sjhm, String zjlx, String zjhm,
			String csny, String hyzk, String zhiYe, String zhiChen, String zhiWu, String xueLi, String yzbm,
			String jtzz, BigDecimal jtysr, StCollectionPersonalAccount collectionPersonalAccount, StCommonUnit unit,
			CCommonPersonExtension extension) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.grzh = grzh;
		this.xingMing = xingMing;
		this.xmqp = xmqp;
		this.xingBie = xingBie;
		this.gddhhm = gddhhm;
		this.sjhm = sjhm;
		this.zjlx = zjlx;
		this.zjhm = zjhm;
		this.csny = csny;
		this.hyzk = hyzk;
		this.zhiYe = zhiYe;
		this.zhiChen = zhiChen;
		this.zhiWu = zhiWu;
		this.xueLi = xueLi;
		this.yzbm = yzbm;
		this.jtzz = jtzz;
		this.jtysr = jtysr;
		this.collectionPersonalAccount = collectionPersonalAccount;
		this.unit = unit;
		this.cCommonPersonExtension = extension;
	}

	public StCollectionPersonalAccount getCollectionPersonalAccount() {
		return collectionPersonalAccount;
	}

	public void setCollectionPersonalAccount(StCollectionPersonalAccount collectionPersonalAccount) {
		this.updated_at = new Date();
		this.collectionPersonalAccount = collectionPersonalAccount;
	}

	public StCommonUnit getUnit() {
		return unit;
	}

	public void setUnit(StCommonUnit unit) {
		this.updated_at = new Date();
		this.unit = unit;
	}

	public CCommonPersonExtension getExtension() {
		return cCommonPersonExtension;
	}

	public void setExtension(CCommonPersonExtension extension) {
		this.updated_at = new Date();
		this.cCommonPersonExtension = extension;
	}

	public String getGrzh() {
		return this.grzh;
	}

	public void setGrzh(String grzh) {
		this.updated_at = new Date();
		this.grzh = grzh;
	}

	public String getXingMing() {
		return this.xingMing;
	}

	public void setXingMing(String xingMing) {
		this.updated_at = new Date();
		this.xingMing = xingMing;
	}

	public String getXmqp() {
		return this.xmqp;
	}

	public void setXmqp(String xmqp) {
		this.updated_at = new Date();
		this.xmqp = xmqp;
	}

	public Character getXingBie() {
		return this.xingBie;
	}

	public void setXingBie(Character xingBie) {
		this.updated_at = new Date();
		this.xingBie = xingBie;
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

	public String getZjlx() {
		return this.zjlx;
	}

	public void setZjlx(String zjlx) {
		this.updated_at = new Date();
		this.zjlx = zjlx;
	}

	public String getZjhm() {
		return this.zjhm;
	}

	public void setZjhm(String zjhm) {
		this.updated_at = new Date();
		this.zjhm = zjhm;
	}

	public String getCsny() {
		return this.csny;
	}

	public void setCsny(String csny) {
		this.updated_at = new Date();
		this.csny = csny;
	}

	public String getHyzk() {
		return this.hyzk;
	}

	public void setHyzk(String hyzk) {
		this.updated_at = new Date();
		this.hyzk = hyzk;
	}

	public String getZhiYe() {
		return this.zhiYe;
	}

	public void setZhiYe(String zhiYe) {
		this.updated_at = new Date();
		this.zhiYe = zhiYe;
	}

	public String getZhiChen() {
		return this.zhiChen;
	}

	public void setZhiChen(String zhiChen) {
		this.updated_at = new Date();
		this.zhiChen = zhiChen;
	}

	public String getZhiWu() {
		return this.zhiWu;
	}

	public void setZhiWu(String zhiWu) {
		this.updated_at = new Date();
		this.zhiWu = zhiWu;
	}

	public String getXueLi() {
		return this.xueLi;
	}

	public void setXueLi(String xueLi) {
		this.updated_at = new Date();
		this.xueLi = xueLi;
	}

	public String getYzbm() {
		return this.yzbm;
	}

	public void setYzbm(String yzbm) {
		this.updated_at = new Date();
		this.yzbm = yzbm;
	}

	public String getJtzz() {
		return this.jtzz;
	}

	public void setJtzz(String jtzz) {
		this.updated_at = new Date();
		this.jtzz = jtzz;
	}

	public BigDecimal getJtysr() {
		return this.jtysr;
	}

	public void setJtysr(BigDecimal jtysr) {
		this.updated_at = new Date();
		this.jtysr = jtysr;
	}


	@Override
	public int compareTo(StCommonPerson o) {
		return this.getGrzh().compareTo(o.getGrzh());
	}
}

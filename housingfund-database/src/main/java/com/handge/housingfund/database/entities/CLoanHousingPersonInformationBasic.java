package com.handge.housingfund.database.entities;

// Generated 2017-8-8 15:42:03 by Hibernate Tools 5.2.3.Final

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

// default package

/**
 * HHousingPersonInformationBasic generated by hbm2java
 */
@Entity
@Table(name = "c_loan_housing_person_information_basic",indexes = {
		@Index(name = "INDEX_DKZH", columnList = "DKZH", unique = true)})
@org.hibernate.annotations.Table(appliesTo = "c_loan_housing_person_information_basic", comment = "贷款个人信息基础表")
public class CLoanHousingPersonInformationBasic extends Common implements java.io.Serializable {

	private static final long serialVersionUID = 2784691723366382107L;
	@Column(name = "DKZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '贷款账号'")
	private String dkzh;
	@Column(name = "JCD", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '缴存地'")
	private String jcd;
	@Column(name = "JKRGJJZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '借款人公积金账号'")
	private String jkrgjjzh;
	@Column(name = "NianLing", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '年龄'")
	private String nianLing;
	@Column(name = "JKZK", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '健康状态（0：良好 1：一般 2：差）'")
	private String jkzk;
	@Column(name = "YGXZ", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '用工性质（0：正式职工 1：合同制 2：聘用制）'")
	private String ygxz;
	@Column(name = "ZYJJLY", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '主要经济来源（0：工资收入 1：个体经营收入 2：其他非工资收入）'")
	private String zyjjly;
	@Column(name = "YSR", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '月收入'")
	private BigDecimal ysr = BigDecimal.ZERO;
	@Column(name = "JTYSR", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '家庭月收入'")
	private BigDecimal jtysr = BigDecimal.ZERO;
	@Column(name = "BLZL", columnDefinition = "TEXT DEFAULT NULL COMMENT '办理资料'")
	private String blzl;
	@Column(name = "QTBLZL", columnDefinition = "TEXT DEFAULT NULL COMMENT '其他办理资料'")
	private String qtblzl;
	@Column(name = "JKRXM", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '借款人姓名'")
	private String jkrxm;
	@Column(name = "JKRZJLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '借款人证件类型'")
	private String jkrzjlx;
	@Column(name = "JKRZJHM", columnDefinition = "VARCHAR(18) DEFAULT NULL COMMENT '借款人证件号码'")
	private String jkrzjhm;
	@Column(name = "CSNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '出生年月'")
	private String csny;
	@Column(name = "XingBie", columnDefinition = "VARCHAR(1) DEFAULT NULL COMMENT '性别'")
	private Character xingBie;
	@Column(name = "XueLi", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '学历'")
	private String xueLi;
	@Column(name = "HYZK", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '婚姻状态（0：已婚 1：未婚 2：丧偶 3：离婚 4：未说明的婚姻状况）'")
	private String hyzk;
	@Column(name = "ZhiCheng", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '职称'")
	private String zhiCheng;
	@Column(name = "ZhiWu", columnDefinition = "VARCHAR(4) DEFAULT NULL COMMENT '职务'")
	private String zhiWu;
	@Column(name = "GDDHHM", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '固定电话号码'")
	private String gddhhm;
	@Column(name = "SJHM", columnDefinition = "VARCHAR(11) DEFAULT NULL COMMENT '手机号码'")
	private String sjhm;
	@Column(name = "JTZZ", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '家庭住址'")
	private String jtzz;
	@Column(name = "HKSZD", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '户口所在地'")
	private String hkszd;
	@Column(name = "JZNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '缴至年月'")
	private String jzny;
	@Column(name = "GRZHZT", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '个人账户状态'")
	private String grzhzt;
	@Column(name = "YJCE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '月缴存额'")
	private BigDecimal yjce = BigDecimal.ZERO;
	@Column(name = "GRJCJS", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '个人缴存基数'")
	private BigDecimal grjcjs = BigDecimal.ZERO;
	@Column(name = "GRZHYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '个人账户余额'")
	private BigDecimal grzhye = BigDecimal.ZERO;
	@Column(name = "DWMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '单位名称'")
	private String dwmc;
	@Column(name = "DWZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '单位账号'")
	private String dwzh;
	@Column(name = "DWDZ", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '单位地址'")
	private String dwdz;
	@Column(name = "DWDH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '单位电话'")
	private String dwdh;
	@Column(name = "DWXZ", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '单位性质'")
	private String dwxz;
	@Column(name = "YWLSH", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '业务流水号'")
	private String ywlsh;
	@Column(name = "DKYT", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '贷款用途'")
	private String dkyt;
	@Column(name = "YHQS", columnDefinition = "NUMERIC(4,0) DEFAULT NULL COMMENT '已还期数'")
	private BigDecimal yhqs = BigDecimal.ZERO;
	@Column(name = "DQYQQS", columnDefinition = "NUMERIC(4,0) DEFAULT NULL COMMENT '当前逾期期数'")
	private BigDecimal dqyqqs = BigDecimal.ZERO;
	@Column(name = "CZY", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '操作员'")
	private String czy;
	@Column(name = "YWWD", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '业务网点'")
	private String ywwd;
	@Column(name = "LXZCJCYS", columnDefinition = "NUMERIC(4,0) DEFAULT NULL COMMENT '连续正常缴存月数'")
	private BigDecimal lxzcjcys = BigDecimal.ZERO;
	@Column(name = "SPBWJ", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '审批表文件'")
	private String spbwj;
	@Column(name = "DKZHZT", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '贷款账户状态'")
	private String dkzhzt;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "coborrower", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '共同还款人'")
	private StHousingCoborrower coborrower;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "guaranteeContract", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '担保合同'")
	private StHousingGuaranteeContract guaranteeContract;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "loanContract", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '借款合同'")
	private StHousingPersonalLoan loanContract;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "personalAccount", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '贷款账户信息'")
	private StHousingPersonalAccount personalAccount;

	// @OneToMany
	// private StHousingBusinessDetails businessDetails;

//	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cLoanHousingPersonInformationBasic")
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "purchasingBasic", columnDefinition = "varchar(32) DEFAULT NULL COMMENT '基础-房屋购买信息表'")
	private CLoanHousePurchasingBasic purchasing;

//	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cLoanHousingPersonInformationBasic")
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "overhaulBasic", columnDefinition = "varchar(32) DEFAULT NULL COMMENT '基础-房屋大修信息表'")
	private CLoanHouseOverhaulBasic overhaul;

//	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cLoanHousingPersonInformationBasic")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "buildBasic", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '基础-房屋自建、翻修信息表'")
	private CLoanHouseBuildBasic build;

//	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cLoanHousingPersonInformationBasic")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "fundsBasic", columnDefinition = "varchar(32) DEFAULT NULL COMMENT '基础-个人贷款资金信息表'")
	private CLoanFundsInformationBasic funds;

	public CLoanHousingPersonInformationBasic() {
	}

	public CLoanHousingPersonInformationBasic(String id, Date created_at, boolean deleted, Date deleted_at,
			Date updated_at, String dkzh, String jcd, String jkrgjjzh, String nianLing, String jkzk, String ygxz,
			String zyjjly, BigDecimal ysr, BigDecimal jtysr, String blzl, String qtblzl, String jkrxm, String jkrzjlx,
			String jkrzjhm, String csny, Character xingBie, String xueLi, String hyzk, String zhiCheng, String zhiWu,
			String gddhhm, String sjhm, String jtzz, String hkszd, String jzny, String grzhzt, BigDecimal yjce,
			BigDecimal grjcjs, BigDecimal grzhye, String dwmc, String dwzh, String dwdz, String dwdh, String dwxz,
			String ywlsh, String dkyt, BigDecimal yhqs, BigDecimal dqyqqs, String czy, String ywwd, BigDecimal lxzcjcys,
			String dkzhzt, CLoanHousePurchasingBasic purchasing, CLoanHouseOverhaulBasic overhaul,
			CLoanHouseBuildBasic build, StHousingCoborrower coborrower, CLoanFundsInformationBasic funds,
			StHousingGuaranteeContract guaranteeContract, StHousingPersonalLoan loanContract,
			StHousingPersonalAccount personalAccount, String spbwj) {
		this.id = id;
		this.created_at = created_at;
		this.deleted = deleted;
		this.deleted_at = deleted_at;
		this.updated_at = updated_at;
		this.dkzh = dkzh;
		this.jcd = jcd;
		this.jkrgjjzh = jkrgjjzh;
		this.nianLing = nianLing;
		this.jkzk = jkzk;
		this.ygxz = ygxz;
		this.zyjjly = zyjjly;
		this.ysr = ysr;
		this.jtysr = jtysr;
		this.blzl = blzl;
		this.qtblzl = qtblzl;
		this.jkrxm = jkrxm;
		this.jkrzjlx = jkrzjlx;
		this.jkrzjhm = jkrzjhm;
		this.csny = csny;
		this.xingBie = xingBie;
		this.xueLi = xueLi;
		this.hyzk = hyzk;
		this.zhiCheng = zhiCheng;
		this.zhiWu = zhiWu;
		this.gddhhm = gddhhm;
		this.sjhm = sjhm;
		this.jtzz = jtzz;
		this.hkszd = hkszd;
		this.jzny = jzny;
		this.grzhzt = grzhzt;
		this.yjce = yjce;
		this.grjcjs = grjcjs;
		this.grzhye = grzhye;
		this.dwmc = dwmc;
		this.dwzh = dwzh;
		this.dwdz = dwdz;
		this.dwdh = dwdh;
		this.dwxz = dwxz;
		this.ywlsh = ywlsh;
		this.dkyt = dkyt;
		this.yhqs = yhqs;
		this.dqyqqs = dqyqqs;
		this.czy = czy;
		this.ywwd = ywwd;
		this.spbwj = spbwj;
		this.lxzcjcys = lxzcjcys;
		this.dkzhzt = dkzhzt;
		this.purchasing = purchasing;
		this.overhaul = overhaul;
		this.build = build;
		this.coborrower = coborrower;
		this.funds = funds;
		this.guaranteeContract = guaranteeContract;
		this.loanContract = loanContract;
		this.personalAccount = personalAccount;
	}

	public String getSpbwj() {
		return spbwj;
	}

	public void setSpbwj(String spbwj) {
		this.updated_at = new Date();
		this.spbwj = spbwj;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJcd() {
		return this.jcd;
	}

	public void setJcd(String jcd) {
		this.updated_at = new Date();
		this.jcd = jcd;
	}

	public String getJkrgjjzh() {
		return this.jkrgjjzh;
	}

	public void setJkrgjjzh(String jkrgjjzh) {
		this.updated_at = new Date();
		this.jkrgjjzh = jkrgjjzh;
	}

	public String getNianLing() {
		return this.nianLing;
	}

	public void setNianLing(String nianLing) {
		this.updated_at = new Date();
		this.nianLing = nianLing;
	}

	public BigDecimal getYsr() {
		return this.ysr;
	}

	public void setYsr(BigDecimal ysr) {
		this.updated_at = new Date();
		this.ysr = ysr;
	}

	public BigDecimal getJtysr() {
		return this.jtysr;
	}

	public void setJtysr(BigDecimal jtysr) {
		this.updated_at = new Date();
		this.jtysr = jtysr;
	}

	public String getBlzl() {
		return this.blzl;
	}

	public void setBlzl(String blzl) {
		this.updated_at = new Date();
		this.blzl = blzl;
	}

	public String getQtblzl() {
		return this.qtblzl;
	}

	public void setQtblzl(String qtblzl) {
		this.updated_at = new Date();
		this.qtblzl = qtblzl;
	}

	public String getJkrxm() {
		return this.jkrxm;
	}

	public void setJkrxm(String jkrxm) {
		this.updated_at = new Date();
		this.jkrxm = jkrxm;
	}

	public String getJkrzjhm() {
		return this.jkrzjhm;
	}

	public void setJkrzjhm(String jkrzjhm) {
		this.updated_at = new Date();
		this.jkrzjhm = jkrzjhm;
	}

	public String getCsny() {
		return this.csny;
	}

	public void setCsny(String csny) {
		this.updated_at = new Date();
		this.csny = csny;
	}

	public String getDkzh() {
		return dkzh;
	}

	public void setDkzh(String dkzh) {
		this.updated_at = new Date();
		this.dkzh = dkzh;
	}

	public String getJkzk() {
		return jkzk;
	}

	public void setJkzk(String jkzk) {
		this.updated_at = new Date();
		this.jkzk = jkzk;
	}

	public String getYgxz() {
		return ygxz;
	}

	public void setYgxz(String ygxz) {
		this.updated_at = new Date();
		this.ygxz = ygxz;
	}

	public String getZyjjly() {
		return zyjjly;
	}

	public void setZyjjly(String zyjjly) {
		this.updated_at = new Date();
		this.zyjjly = zyjjly;
	}

	public String getJkrzjlx() {
		return jkrzjlx;
	}

	public void setJkrzjlx(String jkrzjlx) {
		this.updated_at = new Date();
		this.jkrzjlx = jkrzjlx;
	}

	public Character getXingBie() {
		return xingBie;
	}

	public void setXingBie(Character xingBie) {
		this.updated_at = new Date();
		this.xingBie = xingBie;
	}

	public String getXueLi() {
		return xueLi;
	}

	public void setXueLi(String xueLi) {
		this.updated_at = new Date();
		this.xueLi = xueLi;
	}

	public String getHyzk() {
		return hyzk;
	}

	public void setHyzk(String hyzk) {
		this.updated_at = new Date();
		this.hyzk = hyzk;
	}

	public String getZhiCheng() {
		return zhiCheng;
	}

	public void setZhiCheng(String zhiCheng) {
		this.updated_at = new Date();
		this.zhiCheng = zhiCheng;
	}

	public String getZhiWu() {
		return zhiWu;
	}

	public void setZhiWu(String zhiWu) {
		this.updated_at = new Date();
		this.zhiWu = zhiWu;
	}

	public String getGddhhm() {
		return gddhhm;
	}

	public void setGddhhm(String gddhhm) {
		this.updated_at = new Date();
		this.gddhhm = gddhhm;
	}

	public String getSjhm() {
		return sjhm;
	}

	public void setSjhm(String sjhm) {
		this.updated_at = new Date();
		this.sjhm = sjhm;
	}

	public String getJtzz() {
		return jtzz;
	}

	public void setJtzz(String jtzz) {
		this.updated_at = new Date();
		this.jtzz = jtzz;
	}

	public String getHkszd() {
		return hkszd;
	}

	public void setHkszd(String hkszd) {
		this.updated_at = new Date();
		this.hkszd = hkszd;
	}

	public String getJzny() {
		return jzny;
	}

	public void setJzny(String jzny) {
		this.updated_at = new Date();
		this.jzny = jzny;
	}

	public String getGrzhzt() {
		return grzhzt;
	}

	public void setGrzhzt(String grzhzt) {
		this.updated_at = new Date();
		this.grzhzt = grzhzt;
	}

	public BigDecimal getYjce() {
		return yjce;
	}

	public void setYjce(BigDecimal yjce) {
		this.updated_at = new Date();
		this.yjce = yjce;
	}

	public BigDecimal getGrjcjs() {
		return grjcjs;
	}

	public void setGrjcjs(BigDecimal grjcjs) {
		this.updated_at = new Date();
		this.grjcjs = grjcjs;
	}

	public BigDecimal getGrzhye() {
		return grzhye;
	}

	public void setGrzhye(BigDecimal grzhye) {
		this.updated_at = new Date();
		this.grzhye = grzhye;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.updated_at = new Date();
		this.dwmc = dwmc;
	}

	public String getDwzh() {
		return dwzh;
	}

	public void setDwzh(String dwzh) {
		this.updated_at = new Date();
		this.dwzh = dwzh;
	}

	public String getDwdz() {
		return dwdz;
	}

	public void setDwdz(String dwdz) {
		this.updated_at = new Date();
		this.dwdz = dwdz;
	}

	public String getDwdh() {
		return dwdh;
	}

	public void setDwdh(String dwdh) {
		this.updated_at = new Date();
		this.dwdh = dwdh;
	}

	public String getDwxz() {
		return dwxz;
	}

	public void setDwxz(String dwxz) {
		this.updated_at = new Date();
		this.dwxz = dwxz;
	}

	public String getYwlsh() {
		return ywlsh;
	}

	public void setYwlsh(String ywlsh) {
		this.updated_at = new Date();
		this.ywlsh = ywlsh;
	}

	public String getDkyt() {
		return dkyt;
	}

	public void setDkyt(String dkyt) {
		this.updated_at = new Date();
		this.dkyt = dkyt;
	}

	public BigDecimal getYhqs() {
		return yhqs;
	}

	public void setYhqs(BigDecimal yhqs) {
		this.updated_at = new Date();
		this.yhqs = yhqs;
	}

	public BigDecimal getDqyqqs() {
		return dqyqqs;
	}

	public void setDqyqqs(BigDecimal dqyqqs) {
		this.updated_at = new Date();
		this.dqyqqs = dqyqqs;
	}

	public String getCzy() {
		return czy;
	}

	public void setCzy(String czy) {
		this.updated_at = new Date();
		this.czy = czy;
	}

	public String getYwwd() {
		return ywwd;
	}

	public void setYwwd(String ywwd) {
		this.updated_at = new Date();
		this.ywwd = ywwd;
	}

	public BigDecimal getLxzcjcys() {
		return lxzcjcys;
	}

	public void setLxzcjcys(BigDecimal lxzcjcys) {
		this.updated_at = new Date();
		this.lxzcjcys = lxzcjcys;
	}

	public String getDkzhzt() {
		return dkzhzt;
	}

	public void setDkzhzt(String dkzhzt) {
		this.updated_at = new Date();
		this.dkzhzt = dkzhzt;
	}

	public StHousingCoborrower getCoborrower() {
		return coborrower;
	}

	public void setCoborrower(StHousingCoborrower coborrower) {
		this.updated_at = new Date();
		this.coborrower = coborrower;
	}

	public StHousingGuaranteeContract getGuaranteeContract() {
		return guaranteeContract;
	}

	public void setGuaranteeContract(StHousingGuaranteeContract guaranteeContract) {
		this.updated_at = new Date();
		this.guaranteeContract = guaranteeContract;
	}

	public StHousingPersonalLoan getLoanContract() {
		return loanContract;
	}

	public void setLoanContract(StHousingPersonalLoan loanContract) {
		this.updated_at = new Date();
		this.loanContract = loanContract;
	}

	public StHousingPersonalAccount getPersonalAccount() {
		return personalAccount;
	}

	public void setPersonalAccount(StHousingPersonalAccount personalAccount) {
		this.updated_at = new Date();
		this.personalAccount = personalAccount;
	}

	public CLoanHousePurchasingBasic getPurchasing() {
		return purchasing;
	}

	public void setPurchasing(CLoanHousePurchasingBasic purchasing) {
		this.updated_at = new Date();
		this.purchasing = purchasing;
	}

	public CLoanHouseOverhaulBasic getOverhaul() {
		return overhaul;
	}

	public void setOverhaul(CLoanHouseOverhaulBasic overhaul) {
		this.updated_at = new Date();
		this.overhaul = overhaul;
	}

	public CLoanHouseBuildBasic getBuild() {
		return build;
	}

	public void setBuild(CLoanHouseBuildBasic build) {
		this.updated_at = new Date();
		this.build = build;
	}

	public CLoanFundsInformationBasic getFunds() {
		return funds;
	}

	public void setFunds(CLoanFundsInformationBasic funds) {
		this.updated_at = new Date();
		this.funds = funds;
	}

}
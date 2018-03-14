package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_housing_personal_loan")
@org.hibernate.annotations.Table(appliesTo = "st_housing_personal_loan", comment = "个人住房贷款借款合同信息 表6.0.2")
public class StHousingPersonalLoan extends Common implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 597943330400316573L;
	@Column(name = "JKHTBH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '借款合同编号'")
	private String jkhtbh;
	@Column(name = "GFHTBH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '购房合同编号'")
	private String gfhtbh;
	@Column(name = "SWTYHMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '受委托银行名称'")
	private String swtyhmc;
	@Column(name = "SWTYHDM", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '受委托银行代码'")
	private String swtyhdm;
	@Column(name = "DKDBLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '贷款担保类型'")
	private String dkdblx;
	@Column(name = "YDFKRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '约定放款日期'")
	private Date ydfkrq;
	@Column(name = "YDDQRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '约定到期日期'")
	private Date yddqrq;
	@Column(name = "DKHKFS", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '贷款还款方式'")
	private String dkhkfs;
	@Column(name = "HKZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '还款账号'")
	private String hkzh;
	@Column(name = "ZHKHYHMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '账户开户银行名称'")
	private String zhkhyhmc;
	@Column(name = "ZHKHYHDM", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '账户开户银行代码'")
	private String zhkhyhdm;
	@Column(name = "HTDKJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '合同贷款金额'")
	private BigDecimal htdkje = BigDecimal.ZERO;
	@Column(name = "DKLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '贷款类型'")
	private String dklx;
	@Column(name = "DKQS", columnDefinition = "NUMERIC(4,0) DEFAULT NULL COMMENT '贷款期数'")
	private BigDecimal dkqs = BigDecimal.ZERO;
	@Column(name = "FWZL", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '房屋坐落'")
	private String fwzl;
	@Column(name = "FWJZMJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '房屋建筑面积'")
	private BigDecimal fwjzmj = BigDecimal.ZERO;
	@Column(name = "FWTNMJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '房屋套内面积'")
	private BigDecimal fwtnmj = BigDecimal.ZERO;
	@Column(name = "FWXZ", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '房屋性质'")
	private String fwxz;
	@Column(name = "FWZJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '房屋总价'")
	private BigDecimal fwzj = BigDecimal.ZERO;
	@Column(name = "JKHTLL", columnDefinition = "NUMERIC(8,7) DEFAULT NULL COMMENT '借款合同利率'")
	private BigDecimal jkhtll = BigDecimal.ZERO;
	@Column(name = "LLFDBL", columnDefinition = "NUMERIC(4,2) DEFAULT NULL COMMENT '利率浮动比例'")
	private BigDecimal llfdbl = BigDecimal.ZERO;
	@Column(name = "GFSFK", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '购房首付款'")
	private BigDecimal gfsfk = BigDecimal.ZERO;
	@Column(name = "JKRGJJZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '借款人公积金账号'")
	private String jkrgjjzh;
	@Column(name = "JKRXM", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '借款人姓名'")
	private String jkrxm;
	@Column(name = "JKRZJLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '借款人证件类型'")
	private String jkrzjlx;
	@Column(name = "JKRZJH", columnDefinition = "VARCHAR(18) DEFAULT NULL COMMENT '借款人证件号'")
	private String jkrzjh;
	@Column(name = "SFRMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '售房人名称'")
	private String sfrmc;
	@Column(name = "SFRZHHM", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '售房人账户号码'")
	private String sfrzhhm;
	@Column(name = "SFRKHYHDM", columnDefinition = "VARCHAR(3) DEFAULT NULL COMMENT '售房人开户银行代码'")
	private String sfrkhyhdm;
	@Column(name = "SFRKHYHMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '售房人开户银行名称'")
	private String sfrkhyhmc;
	@Column(name = "JKHTQDRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '借款合同签订日期'")
	private Date jkhtqdrq;
	@Column(name = "YDHKR", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '约定还款日'")
	private String ydhkr;



	@Column(name = "BLZL", columnDefinition = "TEXT DEFAULT NULL COMMENT '办理资料'")
	private String blzl;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "extenstion", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '个人贷款合同拓展'")
	private CLoanHousingPersonalLoanExtension cLoanHousingPersonalLoanExtension;

	public StHousingPersonalLoan() {
		super();

	}

	public StHousingPersonalLoan(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String jkhtbh, String gfhtbh, String swtyhmc, String swtyhdm, String dkdblx, Date ydfkrq, Date yddqrq,
			String dkhkfs, String hkzh, String zhkhyhmc, String zhkhyhdm, BigDecimal htdkje, String dklx,
			BigDecimal dkqs, String fwzl, BigDecimal fwjzmj, BigDecimal fwtnmj, String fwxz, BigDecimal fwzj,
			BigDecimal jkhtll, BigDecimal llfdbl, BigDecimal gfsfk, String jkrgjjzh, String jkrxm, String jkrzjlx,
			String jkrzjh, String sfrmc, String sfrzhhm, String sfrkhyhdm, String sfrkhyhmc, Date jkhtqdrq,
			String ydhkr, CLoanHousingPersonalLoanExtension cLoanHousingPersonalLoanExtension) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.jkhtbh = jkhtbh;
		this.gfhtbh = gfhtbh;
		this.swtyhmc = swtyhmc;
		this.swtyhdm = swtyhdm;
		this.dkdblx = dkdblx;
		this.ydfkrq = ydfkrq;
		this.yddqrq = yddqrq;
		this.dkhkfs = dkhkfs;
		this.hkzh = hkzh;
		this.zhkhyhmc = zhkhyhmc;
		this.zhkhyhdm = zhkhyhdm;
		this.htdkje = htdkje;
		this.dklx = dklx;
		this.dkqs = dkqs;
		this.fwzl = fwzl;
		this.fwjzmj = fwjzmj;
		this.fwtnmj = fwtnmj;
		this.fwxz = fwxz;
		this.fwzj = fwzj;
		this.jkhtll = jkhtll;
		this.llfdbl = llfdbl;
		this.gfsfk = gfsfk;
		this.jkrgjjzh = jkrgjjzh;
		this.jkrxm = jkrxm;
		this.jkrzjlx = jkrzjlx;
		this.jkrzjh = jkrzjh;
		this.sfrmc = sfrmc;
		this.sfrzhhm = sfrzhhm;
		this.sfrkhyhdm = sfrkhyhdm;
		this.sfrkhyhmc = sfrkhyhmc;
		this.jkhtqdrq = jkhtqdrq;
		this.ydhkr = ydhkr;
		this.cLoanHousingPersonalLoanExtension = cLoanHousingPersonalLoanExtension;
	}

	public String getBlzl() {
		this.updated_at = new Date();
		return blzl;
	}

	public void setBlzl(String blzl) {
		this.blzl = blzl;
	}
	public String getJkhtbh() {
		return this.jkhtbh;
	}

	public void setJkhtbh(String jkhtbh) {
		this.updated_at = new Date();
		this.jkhtbh = jkhtbh;
	}

	public String getGfhtbh() {
		return this.gfhtbh;
	}

	public void setGfhtbh(String gfhtbh) {
		this.updated_at = new Date();
		this.gfhtbh = gfhtbh;
	}

	public String getSwtyhmc() {
		return this.swtyhmc;
	}

	public void setSwtyhmc(String swtyhmc) {
		this.updated_at = new Date();
		this.swtyhmc = swtyhmc;
	}

	public String getSwtyhdm() {
		return this.swtyhdm;
	}

	public void setSwtyhdm(String swtyhdm) {
		this.updated_at = new Date();
		this.swtyhdm = swtyhdm;
	}

	public String getDkdblx() {
		return this.dkdblx;
	}

	public void setDkdblx(String dkdblx) {
		this.updated_at = new Date();
		this.dkdblx = dkdblx;
	}

	public Date getYdfkrq() {
		return this.ydfkrq;
	}

	public void setYdfkrq(Date ydfkrq) {
		this.updated_at = new Date();
		this.ydfkrq = ydfkrq;
	}

	public Date getYddqrq() {
		return this.yddqrq;
	}

	public void setYddqrq(Date yddqrq) {
		this.updated_at = new Date();
		this.yddqrq = yddqrq;
	}

	public String getDkhkfs() {
		return this.dkhkfs;
	}

	public void setDkhkfs(String dkhkfs) {
		this.updated_at = new Date();
		this.dkhkfs = dkhkfs;
	}

	public String getHkzh() {
		return this.hkzh;
	}

	public void setHkzh(String hkzh) {
		this.updated_at = new Date();
		this.hkzh = hkzh;
	}

	public String getZhkhyhmc() {
		return this.zhkhyhmc;
	}

	public void setZhkhyhmc(String zhkhyhmc) {
		this.updated_at = new Date();
		this.zhkhyhmc = zhkhyhmc;
	}

	public String getZhkhyhdm() {
		return this.zhkhyhdm;
	}

	public void setZhkhyhdm(String zhkhyhdm) {
		this.updated_at = new Date();
		this.zhkhyhdm = zhkhyhdm;
	}

	public BigDecimal getHtdkje() {
		return this.htdkje;
	}

	public void setHtdkje(BigDecimal htdkje) {
		this.updated_at = new Date();
		this.htdkje = htdkje;
	}

	public String getDklx() {
		return this.dklx;
	}

	public void setDklx(String dklx) {
		this.updated_at = new Date();
		this.dklx = dklx;
	}

	public BigDecimal getDkqs() {
		return this.dkqs;
	}

	public void setDkqs(BigDecimal dkqs) {
		this.updated_at = new Date();
		this.dkqs = dkqs;
	}

	public String getFwzl() {
		return this.fwzl;
	}

	public void setFwzl(String fwzl) {
		this.updated_at = new Date();
		this.fwzl = fwzl;
	}

	public BigDecimal getFwjzmj() {
		return this.fwjzmj;
	}

	public void setFwjzmj(BigDecimal fwjzmj) {
		this.updated_at = new Date();
		this.fwjzmj = fwjzmj;
	}

	public BigDecimal getFwtnmj() {
		return this.fwtnmj;
	}

	public void setFwtnmj(BigDecimal fwtnmj) {
		this.updated_at = new Date();
		this.fwtnmj = fwtnmj;
	}

	public String getFwxz() {
		return this.fwxz;
	}

	public void setFwxz(String fwxz) {
		this.updated_at = new Date();
		this.fwxz = fwxz;
	}

	public BigDecimal getFwzj() {
		return this.fwzj;
	}

	public void setFwzj(BigDecimal fwzj) {
		this.updated_at = new Date();
		this.fwzj = fwzj;
	}

	public BigDecimal getJkhtll() {
		return this.jkhtll;
	}

	public void setJkhtll(BigDecimal jkhtll) {
		this.updated_at = new Date();
		this.jkhtll = jkhtll;
	}

	public BigDecimal getLlfdbl() {
		return this.llfdbl;
	}

	public void setLlfdbl(BigDecimal llfdbl) {
		this.updated_at = new Date();
		this.llfdbl = llfdbl;
	}

	public BigDecimal getGfsfk() {
		return this.gfsfk;
	}

	public void setGfsfk(BigDecimal gfsfk) {
		this.updated_at = new Date();
		this.gfsfk = gfsfk;
	}

	public String getJkrgjjzh() {
		return this.jkrgjjzh;
	}

	public void setJkrgjjzh(String jkrgjjzh) {
		this.updated_at = new Date();
		this.jkrgjjzh = jkrgjjzh;
	}

	public String getJkrxm() {
		return this.jkrxm;
	}

	public void setJkrxm(String jkrxm) {
		this.updated_at = new Date();
		this.jkrxm = jkrxm;
	}

	public String getJkrzjlx() {
		return this.jkrzjlx;
	}

	public void setJkrzjlx(String jkrzjlx) {
		this.updated_at = new Date();
		this.jkrzjlx = jkrzjlx;
	}

	public String getJkrzjh() {
		return this.jkrzjh;
	}

	public void setJkrzjh(String jkrzjh) {
		this.updated_at = new Date();
		this.jkrzjh = jkrzjh;
	}

	public String getSfrmc() {
		return this.sfrmc;
	}

	public void setSfrmc(String sfrmc) {
		this.updated_at = new Date();
		this.sfrmc = sfrmc;
	}

	public String getSfrzhhm() {
		return this.sfrzhhm;
	}

	public void setSfrzhhm(String sfrzhhm) {
		this.updated_at = new Date();
		this.sfrzhhm = sfrzhhm;
	}

	public String getSfrkhyhdm() {
		return this.sfrkhyhdm;
	}

	public void setSfrkhyhdm(String sfrkhyhdm) {
		this.updated_at = new Date();
		this.sfrkhyhdm = sfrkhyhdm;
	}

	public String getSfrkhyhmc() {
		return this.sfrkhyhmc;
	}

	public void setSfrkhyhmc(String sfrkhyhmc) {
		this.updated_at = new Date();
		this.sfrkhyhmc = sfrkhyhmc;
	}

	public Date getJkhtqdrq() {
		return this.jkhtqdrq;
	}

	public void setJkhtqdrq(Date jkhtqdrq) {
		this.updated_at = new Date();
		this.jkhtqdrq = jkhtqdrq;
	}

	public String getYdhkr() {
		return this.ydhkr;
	}

	public void setYdhkr(String ydhkr) {
		this.updated_at = new Date();
		this.ydhkr = ydhkr;
	}

	public CLoanHousingPersonalLoanExtension getcLoanHousingPersonalLoanExtension() {
		return cLoanHousingPersonalLoanExtension;
	}

	public void setcLoanHousingPersonalLoanExtension(
			CLoanHousingPersonalLoanExtension cLoanHousingPersonalLoanExtension) {
		this.updated_at = new Date();
		this.cLoanHousingPersonalLoanExtension = cLoanHousingPersonalLoanExtension;
	}
}

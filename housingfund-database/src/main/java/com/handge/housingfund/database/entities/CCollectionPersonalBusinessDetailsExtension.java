package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "c_collection_personal_business_details_extension")
@org.hibernate.annotations.Table(appliesTo = "c_collection_personal_business_details_extension", comment = "个人业务明细扩展表")
public class CCollectionPersonalBusinessDetailsExtension extends Common implements Serializable {

	private static final long serialVersionUID = 3444318564734683869L;

	@Column(name = "SLSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '受理时间'")
	private Date slsj;
	@Column(name = "BJSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '办结时间'")
	private Date bjsj;
	@Column(name = "SHSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '审核时间（即审核列表中的受理时间）'")
	private Date shsj;
	/*
	 * @Column(name = "YWZT", columnDefinition =
	 * "varchar(20) DEFAULT NULL COMMENT '业务状态'") private String ywzt;
	 */
	@Column(name = "BLZL", columnDefinition = "TEXT DEFAULT NULL COMMENT '办理资料'")
	private String blzl;
	@Column(name = "DDSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '到达时间'")
	private Date ddsj;
	/*
	 * @Column(name = "YWZT", columnDefinition =
	 * "varchar(20) DEFAULT NULL COMMENT '业务状态'") private String ywzt;
	 */
	@Column(name = "CZY", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '操作员'")
	private String czy;
	@OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},fetch = FetchType.LAZY)
	@JoinColumn(name = "YWWD", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '业务网点对象'")
	private CAccountNetwork ywwd;
	@Column(name = "CZYY", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '操作原因'")
	private String czyy;
	@Column(name = "BeiZhu", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '备注'")
	private String beizhu;
	@Column(name = "STEP", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '状态机状态'")
	private String step;
	@Column(name = "CZMC", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '操作名称'")
	private String czmc;
	@Column(name = "DJJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '冻结金额'")
	private BigDecimal djje = BigDecimal.ZERO;
	@Column(name = "ZCDW", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '转出单位'")
	private String zcdw;
	@Column(name = "JBRXM", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '经办人姓名'")
	private String jbrxm;
	@Column(name = "JBRZJLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '经办人证件类型'")
	private String jbrzjlx;
	@Column(name = "JBRZJHM", columnDefinition = "VARCHAR(18) DEFAULT NULL COMMENT '经办人证件号码'")
	private String jbrzjhm;
	@Column(name = "SHBTGYY", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '审核不通过原因'")
	private String shbtgyy;
	@Column(name = "SHYJ", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '审核意见'")
	private String shyj;

	// 提取拓展
	@Column(name = "DLRXM", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '代理人姓名'")
	private String dlrxm;
	@Column(name = "DLRZJLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '代理人证件类型'")
	private String dlrzjlx;
	@Column(name = "DLRZJHM", columnDefinition = "VARCHAR(18) DEFAULT NULL COMMENT '代理人证件号码'")
	private String dlrzjhm;
	@Column(name = "BLR", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '办理人'")
	private String blr;
	@Column(name = "YZM", columnDefinition = "VARCHAR(36) DEFAULT NULL COMMENT '验证码'")
	private String yzm;
	@Column(name = "JZPZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '记账凭证号'")
	private String jzpzh;
	@Column(name = "HuMing", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '户名'")
	private String huming;
	@Column(name = "GRCKZHHM", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '个人存款账户号码'")
	private String grckzhhm;
	@Column(name = "GRCKZHKHYHMC", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '个人存款账户开户银行名称'")
	private String grckzhkhyhmc;
	@Column(name = "LJTQJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '累计提取金额'")
	private BigDecimal ljtqje = BigDecimal.ZERO;
	@Column(name = "YHKE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '月还款额'")
	private BigDecimal yhke = BigDecimal.ZERO;
	@Column(name = "XCTQRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '下次提取日期'")
	private Date xctqrq;
	@Column(name = "XHTQFSLXE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '销户提取发生利息额'")
	private BigDecimal xhtqfslxe = BigDecimal.ZERO;
	@Column(name = "JQE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '结清额'")
	private BigDecimal jqe = BigDecimal.ZERO;
	@Column(name = "HKFS", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '还款方式'")
	private String hkfs;
	@Column(name = "SHYBH", columnDefinition = "TEXT DEFAULT NULL COMMENT '审核员编号'")
	private String shybh;
	@Column(name = "PCH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '批次号'")
	private String pch;

	@Column(name = "DQYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '当前余额'")
	private BigDecimal dqye = BigDecimal.ZERO;

	@Column(name = "GRFSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '个人发生额'")
	private BigDecimal grfse = BigDecimal.ZERO;
	@Column(name = "DWFSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '单位发生额'")
	private BigDecimal dwfse = BigDecimal.ZERO;
	@Column(name = "FSNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '发生年月'")
	private String fsny;
	@Column(name = "SBYY", columnDefinition = "TEXT DEFAULT NULL COMMENT '失败原因'")
	private String sbyy;

	public String getPch() {
		return pch;
	}

	public void setPch(String pch) {
		this.pch = pch;
	}

	public String getSbyy() {
		return sbyy;
	}

	public void setSbyy(String sbyy) {
		this.sbyy = sbyy;
	}

	public BigDecimal getJqe() {
		return jqe;
	}

	public void setJqe(BigDecimal jqe) {
		this.jqe = jqe;
	}

	public String getHkfs() {
		return hkfs;
	}

	public void setHkfs(String hkfs) {
		this.hkfs = hkfs;
	}

	public String getGrckzhhm() {
		return grckzhhm;
	}

	public void setGrckzhhm(String grckzhhm) {
		this.grckzhhm = grckzhhm;
	}

	public String getGrckzhkhyhmc() {
		return grckzhkhyhmc;
	}

	public void setGrckzhkhyhmc(String grckzhkhyhmc) {
		this.grckzhkhyhmc = grckzhkhyhmc;
	}

	public BigDecimal getXhtqfslxe() {
		return xhtqfslxe;
	}

	public void setXhtqfslxe(BigDecimal xhtqfslxe) {

		this.xhtqfslxe = xhtqfslxe;
	}

	public String getShyj() {
		return shyj;
	}

	public void setShyj(String shyj) {

		this.shyj = shyj;
	}

	public BigDecimal getYhke() {
		return yhke;
	}

	public void setYhke(BigDecimal yhke) {

		this.yhke = yhke;
	}

	public Date getXctqrq() {
		return xctqrq;
	}

	public void setXctqrq(Date xctqrq) {

		this.xctqrq = xctqrq;
	}

	public BigDecimal getLjtqje() {
		return ljtqje;
	}

	public void setLjtqje(BigDecimal ljtqje) {

		this.ljtqje = ljtqje;
	}

	public String getHuming() {
		return huming;
	}

	public void setHuming(String huming) {

		this.huming = huming;
	}

	public Date getSlsj() {
		return slsj;
	}

	public void setSlsj(Date slsj) {

		this.slsj = slsj;
	}

	public Date getBjsj() {
		return bjsj;
	}

	public void setBjsj(Date bjsj) {

		this.bjsj = bjsj;
	}

	/*
	 * public String getYwzt() { return ywzt; }
	 * 
	 * public void setYwzt(String ywzt) {
	 * 
	 * this.ywzt = ywzt; }
	 */

	public String getBlzl() {
		return blzl;
	}

	public void setBlzl(String blzl) {

		this.blzl = blzl;
	}

	public String getCzy() {
		return czy;
	}

	public void setCzy(String czy) {

		this.czy = czy;
	}

	public CAccountNetwork getYwwd() {
		return ywwd;
	}

	public void setYwwd(CAccountNetwork ywwd) {

		this.ywwd = ywwd;
	}

	public String getCzyy() {
		return czyy;
	}

	public void setCzyy(String czyy) {

		this.czyy = czyy;
	}

	public String getBeizhu() {
		return beizhu;
	}

	public void setBeizhu(String beizhu) {

		this.beizhu = beizhu;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {

		this.step = step;
	}

	public String getCzmc() {
		return czmc;
	}

	public void setCzmc(String czmc) {

		this.czmc = czmc;
	}

	public BigDecimal getDjje() {
		return djje;
	}

	public void setDjje(BigDecimal djje) {

		this.djje = djje;
	}

	public String getZcdw() {
		return zcdw;
	}

	public void setZcdw(String zcdw) {

		this.zcdw = zcdw;
	}

	public String getJbrxm() {
		return jbrxm;
	}

	public void setJbrxm(String jbrxm) {

		this.jbrxm = jbrxm;
	}

	public String getJbrzjlx() {
		return jbrzjlx;
	}

	public void setJbrzjlx(String jbrzjlx) {

		this.jbrzjlx = jbrzjlx;
	}

	public String getJbrzjhm() {
		return jbrzjhm;
	}

	public void setJbrzjhm(String jbrzjhm) {

		this.jbrzjhm = jbrzjhm;
	}

	public String getShbtgyy() {
		return shbtgyy;
	}

	public void setShbtgyy(String shbtgyy) {

		this.shbtgyy = shbtgyy;
	}

	public String getDlrxm() {
		return dlrxm;
	}

	public void setDlrxm(String dlrxm) {

		this.dlrxm = dlrxm;
	}

	public String getDlrzjlx() {
		return dlrzjlx;
	}

	public void setDlrzjlx(String dlrzjlx) {

		this.dlrzjlx = dlrzjlx;
	}

	public String getDlrzjhm() {
		return dlrzjhm;
	}

	public void setDlrzjhm(String dlrzjhm) {

		this.dlrzjhm = dlrzjhm;
	}

	public String getBlr() {
		return blr;
	}

	public void setBlr(String blr) {

		this.blr = blr;
	}

	public String getYzm() {
		return yzm;
	}

	public void setYzm(String yzm) {

		this.yzm = yzm;
	}

	public String getJzpzh() {
		return jzpzh;
	}

	public void setJzpzh(String jzpzh) {

		this.jzpzh = jzpzh;
	}

	public String getShybh() {
		return shybh;
	}

	public void setShybh(String shybh) {

		this.shybh = shybh;
	}

	public Date getShsj() {
		return shsj;
	}

	public void setShsj(Date shsj) {

		this.shsj = shsj;
	}

	public Date getDdsj() {
		return ddsj;
	}

	public void setDdsj(Date ddsj) {
		this.ddsj = ddsj;
	}

	public BigDecimal getDqye() {
		return dqye;
	}

	public void setDqye(BigDecimal dqye) {
		this.dqye = dqye;
	}

	public BigDecimal getGrfse() {
		return grfse;
	}

	public void setGrfse(BigDecimal grfse) {
		this.grfse = grfse;
	}

	public BigDecimal getDwfse() {
		return dwfse;
	}

	public void setDwfse(BigDecimal dwfse) {
		this.dwfse = dwfse;
	}

	public String getFsny() {
		return fsny;
	}

	public void setFsny(String fsny) {
		this.fsny = fsny;
	}

	public CCollectionPersonalBusinessDetailsExtension() {
		super();

	}

	public CCollectionPersonalBusinessDetailsExtension(
			String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			Date slsj, Date bjsj, Date shsj, String blzl, Date ddsj, String czy, CAccountNetwork ywwd, String czyy, String beizhu, String step, String czmc, BigDecimal djje, String zcdw, String jbrxm, String jbrzjlx, String jbrzjhm, String shbtgyy, String shyj, String dlrxm, String dlrzjlx, String dlrzjhm, String blr, String yzm, String jzpzh, String huming, String grckzhhm, String grckzhkhyhmc, BigDecimal ljtqje, BigDecimal yhke, Date xctqrq, BigDecimal xhtqfslxe, BigDecimal jqe, String hkfs, String shybh) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.slsj = slsj;
		this.bjsj = bjsj;
		this.shsj = shsj;
		this.blzl = blzl;
		this.ddsj = ddsj;
		this.czy = czy;
		this.ywwd = ywwd;
		this.czyy = czyy;
		this.beizhu = beizhu;
		this.step = step;
		this.czmc = czmc;
		this.djje = djje;
		this.zcdw = zcdw;
		this.jbrxm = jbrxm;
		this.jbrzjlx = jbrzjlx;
		this.jbrzjhm = jbrzjhm;
		this.shbtgyy = shbtgyy;
		this.shyj = shyj;
		this.dlrxm = dlrxm;
		this.dlrzjlx = dlrzjlx;
		this.dlrzjhm = dlrzjhm;
		this.blr = blr;
		this.yzm = yzm;
		this.jzpzh = jzpzh;
		this.huming = huming;
		this.grckzhhm = grckzhhm;
		this.grckzhkhyhmc = grckzhkhyhmc;
		this.ljtqje = ljtqje;
		this.yhke = yhke;
		this.xctqrq = xctqrq;
		this.xhtqfslxe = xhtqfslxe;
		this.jqe = jqe;
		this.hkfs = hkfs;
		this.shybh = shybh;
	}
}

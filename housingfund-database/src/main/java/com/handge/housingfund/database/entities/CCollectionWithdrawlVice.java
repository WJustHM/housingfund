package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 创建人Dalu Guo 创建时间 2017/7/24. 描述
 */
@Entity
@Table(name = "c_collection_withdrawl_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_withdrawl_vice", comment = "提取业务信息副表")
public class CCollectionWithdrawlVice extends Common implements Serializable {
	private static final long serialVersionUID = -3040428563994956012L;
	@Column(name = "YWLSH", columnDefinition = "VARCHAR(20) DEFAULT NULL")
	private String ywlsh;
	@Column(name = "GRZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '个人账号'")
	private String grzh;
	@Column(name = "TQYY", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '提取原因 附A.7'")
	private String tqyy;
	@Column(name = "TQFS", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '提取方式'")
	private String tqfs;
	@Column(name = "YWMXLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '业务明细类型 附A.5'")
	private String ywmxlx;
	@Column(name = "XHYY", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '销户原因 附A.7'")
	private String xhyy;
	@Column(name = "BLR", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '办理人'")
	private String blr;
	@Column(name = "DLRXM", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '代理人姓名'")
	private String dlrxm;
	@Column(name = "DLRZJLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '代理人证件类型'")
	private String dlrzjlx;
	@Column(name = "DLRZJHM", columnDefinition = "VARCHAR(18) DEFAULT NULL COMMENT '代理人证件号码'")
	private String dlrzjhm;
	@Column(name = "FSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '发生额'")
	private BigDecimal fse = BigDecimal.ZERO;
	@Column(name = "GRCKZHHM", length = 30, columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '个人存款账户号码'")
	private String grckzhhm;
	@Column(name = "GRCKZHKHYHMC", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '个人存款账户开户银行名称'")
	private String grckzhkhyhmc;
	@Column(name = "HuMing", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '户名'")
	private String huming;
	@Column(name = "JZRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '记账日期 YYYYMMDD'")
	private Date jzrq;
	@Column(name = "BLZL", columnDefinition = "TEXT DEFAULT NULL COMMENT '办理资料'")
	private String blzl;
	@Column(name = "SLSJ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '受理时间'")
	private Date slsj;
	@Column(name = "CZY", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '操作员'")
	private String czy;
	@Column(name = "YWWD", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '业务网点'")
	private String ywwd;
	@Column(name = "ICL", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '是否处理'")
	private String icl;
	@Column(name = "LJTQJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '累计提取金额'")
	private BigDecimal ljtqje = BigDecimal.ZERO;
	@Column(name = "YHKE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '月还款额'")
	private BigDecimal yhke = BigDecimal.ZERO;
	@Column(name = "XCTQRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '下次提取日期'")
	private Date xctqrq;
	@Column(name = "JQE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '结清额'")
	private BigDecimal jqe = BigDecimal.ZERO;
	@Column(name = "HKFS", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '还款方式'")
	private String hkfs;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "GRYWMX", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '个人业务明细'")
	private StCollectionPersonalBusinessDetails grywmx;

	public BigDecimal getYhke() {
		return yhke;
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

	public void setYhke(BigDecimal yhke) {
		this.updated_at = new Date();
		this.yhke = yhke;
	}

	public Date getXctqrq() {
		return xctqrq;
	}

	public void setXctqrq(Date xctqrq) {
		this.updated_at = new Date();
		this.xctqrq = xctqrq;
	}

	public String getDlrzjlx() {
		return dlrzjlx;
	}

	public void setDlrzjlx(String dlrzjlx) {
		this.updated_at = new Date();
		this.dlrzjlx = dlrzjlx;
	}

	public String getIcl() {
		return icl;
	}

	public void setIcl(String icl) {
		this.updated_at = new Date();
		this.icl = icl;
	}

	public BigDecimal getLjtqje() {
		return ljtqje;
	}

	public void setLjtqje(BigDecimal ljtqje) {
		this.updated_at = new Date();
		this.ljtqje = ljtqje;
	}

	public StCollectionPersonalBusinessDetails getGrywmx() {
		return grywmx;
	}

	public void setGrywmx(StCollectionPersonalBusinessDetails grywmx) {
		this.updated_at = new Date();
		this.grywmx = grywmx;
	}

	public String getYwlsh() {
		return ywlsh;
	}

	public void setYwlsh(String ywlsh) {
		this.updated_at = new Date();
		this.ywlsh = ywlsh;
	}

	public String getGrzh() {
		return grzh;
	}

	public void setGrzh(String grzh) {
		this.updated_at = new Date();
		this.grzh = grzh;
	}

	public String getTqyy() {
		return tqyy;
	}

	public void setTqyy(String tqyy) {
		this.updated_at = new Date();
		this.tqyy = tqyy;
	}

	public String getTqfs() {
		return tqfs;
	}

	public void setTqfs(String tqfs) {
		this.updated_at = new Date();
		this.tqfs = tqfs;
	}

	public String getYwmxlx() {
		return ywmxlx;
	}

	public void setYwmxlx(String ywmxlx) {
		this.updated_at = new Date();
		this.ywmxlx = ywmxlx;
	}

	public String getXhyy() {
		return xhyy;
	}

	public void setXhyy(String xhyy) {
		this.updated_at = new Date();
		this.xhyy = xhyy;
	}

	public String getBlr() {
		return blr;
	}

	public void setBlr(String blr) {
		this.updated_at = new Date();
		this.blr = blr;
	}

	public String getDlrxm() {
		return dlrxm;
	}

	public void setDlrxm(String dlrxm) {
		this.updated_at = new Date();
		this.dlrxm = dlrxm;
	}

	public String getDlrzjhm() {
		return dlrzjhm;
	}

	public void setDlrzjhm(String dlrzjhm) {
		this.updated_at = new Date();
		this.dlrzjhm = dlrzjhm;
	}

	public BigDecimal getFse() {
		return fse;
	}

	public void setFse(BigDecimal fse) {
		this.updated_at = new Date();
		this.fse = fse;
	}

	public String getGrckzhhm() {
		return grckzhhm;
	}

	public void setGrckzhhm(String grckzhhm) {
		this.updated_at = new Date();
		this.grckzhhm = grckzhhm;
	}

	public String getHuming() {
		return huming;
	}

	public void setHuming(String huming) {
		this.updated_at = new Date();
		this.huming = huming;
	}

	public String getGrckzhkhyhmc() {
		return grckzhkhyhmc;
	}

	public void setGrckzhkhyhmc(String grckzhkhyhmc) {
		this.updated_at = new Date();
		this.grckzhkhyhmc = grckzhkhyhmc;
	}

	public Date getJzrq() {
		return jzrq;
	}

	public void setJzrq(Date jzrq) {
		this.updated_at = new Date();
		this.jzrq = jzrq;
	}

	public String getBlzl() {
		return blzl;
	}

	public void setBlzl(String blzl) {
		this.updated_at = new Date();
		this.blzl = blzl;
	}

	public Date getSlsj() {
		return slsj;
	}

	public void setSlsj(Date slsj) {
		this.updated_at = new Date();
		this.slsj = slsj;
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

	public String getIsChuLi() {
		return icl;
	}

	public void setIsChuLi(String icl) {
		this.updated_at = new Date();
		this.icl = icl;
	}

	public CCollectionWithdrawlVice() {
		super();

	}

	public CCollectionWithdrawlVice(String ywlsh, String grzh, String tqyy, String tqfs, String ywmxlx, String xhyy, String blr, String dlrxm, String dlrzjlx, String dlrzjhm, BigDecimal fse, String grckzhhm, String grckzhkhyhmc, String huming, Date jzrq, String blzl, Date slsj, String czy, String ywwd, String icl, BigDecimal ljtqje, BigDecimal yhke, Date xctqrq, BigDecimal jqe, String hkfs, StCollectionPersonalBusinessDetails grywmx) {
		this.ywlsh = ywlsh;
		this.grzh = grzh;
		this.tqyy = tqyy;
		this.tqfs = tqfs;
		this.ywmxlx = ywmxlx;
		this.xhyy = xhyy;
		this.blr = blr;
		this.dlrxm = dlrxm;
		this.dlrzjlx = dlrzjlx;
		this.dlrzjhm = dlrzjhm;
		this.fse = fse;
		this.grckzhhm = grckzhhm;
		this.grckzhkhyhmc = grckzhkhyhmc;
		this.huming = huming;
		this.jzrq = jzrq;
		this.blzl = blzl;
		this.slsj = slsj;
		this.czy = czy;
		this.ywwd = ywwd;
		this.icl = icl;
		this.ljtqje = ljtqje;
		this.yhke = yhke;
		this.xctqrq = xctqrq;
		this.jqe = jqe;
		this.hkfs = hkfs;
		this.grywmx = grywmx;
	}
}

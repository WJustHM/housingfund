package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "c_loan_apply_repayment_vice")
@org.hibernate.annotations.Table(appliesTo = "c_loan_apply_repayment_vice", comment = "还款申请表")
public class CLoanApplyRepaymentVice extends Common implements java.io.Serializable {

	private static final long serialVersionUID = 8206261408715861555L;

//	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinColumn(name = "GRYWMX", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '个人业务明细'")
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "loanApplyRepaymentVice")
	private CLoanHousingBusinessProcess grywmx;

	@Column(name = "DKZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '贷款账号'")
	private String dkzh;
	@Column(name = "HKJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '还款金额'")
	private BigDecimal hkje = BigDecimal.ZERO;
	@Column(name = "YDKKRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '约定扣款日期'")
	private Date ydkkrq;
	@Column(name = "HKQC", columnDefinition = "NUMERIC(4,0) DEFAULT NULL COMMENT '还款期次'")
	private BigDecimal hkqc = BigDecimal.ZERO;
	@Column(name = "SYQC", columnDefinition = "NUMERIC(4,0) DEFAULT NULL COMMENT '剩余期次'")
	private BigDecimal syqc = BigDecimal.ZERO;
	@Column(name = "YSYHKE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '原首月还款额'")
	private BigDecimal ysyhke = BigDecimal.ZERO;
	@Column(name = "MYDJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '每月递减'")
	private BigDecimal mydj = BigDecimal.ZERO;
	@Column(name = "YZHHKQX", columnDefinition = "DATETIME DEFAULT NULL COMMENT '原最后还款期限'")
	private Date yzhhkqx;
	@Column(name = "GYYCHKE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '该月一次还款额'")
	private BigDecimal gyychke = BigDecimal.ZERO;
	@Column(name = "XYHKE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '新月还款额'")
	private BigDecimal xyhke = BigDecimal.ZERO;
	@Column(name = "SYBJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '剩余本金'")
	private BigDecimal sybj = BigDecimal.ZERO;
	@Column(name = "XMYDJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '新每月递减'")
	private BigDecimal xmydj = BigDecimal.ZERO;
	@Column(name = "JYLX", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '节约利息'")
	private BigDecimal jylx = BigDecimal.ZERO;
	@Column(name = "SYLX", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '剩余利息'")
	private BigDecimal sylx = BigDecimal.ZERO;
	@Column(name = "YQKZJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '逾期款总计'")
	private BigDecimal yqkzj = BigDecimal.ZERO;
	@Column(name = "XZHHKQX", columnDefinition = "DATETIME DEFAULT NULL COMMENT '新最后还款期限'")
	private Date xzhhkqx;
	@Column(name = "HKLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '还款类型'")
	private String hklx;
	@Column(name = "JKRXM", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '借款人姓名'")
	private String jkrxm;
	@Column(name = "HKFS", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '还款方式'")
	private String hkfs;
	@Column(name = "JKRZJHM", columnDefinition = "VARCHAR(18) DEFAULT NULL COMMENT '借款人证件号码'")
	private String jkrzjhm;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "grywmx")
	private List<CLoanHousingOverdueVice> cLoanHousingOverdueVices;

	public CLoanApplyRepaymentVice() {
		super();
	}

	public CLoanApplyRepaymentVice(String id, Date created_at, Date updated_at, Date deleted_at, Boolean deleted,
			CLoanHousingBusinessProcess grywmx, String dkzh, BigDecimal hkje, Date ydkkrq, BigDecimal hkqc,
			BigDecimal ysyhke, BigDecimal mydj, Date yzhhkqx, BigDecimal gyychke, BigDecimal xyhke, BigDecimal xmydj,
			BigDecimal jylx, Date xzhhkqx, String hklx, BigDecimal sylx, BigDecimal syqc, BigDecimal sybj, String jkrxm,
			String hkfs, BigDecimal yqkzj, String jkrzjhm) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.grywmx = grywmx;
		this.dkzh = dkzh;
		this.hkje = hkje;
		this.ydkkrq = ydkkrq;
		this.hkqc = hkqc;
		this.ysyhke = ysyhke;
		this.mydj = mydj;
		this.yzhhkqx = yzhhkqx;
		this.gyychke = gyychke;
		this.xyhke = xyhke;
		this.xmydj = xmydj;
		this.jylx = jylx;
		this.xzhhkqx = xzhhkqx;
		this.hklx = hklx;
		this.syqc = syqc;
		this.sylx = sylx;
		this.sybj = sybj;
		this.jkrxm = jkrxm;
		this.hkfs = hkfs;
		this.yqkzj = yqkzj;
		this.jkrzjhm = jkrzjhm;
	}

	public CLoanHousingBusinessProcess getGrywmx() {
		return grywmx;
	}

	public void setGrywmx(CLoanHousingBusinessProcess grywmx) {
		this.updated_at = new Date();
		this.grywmx = grywmx;
	}

	public String getDkzh() {
		return dkzh;
	}

	public void setDkzh(String dkzh) {
		this.updated_at = new Date();
		this.dkzh = dkzh;
	}

	public BigDecimal getHkje() {
		return hkje;
	}

	public void setHkje(BigDecimal hkje) {
		this.updated_at = new Date();
		this.hkje = hkje;
	}

	public Date getYdkkrq() {
		return ydkkrq;
	}

	public void setYdkkrq(Date ydkkrq) {
		this.updated_at = new Date();
		this.ydkkrq = ydkkrq;
	}

	public BigDecimal getHkqc() {
		return hkqc;
	}

	public void setHkqc(BigDecimal hkqc) {
		this.updated_at = new Date();
		this.hkqc = hkqc;
	}

	public BigDecimal getSyqc() {
		return syqc;
	}

	public void setSyqc(BigDecimal syqc) {
		this.updated_at = new Date();
		this.syqc = syqc;
	}

	public BigDecimal getYsyhke() {
		return ysyhke;
	}

	public void setYsyhke(BigDecimal ysyhke) {
		this.updated_at = new Date();
		this.ysyhke = ysyhke;
	}

	public BigDecimal getMydj() {
		return mydj;
	}

	public void setMydj(BigDecimal mydj) {
		this.updated_at = new Date();
		this.mydj = mydj;
	}

	public Date getYzhhkqx() {
		return yzhhkqx;
	}

	public void setYzhhkqx(Date yzhhkqx) {
		this.updated_at = new Date();
		this.yzhhkqx = yzhhkqx;
	}

	public BigDecimal getGyychke() {
		return gyychke;
	}

	public void setGyychke(BigDecimal gyychke) {
		this.updated_at = new Date();
		this.gyychke = gyychke;
	}

	public BigDecimal getXyhke() {
		return xyhke;
	}

	public void setXyhke(BigDecimal xyhke) {
		this.updated_at = new Date();
		this.xyhke = xyhke;
	}

	public BigDecimal getSybj() {
		return sybj;
	}

	public void setSybj(BigDecimal sybj) {
		this.updated_at = new Date();
		this.sybj = sybj;
	}

	public BigDecimal getXmydj() {
		return xmydj;
	}

	public void setXmydj(BigDecimal xmydj) {
		this.updated_at = new Date();
		this.xmydj = xmydj;
	}

	public BigDecimal getJylx() {
		return jylx;
	}

	public void setJylx(BigDecimal jylx) {
		this.updated_at = new Date();
		this.jylx = jylx;
	}

	public BigDecimal getSylx() {
		return sylx;
	}

	public void setSylx(BigDecimal sylx) {
		this.updated_at = new Date();
		this.sylx = sylx;
	}

	public BigDecimal getYqkzj() {
		return yqkzj;
	}

	public void setYqkzj(BigDecimal yqkzj) {
		this.updated_at = new Date();
		this.yqkzj = yqkzj;
	}

	public Date getXzhhkqx() {
		return xzhhkqx;
	}

	public void setXzhhkqx(Date xzhhkqx) {
		this.updated_at = new Date();
		this.xzhhkqx = xzhhkqx;
	}

	public String getHklx() {
		return hklx;
	}

	public void setHklx(String hklx) {
		this.updated_at = new Date();
		this.hklx = hklx;
	}

	public String getJkrxm() {
		return jkrxm;
	}

	public void setJkrxm(String jkrxm) {
		this.updated_at = new Date();
		this.jkrxm = jkrxm;
	}

	public String getHkfs() {
		return hkfs;
	}

	public void setHkfs(String hkfs) {
		this.updated_at = new Date();
		this.hkfs = hkfs;
	}

	public String getJkrzjhm() {
		return jkrzjhm;
	}

	public void setJkrzjhm(String jkrzjhm) {
		this.updated_at = new Date();
		this.jkrzjhm = jkrzjhm;
	}

	public List<CLoanHousingOverdueVice> getcLoanHousingOverdueVices() {
		return cLoanHousingOverdueVices;
	}

	public void setcLoanHousingOverdueVices(List<CLoanHousingOverdueVice> cLoanHousingOverdueVices) {
		this.updated_at = new Date();
		this.cLoanHousingOverdueVices = cLoanHousingOverdueVices;
	}

}

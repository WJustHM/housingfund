package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_finance_bank_deposit_journal")
@org.hibernate.annotations.Table(appliesTo = "st_finance_bank_deposit_journal", comment = "银行存款日记账信息 表8.0.6")
public class StFinanceBankDepositJournal extends Common implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1314286542840155730L;
	@Column(name = "CNLSH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '出纳流水号'")
	private String cnlsh;
	@Column(name = "YHZHHM", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '银行专户号码'")
	private String yhzhhm;
	@Column(name = "JFFSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '借方发生额'")
	private BigDecimal jffse = BigDecimal.ZERO;
	@Column(name = "DFFSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '贷方发生额'")
	private BigDecimal dffse = BigDecimal.ZERO;
	@Column(name = "YuE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '余额'")
	private BigDecimal yuE = BigDecimal.ZERO;
	@Column(name = "YEJDFX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '余额借贷方向'")
	private String yejdfx;
	@Column(name = "YHJSLSH", columnDefinition = "VARCHAR(40) DEFAULT NULL COMMENT '银行结算流水号'")
	private String yhjslsh;
	@Column(name = "ZhaiYao", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '摘要'")
	private String zhaiYao;
	@Column(name = "JZRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '记账日期'")
	private Date jzrq;
	@Column(name = "RZRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '入账日期'")
	private Date rzrq;
	@Column(name = "RZZT", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '入账状态'")
	private String rzzt;
	@Column(name = "PZSSNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '凭证所属年月'")
	private String pzssny;
	@Column(name = "CZBS", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '冲账标识'")
	private String czbs;
	@Column(name = "ZJYWLX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '资金业务类型'")
	private String zjywlx;
	@Column(name = "JZPZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '记账凭证号'")
	private String jzpzh;

	public StFinanceBankDepositJournal() {
		super();

	}

	public StFinanceBankDepositJournal(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String cnlsh, String yhzhhm, BigDecimal jffse, BigDecimal dffse, BigDecimal yuE, String yejdfx,
			String yhjslsh, String zhaiYao, Date jzrq, Date rzrq, String rzzt, String pzssny, String czbs,
			String zjywlx, String jzpzh) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.cnlsh = cnlsh;
		this.yhzhhm = yhzhhm;
		this.jffse = jffse;
		this.dffse = dffse;
		this.yuE = yuE;
		this.yejdfx = yejdfx;
		this.yhjslsh = yhjslsh;
		this.zhaiYao = zhaiYao;
		this.jzrq = jzrq;
		this.rzrq = rzrq;
		this.rzzt = rzzt;
		this.pzssny = pzssny;
		this.czbs = czbs;
		this.zjywlx = zjywlx;
		this.jzpzh = jzpzh;
	}

	public String getCnlsh() {
		return this.cnlsh;
	}

	public void setCnlsh(String cnlsh) {
		this.updated_at = new Date();
		this.cnlsh = cnlsh;
	}

	public String getYhzhhm() {
		return this.yhzhhm;
	}

	public void setYhzhhm(String yhzhhm) {
		this.updated_at = new Date();
		this.yhzhhm = yhzhhm;
	}

	public BigDecimal getJffse() {
		return this.jffse;
	}

	public void setJffse(BigDecimal jffse) {
		this.updated_at = new Date();
		this.jffse = jffse;
	}

	public BigDecimal getDffse() {
		return this.dffse;
	}

	public void setDffse(BigDecimal dffse) {
		this.updated_at = new Date();
		this.dffse = dffse;
	}

	public BigDecimal getYuE() {
		return this.yuE;
	}

	public void setYuE(BigDecimal yuE) {
		this.updated_at = new Date();
		this.yuE = yuE;
	}

	public String getYejdfx() {
		return this.yejdfx;
	}

	public void setYejdfx(String yejdfx) {
		this.updated_at = new Date();
		this.yejdfx = yejdfx;
	}

	public String getYhjslsh() {
		return this.yhjslsh;
	}

	public void setYhjslsh(String yhjslsh) {
		this.updated_at = new Date();
		this.yhjslsh = yhjslsh;
	}

	public String getZhaiYao() {
		return this.zhaiYao;
	}

	public void setZhaiYao(String zhaiYao) {
		this.updated_at = new Date();
		this.zhaiYao = zhaiYao;
	}

	public Date getJzrq() {
		return this.jzrq;
	}

	public void setJzrq(Date jzrq) {
		this.updated_at = new Date();
		this.jzrq = jzrq;
	}

	public Date getRzrq() {
		return this.rzrq;
	}

	public void setRzrq(Date rzrq) {
		this.updated_at = new Date();
		this.rzrq = rzrq;
	}

	public String getRzzt() {
		return this.rzzt;
	}

	public void setRzzt(String rzzt) {
		this.updated_at = new Date();
		this.rzzt = rzzt;
	}

	public String getPzssny() {
		return this.pzssny;
	}

	public void setPzssny(String pzssny) {
		this.updated_at = new Date();
		this.pzssny = pzssny;
	}

	public String getCzbs() {
		return this.czbs;
	}

	public void setCzbs(String czbs) {
		this.updated_at = new Date();
		this.czbs = czbs;
	}

	public String getZjywlx() {
		return this.zjywlx;
	}

	public void setZjywlx(String zjywlx) {
		this.updated_at = new Date();
		this.zjywlx = zjywlx;
	}

	public String getJzpzh() {
		return this.jzpzh;
	}

	public void setJzpzh(String jzpzh) {
		this.updated_at = new Date();
		this.jzpzh = jzpzh;
	}

}

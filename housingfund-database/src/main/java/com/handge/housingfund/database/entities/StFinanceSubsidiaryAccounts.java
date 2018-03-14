package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_finance_subsidiary_accounts")
@org.hibernate.annotations.Table(appliesTo = "st_finance_subsidiary_accounts", comment = "明细账信息 表 8.0.4")
public class StFinanceSubsidiaryAccounts extends Common implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5125162316359485832L;
	@Column(name = "KMBH", columnDefinition = "VARCHAR(19) DEFAULT NULL COMMENT '科目编号'")
	private String kmbh;
	@Column(name = "JZPZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '记账凭证号'")
	private String jzpzh;
	@Column(name = "ZhaiYao", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '摘要'")
	private String zhaiYao;
	@Column(name = "QCYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '期初余额'")
	private BigDecimal qcye = BigDecimal.ZERO;
	@Column(name = "QCYEFX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '期初余额方向'")
	private String qcyefx;
	@Column(name = "JFFSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '借方发生额'")
	private BigDecimal jffse = BigDecimal.ZERO;
	@Column(name = "DFFSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '贷方发生额'")
	private BigDecimal dffse = BigDecimal.ZERO;
	@Column(name = "QMYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '期末余额'")
	private BigDecimal qmye = BigDecimal.ZERO;
	@Column(name = "QMYEFX", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '期末余额方向'")
	private String qmyefx;
	@Column(name = "JZRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '记账日期'")
	private Date jzrq;

	public StFinanceSubsidiaryAccounts(){
       super();


	}

	public StFinanceSubsidiaryAccounts(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String kmbh, String jzpzh, String zhaiYao, BigDecimal qcye, String qcyefx, BigDecimal jffse,
			BigDecimal dffse, BigDecimal qmye, String qmyefx, Date jzrq) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.kmbh = kmbh;
		this.jzpzh = jzpzh;
		this.zhaiYao = zhaiYao;
		this.qcye = qcye;
		this.qcyefx = qcyefx;
		this.jffse = jffse;
		this.dffse = dffse;
		this.qmye = qmye;
		this.qmyefx = qmyefx;
		this.jzrq = jzrq;
	}

	public String getKmbh() {
		return this.kmbh;
	}

	public void setKmbh(String kmbh) {
       this.updated_at = new Date();
		this.kmbh = kmbh;
	}

	public String getJzpzh() {
		return this.jzpzh;
	}

	public void setJzpzh(String jzpzh) {
       this.updated_at = new Date();
		this.jzpzh = jzpzh;
	}

	public String getZhaiYao() {
		return this.zhaiYao;
	}

	public void setZhaiYao(String zhaiYao) {
       this.updated_at = new Date();
		this.zhaiYao = zhaiYao;
	}

	public BigDecimal getQcye() {
		return this.qcye;
	}

	public void setQcye(BigDecimal qcye) {
       this.updated_at = new Date();
		this.qcye = qcye;
	}

	public String getQcyefx() {
		return this.qcyefx;
	}

	public void setQcyefx(String qcyefx) {
       this.updated_at = new Date();
		this.qcyefx = qcyefx;
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

	public BigDecimal getQmye() {
		return this.qmye;
	}

	public void setQmye(BigDecimal qmye) {
       this.updated_at = new Date();
		this.qmye = qmye;
	}

	public String getQmyefx() {
		return this.qmyefx;
	}

	public void setQmyefx(String qmyefx) {
       this.updated_at = new Date();
		this.qmyefx = qmyefx;
	}

	public Date getJzrq() {
		return this.jzrq;
	}

	public void setJzrq(Date jzrq) {
       this.updated_at = new Date();
		this.jzrq = jzrq;
	}

}

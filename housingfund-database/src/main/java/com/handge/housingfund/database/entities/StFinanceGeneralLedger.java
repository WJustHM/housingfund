package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_finance_general_ledger")
@org.hibernate.annotations.Table(appliesTo = "st_finance_general_ledger", comment = "总账信息 表8.0.3")

public class StFinanceGeneralLedger extends Common implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8713465957238730501L;
	@Column(name = "KMBH", columnDefinition = "VARCHAR(19) DEFAULT NULL COMMENT '科目编号'")
	private String kmbh;
	@Column(name = "KMMC", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '科目名称'")
	private String kmmc;
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
	@Column(name = "ZhaiYao", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '摘要'")
	private String zhaiYao;

	public StFinanceGeneralLedger(){
       super();


	}

	public StFinanceGeneralLedger(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String kmbh, String kmmc, BigDecimal qcye, String qcyefx, BigDecimal jffse, BigDecimal dffse,
			BigDecimal qmye, String qmyefx, Date jzrq, String zhaiYao) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.kmbh = kmbh;
		this.kmmc = kmmc;
		this.qcye = qcye;
		this.qcyefx = qcyefx;
		this.jffse = jffse;
		this.dffse = dffse;
		this.qmye = qmye;
		this.qmyefx = qmyefx;
		this.jzrq = jzrq;
		this.zhaiYao = zhaiYao;
	}

	public String getKmbh() {
		return kmbh;
	}

	public void setKmbh(String kmbh) {
       this.updated_at = new Date();
		this.kmbh = kmbh;
	}

	public String getKmmc() {
		return kmmc;
	}

	public void setKmmc(String kmmc) {
       this.updated_at = new Date();
		this.kmmc = kmmc;
	}

	public BigDecimal getQcye() {
		return qcye;
	}

	public void setQcye(BigDecimal qcye) {
       this.updated_at = new Date();
		this.qcye = qcye;
	}

	public String getQcyefx() {
		return qcyefx;
	}

	public void setQcyefx(String qcyefx) {
       this.updated_at = new Date();
		this.qcyefx = qcyefx;
	}

	public BigDecimal getJffse() {
		return jffse;
	}

	public void setJffse(BigDecimal jffse) {
       this.updated_at = new Date();
		this.jffse = jffse;
	}

	public BigDecimal getDffse() {
		return dffse;
	}

	public void setDffse(BigDecimal dffse) {
       this.updated_at = new Date();
		this.dffse = dffse;
	}

	public BigDecimal getQmye() {
		return qmye;
	}

	public void setQmye(BigDecimal qmye) {
       this.updated_at = new Date();
		this.qmye = qmye;
	}

	public String getQmyefx() {
		return qmyefx;
	}

	public void setQmyefx(String qmyefx) {
       this.updated_at = new Date();
		this.qmyefx = qmyefx;
	}

	public Date getJzrq() {
		return jzrq;
	}

	public void setJzrq(Date jzrq) {
       this.updated_at = new Date();
		this.jzrq = jzrq;
	}

	public String getZhaiYao() {
		return zhaiYao;
	}

	public void setZhaiYao(String zhaiYao) {
       this.updated_at = new Date();
		this.zhaiYao = zhaiYao;
	}
}

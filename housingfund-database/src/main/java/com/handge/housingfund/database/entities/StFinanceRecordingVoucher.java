package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "st_finance_recording_voucher")
@org.hibernate.annotations.Table(appliesTo = "st_finance_recording_voucher", comment = "记账凭证信息 表8.0.5")
public class StFinanceRecordingVoucher extends Common implements java.io.Serializable {

	private static final long serialVersionUID = -2323216244759919847L;
	@Column(name = "JZPZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '记账凭证号'")
	private String jzpzh;
	@Column(name = "ZhaiYao", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '摘要'")
	private String zhaiYao;
	@Column(name = "KMBH", columnDefinition = "VARCHAR(19) DEFAULT NULL COMMENT '科目编号'")
	private String kmbh;
	@Column(name = "JFFSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '借方发生额'")
	private BigDecimal jffse = BigDecimal.ZERO;
	@Column(name = "DFFSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '贷方发生额'")
	private BigDecimal dffse = BigDecimal.ZERO;
	@Column(name = "FJDJS", columnDefinition = "NUMERIC(5,0) DEFAULT NULL COMMENT '附件单据数'")
	private BigDecimal fjdjs = BigDecimal.ZERO;
	@Column(name = "JZRQ", columnDefinition = "DATETIME DEFAULT NULL COMMENT '记账日期'")
	private Date jzrq;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "extension", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '记账凭证信息 扩展'")
	private CFinanceRecordingVoucherExtension cFinanceRecordingVoucherExtension;

	public StFinanceRecordingVoucher() {
	}

	public StFinanceRecordingVoucher(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String jzpzh, String zhaiYao, String kmbh, BigDecimal jffse, BigDecimal dffse, BigDecimal fjdjs, Date jzrq,
			CFinanceRecordingVoucherExtension cFinanceRecordingVoucherExtension) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.jzpzh = jzpzh;
		this.zhaiYao = zhaiYao;
		this.kmbh = kmbh;
		this.jffse = jffse;
		this.dffse = dffse;
		this.fjdjs = fjdjs;
		this.jzrq = jzrq;
		this.cFinanceRecordingVoucherExtension = cFinanceRecordingVoucherExtension;
	}

	public CFinanceRecordingVoucherExtension getcFinanceRecordingVoucherExtension() {
		return cFinanceRecordingVoucherExtension;
	}

	public void setcFinanceRecordingVoucherExtension(
			CFinanceRecordingVoucherExtension cFinanceRecordingVoucherExtension) {
		this.updated_at = new Date();
		this.cFinanceRecordingVoucherExtension = cFinanceRecordingVoucherExtension;
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

	public String getKmbh() {
		return this.kmbh;
	}

	public void setKmbh(String kmbh) {
		this.updated_at = new Date();
		this.kmbh = kmbh;
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

	public BigDecimal getFjdjs() {
		return this.fjdjs;
	}

	public void setFjdjs(BigDecimal fjdjs) {
		this.updated_at = new Date();
		this.fjdjs = fjdjs;
	}

	public Date getJzrq() {
		return this.jzrq;
	}

	public void setJzrq(Date jzrq) {
		this.updated_at = new Date();
		this.jzrq = jzrq;
	}

}

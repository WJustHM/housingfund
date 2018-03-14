package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * Created by 凡 on 2017/7/21. 单位补缴
 */
@Entity
@Table(name = "c_collection_unit_payback_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_unit_payback_vice", comment = "单位补缴")
public class CCollectionUnitPaybackVice extends Common implements Serializable {

	private static final long serialVersionUID = 7533031718044636360L;
	// TODO 清册确认单号长度/规则确认
	@Column(name = "QCQRDH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '清册确认单号'")
	private String qcqrdh;
	@Column(name = "BJFS", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '补缴方式'")
	private String bjfs;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "DWYWMX", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '单位业务明细'")
	private StCollectionUnitBusinessDetails dwywmx;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "BJMX", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '补缴明细'")
	private Set<CCollectionUnitPaybackDetailVice> bjmx;

	@Column(name = "FSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '发生额'")
	private BigDecimal fse = BigDecimal.ZERO;

	public BigDecimal getFse() {
		return fse;
	}

	public void setFse(BigDecimal fse) {
		this.fse = fse;
	}

	public String getQcqrdh() {
		return qcqrdh;
	}

	public void setQcqrdh(String qcqrdh) {
		this.updated_at = new Date();
		this.qcqrdh = qcqrdh;
	}

	public StCollectionUnitBusinessDetails getDwywmx() {
		return dwywmx;
	}

	public void setDwywmx(StCollectionUnitBusinessDetails dwywmx) {
		this.updated_at = new Date();
		this.dwywmx = dwywmx;
	}

	public String getBjfs() {
		return bjfs;
	}

	public void setBjfs(String bjfs) {
		this.updated_at = new Date();
		this.bjfs = bjfs;
	}

	public Set<CCollectionUnitPaybackDetailVice> getBjmx() {
		return bjmx;
	}

	public void setBjmx(Set<CCollectionUnitPaybackDetailVice> bjmx) {
		this.updated_at = new Date();
		this.bjmx = bjmx;
	}

	public CCollectionUnitPaybackVice() {
		super();

	}

	public CCollectionUnitPaybackVice(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String qcqrdh, StCollectionUnitBusinessDetails dwywmx, Set<CCollectionUnitPaybackDetailVice> bjmx, BigDecimal fse) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.qcqrdh = qcqrdh;
		this.dwywmx = dwywmx;
		this.bjmx = bjmx;
		this.fse = fse;
	}
}

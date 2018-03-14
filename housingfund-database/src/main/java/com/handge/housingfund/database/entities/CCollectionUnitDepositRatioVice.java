package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 凡 on 2017/7/21. 单位比例调整
 */
@Entity
@Table(name = "c_collection_unit_deposit_ratio_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_unit_deposit_ratio_vice", comment = "单位比例调整")
public class CCollectionUnitDepositRatioVice extends Common implements Serializable {

	private static final long serialVersionUID = -161428665175403644L;
	@Column(name = "TZQDWBL", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '调整前单位比例'")
	private BigDecimal tzqdwbl = BigDecimal.ZERO;
	@Column(name = "TZQGRBL", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '调整前个人比例'")
	private BigDecimal tzqgrbl = BigDecimal.ZERO;
	@Column(name = "TZHDWBL", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '调整后单位比例'")
	private BigDecimal tzhdwbl = BigDecimal.ZERO;
	@Column(name = "TZHGRBL", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '调整后个人比例'")
	private BigDecimal tzhgrbl = BigDecimal.ZERO;
	@Column(name = "SXNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '生效年月'")
	private String sxny;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "DWYWMX", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '单位业务明细'")
	private StCollectionUnitBusinessDetails dwywmx;

	public BigDecimal getTzqdwbl() {
		return tzqdwbl;
	}

	public void setTzqdwbl(BigDecimal tzqdwbl) {
		this.updated_at = new Date();
		this.tzqdwbl = tzqdwbl;
	}

	public BigDecimal getTzqgrbl() {
		return tzqgrbl;
	}

	public void setTzqgrbl(BigDecimal tzqgrbl) {
		this.updated_at = new Date();
		this.tzqgrbl = tzqgrbl;
	}

	public BigDecimal getTzhdwbl() {
		return tzhdwbl;
	}

	public void setTzhdwbl(BigDecimal tzhdwbl) {
		this.updated_at = new Date();
		this.tzhdwbl = tzhdwbl;
	}

	public BigDecimal getTzhgrbl() {
		return tzhgrbl;
	}

	public void setTzhgrbl(BigDecimal tzhgrbl) {
		this.updated_at = new Date();
		this.tzhgrbl = tzhgrbl;
	}

	public String getSxny() {
		return sxny;
	}

	public void setSxny(String sxny) {
		this.updated_at = new Date();
		this.sxny = sxny;
	}

	public StCollectionUnitBusinessDetails getDwywmx() {
		return dwywmx;
	}

	public void setDwywmx(StCollectionUnitBusinessDetails dwywmx) {
		this.updated_at = new Date();
		this.dwywmx = dwywmx;
	}

	public CCollectionUnitDepositRatioVice() {
		super();

	}

	public CCollectionUnitDepositRatioVice(String id, Date created_at, Date updated_at, Date deleted_at,
										   boolean deleted, BigDecimal tzqdwbl, BigDecimal tzqgrbl, BigDecimal tzhdwbl, BigDecimal tzhgrbl,
										   String sxny, StCollectionUnitBusinessDetails dwywmx) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.tzqdwbl = tzqdwbl;
		this.tzqgrbl = tzqgrbl;
		this.tzhdwbl = tzhdwbl;
		this.tzhgrbl = tzhgrbl;
		this.sxny = sxny;
		this.dwywmx = dwywmx;
	}
}

package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 凡 on 2017/7/21. 单位清册详情
 */
@Entity
@Table(name = "c_collection_unit_deposit_inventory_detail_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_unit_deposit_inventory_detail_vice", comment = "单位清册详情")
public class CCollectionUnitDepositInventoryDetailVice extends Common implements Serializable {

	private static final long serialVersionUID = 3925231403785486818L;
	// TODO 清册确认单号长度/规则确认
	@Column(name = "QCQRDH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '清册确认单号'")
	private String qcqrdh;
	@Column(name = "GRZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '个人账号'")
	private String grzh;
	@Column(name = "GRJCJS", columnDefinition = "NUMERIC(18,2) DEFAULT 0 NOT NULL COMMENT '个人缴存基数'")
	private BigDecimal grjcjs = BigDecimal.ZERO;
	@Column(name = "GRYJCE", columnDefinition = "NUMERIC(18,2) DEFAULT 0 NOT NULL COMMENT '个人月缴存额'")
	private BigDecimal gryjce = BigDecimal.ZERO;
	@Column(name = "DWYJCE", columnDefinition = "NUMERIC(18,2) DEFAULT 0 NOT NULL COMMENT '单位月缴存额'")
	private BigDecimal dwyjce = BigDecimal.ZERO;
	@Column(name = "YJCE", columnDefinition = "NUMERIC(18,2) DEFAULT 0 NOT NULL COMMENT '月缴存额'")
	private BigDecimal yjce = BigDecimal.ZERO;
	@Column(name = "QCFSE", columnDefinition = "NUMERIC(18,2) DEFAULT 0 NOT NULL COMMENT '清册发生额'")
	private BigDecimal qcfse = BigDecimal.ZERO;
	@Column(name = "GRZHZT", columnDefinition = "VARCHAR(2) DEFAULT NULL COMMENT '个人账户状态'")
	private String grzhzt;
	@Column(name = "GRZHYE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '个人账户余额'")
	private BigDecimal grzhye = BigDecimal.ZERO;

	@ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
	private CCollectionUnitDepositInventoryVice inventory;

	public String getQcqrdh() {
		return qcqrdh;
	}

	public void setQcqrdh(String qcqrdh) {
		this.updated_at = new Date();
		this.qcqrdh = qcqrdh;
	}

	public String getGrzh() {
		return grzh;
	}

	public void setGrzh(String grzh) {
		this.updated_at = new Date();
		this.grzh = grzh;
	}

	public BigDecimal getGrjcjs() {
		return grjcjs;
	}

	public void setGrjcjs(BigDecimal grjcjs) {
		this.updated_at = new Date();
		this.grjcjs = grjcjs;
	}

	public BigDecimal getGryjce() {
		return gryjce;
	}

	public void setGryjce(BigDecimal gryjce) {
		this.updated_at = new Date();
		this.gryjce = gryjce;
	}

	public BigDecimal getDwyjce() {
		return dwyjce;
	}

	public void setDwyjce(BigDecimal dwyjce) {
		this.updated_at = new Date();
		this.dwyjce = dwyjce;
	}

	public BigDecimal getYjce() {
		return yjce;
	}

	public void setYjce(BigDecimal yjce) {
		this.yjce = yjce;
	}

	public BigDecimal getQcfse() {
		return qcfse;
	}

	public void setQcfse(BigDecimal qcfse) {
		this.qcfse = qcfse;
	}

	public String getGrzhzt() {
		return grzhzt;
	}

	public void setGrzhzt(String grzhzt) {
		this.grzhzt = grzhzt;
	}

	public BigDecimal getGrzhye() {
		return grzhye;
	}

	public void setGrzhye(BigDecimal grzhye) {
		this.grzhye = grzhye;
	}

	public CCollectionUnitDepositInventoryVice getInventory() {
		return inventory;
	}

	public void setInventory(CCollectionUnitDepositInventoryVice inventory) {
		this.inventory = inventory;
	}

	public CCollectionUnitDepositInventoryDetailVice() {
		super();

	}

	public CCollectionUnitDepositInventoryDetailVice(String id, Date created_at, Date updated_at, Date deleted_at,
			boolean deleted, String qcqrdh, String grzh, BigDecimal grjcjs, BigDecimal gryjce, BigDecimal dwyjce) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.qcqrdh = qcqrdh;
		this.grzh = grzh;
		this.grjcjs = grjcjs;
		this.gryjce = gryjce;
		this.dwyjce = dwyjce;
	}

}

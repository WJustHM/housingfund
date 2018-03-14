package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * Created by 凡 on 2017/7/20. 单位缴存清册
 */
@Entity
@Table(name = "c_collection_unit_deposit_inventory_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_unit_deposit_inventory_vice", comment = "单位缴存清册")
public class CCollectionUnitDepositInventoryVice extends Common implements Serializable,Comparable<CCollectionUnitDepositInventoryVice> {

	private static final long serialVersionUID = 8956949383786063321L;
	// TODO 清册确认单号长度/规则确认
	@Column(name = "QCQRDH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '清册确认单号'")
	private String qcqrdh;
	@Column(name = "QCNY", columnDefinition = "VARCHAR(6) DEFAULT NULL COMMENT '清册年月'")
	private String qcny;
	@Column(name = "DWJCRS", columnDefinition = "NUMERIC(18,0) DEFAULT NULL COMMENT '单位缴存人数'")
	private BigDecimal dwjcrs = BigDecimal.ZERO;
	@Column(name = "DWFCRS", columnDefinition = "NUMERIC(18,0) DEFAULT NULL COMMENT '单位封存人数'")
	private BigDecimal dwfcrs = BigDecimal.ZERO;
	@Column(name = "DWJCBL", columnDefinition = "NUMERIC(4,2) DEFAULT NULL COMMENT '单位缴存比例'")
	private BigDecimal dwjcbl = BigDecimal.ZERO;
	@Column(name = "GRJCBL", columnDefinition = "NUMERIC(4,2) DEFAULT NULL COMMENT '个人缴存比例'")
	private BigDecimal grjcbl = BigDecimal.ZERO;
	@Column(name = "DWYJCEHJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '单位月缴存额合计'")
	private BigDecimal dwyjcehj = BigDecimal.ZERO;
	@Column(name = "GRYJCEHJ", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '个人月缴存额合计'")
	private BigDecimal gryjcehj = BigDecimal.ZERO;
	@Column(name = "QCFSE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '清册发生额'")
	private BigDecimal qcfse = BigDecimal.ZERO;;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "DWYWMX", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '单位业务明细'")
	private StCollectionUnitBusinessDetails dwywmx;

	@JoinColumn(name = "inventory_id", columnDefinition = "VARCHAR(32) DEFAULT NULL COMMENT '清册详情'")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<CCollectionUnitDepositInventoryDetailVice> qcxq;

	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private CCollectionUnitDepositInventoryBatchVice qcpc;

	public String getQcqrdh() {
		return qcqrdh;
	}

	public void setQcqrdh(String qcqrdh) {
		this.updated_at = new Date();
		this.qcqrdh = qcqrdh;
	}

	public String getQcny() {
		return qcny;
	}

	public void setQcny(String qcny) {
		this.updated_at = new Date();
		this.qcny = qcny;
	}

	public BigDecimal getDwjcrs() {
		return dwjcrs;
	}

	public void setDwjcrs(BigDecimal dwjcrs) {
		this.updated_at = new Date();
		this.dwjcrs = dwjcrs;
	}

	public BigDecimal getDwfcrs() {
		return dwfcrs;
	}

	public void setDwfcrs(BigDecimal dwfcrs) {
		this.updated_at = new Date();
		this.dwfcrs = dwfcrs;
	}

	public BigDecimal getDwjcbl() {
		return dwjcbl;
	}

	public void setDwjcbl(BigDecimal dwjcbl) {
		this.updated_at = new Date();
		this.dwjcbl = dwjcbl;
	}

	public BigDecimal getGrjcbl() {
		return grjcbl;
	}

	public void setGrjcbl(BigDecimal grjcbl) {
		this.updated_at = new Date();
		this.grjcbl = grjcbl;
	}

	public StCollectionUnitBusinessDetails getDwywmx() {
		return dwywmx;
	}

	public void setDwywmx(StCollectionUnitBusinessDetails dwywmx) {
		this.updated_at = new Date();
		this.dwywmx = dwywmx;
	}

	public Set<CCollectionUnitDepositInventoryDetailVice> getQcxq() {
		return qcxq;
	}

	public void setQcxq(Set<CCollectionUnitDepositInventoryDetailVice> qcxq) {
		this.updated_at = new Date();
		this.qcxq = qcxq;
	}

	public BigDecimal getDwyjcehj() {
		return dwyjcehj;
	}

	public void setDwyjcehj(BigDecimal dwyjcehj) {
		this.updated_at = new Date();
		this.dwyjcehj = dwyjcehj;
	}

	public BigDecimal getGryjcehj() {
		return gryjcehj;
	}

	public void setGryjcehj(BigDecimal gryjcehj) {
		this.updated_at = new Date();
		this.gryjcehj = gryjcehj;
	}

	public BigDecimal getQcfse() {
		return qcfse;
	}

	public void setQcfse(BigDecimal qcfse) {
		this.qcfse = qcfse;
	}

	public CCollectionUnitDepositInventoryBatchVice getQcpc() {
		return qcpc;
	}

	public void setQcpc(CCollectionUnitDepositInventoryBatchVice qcpc) {
		this.qcpc = qcpc;
	}

	public CCollectionUnitDepositInventoryVice() {
		super();

	}

	public CCollectionUnitDepositInventoryVice(String id, Date created_at, Date updated_at, Date deleted_at,
			boolean deleted, String qcqrdh, String qcny, BigDecimal dwjcrs, BigDecimal dwfcrs, BigDecimal dwjcbl,
			BigDecimal grjcbl, BigDecimal dwyjcehj, BigDecimal gryjcehj, StCollectionUnitBusinessDetails dwywmx,
			Set<CCollectionUnitDepositInventoryDetailVice> qcxq) {

		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.qcqrdh = qcqrdh;
		this.qcny = qcny;
		this.dwjcrs = dwjcrs;
		this.dwfcrs = dwfcrs;
		this.dwjcbl = dwjcbl;
		this.grjcbl = grjcbl;
		this.dwyjcehj = dwyjcehj;
		this.gryjcehj = gryjcehj;
		this.dwywmx = dwywmx;
		this.qcxq = qcxq;
	}

	@Override
	public int compareTo(CCollectionUnitDepositInventoryVice o) {
		String qcny1 = this.getQcny();
		String qcny2 = o.getQcny();
		int i = Integer.parseInt(qcny1);
		int j = Integer.parseInt(qcny2);
		return i-j;
	}
}

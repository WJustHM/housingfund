package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 凡 on 2017/7/21. 单位补缴明细
 */
@Entity
@Table(name = "c_collection_unit_payback_detail_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_unit_payback_detail_vice", comment = "单位补缴明细")
public class CCollectionUnitPaybackDetailVice extends Common implements Serializable {

	private static final long serialVersionUID = 3902727111376856279L;
	@Column(name = "GRZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '个人账号'")
	private String grzh;
	@Column(name = "GRBJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '个人补缴额'")
	private BigDecimal gryjce = BigDecimal.ZERO;
	@Column(name = "DWBJE", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '单位补缴额'")
	private BigDecimal dwyjce = BigDecimal.ZERO;
	@Column(name = "BJYY", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '补缴原因'")
	private String bjyy;

	public String getGrzh() {
		return grzh;
	}

	public void setGrzh(String grzh) {
		this.updated_at = new Date();
		this.grzh = grzh;
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

	public String getBjyy() {
		return bjyy;
	}

	public void setBjyy(String bjyy) {
		this.updated_at = new Date();
		this.bjyy = bjyy;
	}

	public CCollectionUnitPaybackDetailVice() {
		super();

	}

	public CCollectionUnitPaybackDetailVice(String id, Date created_at, Date updated_at, Date deleted_at,
			boolean deleted, String grzh, BigDecimal gryjce, BigDecimal dwyjce, String bjyy) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.grzh = grzh;
		this.gryjce = gryjce;
		this.dwyjce = dwyjce;
		this.bjyy = bjyy;
	}
}

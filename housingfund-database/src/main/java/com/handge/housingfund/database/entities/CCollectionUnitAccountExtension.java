package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "c_collection_unit_account_extension")
@org.hibernate.annotations.Table(appliesTo = "c_collection_unit_account_extension", comment = "缴存单位信息扩展表")
public class CCollectionUnitAccountExtension extends Common implements Serializable {

	private static final long serialVersionUID = -3767117250534980830L;

	@Column(name = "FXZHKHYH", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '发薪账号开户银行'")
	private String fxzhkhyh;
	@Column(name = "FXZH", columnDefinition = "VARCHAR(30) DEFAULT NULL COMMENT '发薪账号'")
	private String fxzh;
	@Column(name = "FXZHHM", columnDefinition = "VARCHAR(120) DEFAULT NULL COMMENT '发薪账号户名'")
	private String fxzhhm;
	@Column(name = "ZSYE", columnDefinition = "NUMERIC(18,2) DEFAULT 0 NOT NULL COMMENT '暂收余额'")
	private BigDecimal zsye = BigDecimal.ZERO;
	/*
	 * @Column(name = "LSYLWFT", columnDefinition =
	 * "decimal(18,2) DEFAULT 0 COMMENT '历史遗留未分摊'") private BigDecimal lsylwft = BigDecimal.ZERO;
	 */

	public String getFxzhkhyh() {
		return fxzhkhyh;
	}

	public void setFxzhkhyh(String fxzhkhyh) {

		this.fxzhkhyh = fxzhkhyh;
	}

	public String getFxzhhm() {
		return fxzhhm;
	}

	public void setFxzhhm(String fxzhhm) {

		this.fxzhhm = fxzhhm;
	}

	public BigDecimal getZsye() {
		return zsye;
	}

	public void setZsye(BigDecimal zsye) {

		this.zsye = zsye;
	}

	/*
	 * public BigDecimal getLsylwft() { return lsylwft; }
	 * 
	 * public void setLsylwft(BigDecimal lsylwft) {
	 * 
	 * this.lsylwft = lsylwft; }
	 */

	public String getFxzh() {
		return fxzh;
	}

	public void setFxzh(String fxzh) {

		this.fxzh = fxzh;
	}

	public CCollectionUnitAccountExtension() {
		super();

	}

	public CCollectionUnitAccountExtension(
			String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String fxzhkhyh, String fxzh, String fxzhhm,
			BigDecimal zsye/* , BigDecimal lsylwft */) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.fxzhkhyh = fxzhkhyh;
		this.fxzh = fxzh;
		this.fxzhhm = fxzhhm;
		this.zsye = zsye;
		/* this.lsylwft = lsylwft; */
	}

}

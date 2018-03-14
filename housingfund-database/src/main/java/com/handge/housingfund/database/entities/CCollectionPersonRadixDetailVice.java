package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 凡 on 2017/7/21. 个人基数调整详情
 */
@Entity
@Table(name = "c_collection_person_radix_detail_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_person_radix_detail_vice", comment = "个人基数调整")
public class CCollectionPersonRadixDetailVice extends Common implements Serializable,Comparable<CCollectionPersonRadixDetailVice> {

	private static final long serialVersionUID = -743126397253011837L;
	@Column(name = "GRZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '个人账号'")
	private String grzh;
	@Column(name = "TZQGRJS", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '调整前个人基数'")
	private BigDecimal tzqgrjs = BigDecimal.ZERO;
	@Column(name = "TZHGRJS", columnDefinition = "NUMERIC(18,2) DEFAULT NULL COMMENT '调整后个人基数'")
	private BigDecimal tzhgrjs = BigDecimal.ZERO;


	public String getGrzh() {
		return grzh;
	}

	public void setGrzh(String grzh) {
		this.updated_at = new Date();
		this.grzh = grzh;
	}

	public BigDecimal getTzqgrjs() {
		return tzqgrjs;
	}

	public void setTzqgrjs(BigDecimal tzqgrjs) {
		this.updated_at = new Date();
		this.tzqgrjs = tzqgrjs;
	}

	public BigDecimal getTzhgrjs() {
		return tzhgrjs;
	}

	public void setTzhgrjs(BigDecimal tzhgrjs) {
		this.updated_at = new Date();
		this.tzhgrjs = tzhgrjs;
	}

	public CCollectionPersonRadixDetailVice() {
		super();

	}

	public CCollectionPersonRadixDetailVice(String id, Date created_at, Date updated_at, Date deleted_at,
			boolean deleted, String grzh, BigDecimal tzqgrjs, BigDecimal tzhgrjs) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.grzh = grzh;
		this.tzqgrjs = tzqgrjs;
		this.tzhgrjs = tzhgrjs;
	}

	@Override
	public int compareTo(CCollectionPersonRadixDetailVice o) {
		return this.getGrzh().compareTo(o.getGrzh());
	}

}

package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by 凡 on 2017/7/20. 单位汇缴详情
 */
@Entity
@Table(name = "c_collection_unit_remittance_detail_vice")
@org.hibernate.annotations.Table(appliesTo = "c_collection_unit_remittance_detail_vice", comment = "单位汇缴详情")
public class CCollectionUnitRemittanceDetailVice extends Common implements Serializable {

	private static final long serialVersionUID = -4202102123021240960L;
	@Column(name = "GRZH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '个人账号'")
	private String grzh;
	@Column(name = "QCQRDH", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '清册确认单号'")
	private String qcqrdh;

	public String getGrzh() {
		return grzh;
	}

	public void setGrzh(String grzh) {
		this.updated_at = new Date();
		this.grzh = grzh;
	}

	public String getQcqrdh() {
		return qcqrdh;
	}

	public void setQcqrdh(String qcqrdh) {
		this.updated_at = new Date();
		this.qcqrdh = qcqrdh;
	}

	public CCollectionUnitRemittanceDetailVice() {
		super();

	}

	public CCollectionUnitRemittanceDetailVice(String id, Date created_at, Date updated_at, Date deleted_at,
			boolean deleted, String grzh, String qcqrdh) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.grzh = grzh;
		this.qcqrdh = qcqrdh;
	}

}

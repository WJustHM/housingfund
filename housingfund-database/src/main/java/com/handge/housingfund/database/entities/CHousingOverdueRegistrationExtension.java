package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Funnyboy on 2017/8/31.
 */
@Entity
@Table(name = "c_housing_overdue_registration_extension")
@org.hibernate.annotations.Table(appliesTo = "c_housing_overdue_registration_extension", comment = "个人住房贷款逾期登记信息副表 表6.0.7")
public class CHousingOverdueRegistrationExtension extends Common implements Serializable {

	private static final long serialVersionUID = 6138863900335946785L;

	@Column(name = "YWZT", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务状态'")
	private String ywzt;

	public CHousingOverdueRegistrationExtension() {
		super();
	}

	public CHousingOverdueRegistrationExtension(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted, String ywzt) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.ywzt = ywzt;
	}

	public String getYwzt() {
		return ywzt;
	}

	public void setYwzt(String ywzt) {
		this.ywzt = ywzt;
	}

}

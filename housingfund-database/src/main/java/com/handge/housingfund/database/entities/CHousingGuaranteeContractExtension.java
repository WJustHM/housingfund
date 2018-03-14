package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Liujuhao on 2017/8/16.
 */

@Entity
@Table(name = "c_housing_guarantee_contract_extension")
@org.hibernate.annotations.Table(appliesTo = "c_housing_guarantee_contract_extension", comment = "抵押表")
public class CHousingGuaranteeContractExtension extends Common implements Serializable {

	private static final long serialVersionUID = -6629500781983157321L;

	@Column(name = "BLZL", columnDefinition = "TEXT DEFAULT NULL COMMENT '办理资料'")
	private String blzl;

	public CHousingGuaranteeContractExtension() {
		super();

	}
	public CHousingGuaranteeContractExtension(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted, String blzl) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.blzl = blzl;
	}

	public String getBlzl() {
		return blzl;
	}

	public void setBlzl(String blzl) {

		this.blzl = blzl;
	}


}

package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/5.
 */
@Entity
@Table(name = "c_settlement_special_bank_account_extension")
@org.hibernate.annotations.Table(appliesTo = "c_settlement_special_bank_account_extension", comment = "银行专户信息 扩展表")
public class CSettlementSpecialBankAccountExtension extends Common implements Serializable {

	private static final long serialVersionUID = -4960153162042297394L;

	@Column(name = "SFYSY", columnDefinition = "BIT(1) DEFAULT 0 COMMENT '是否已使用'")
	private Boolean sfysy;
	@Column(name = "status", columnDefinition = "VARCHAR(16) DEFAULT 0 COMMENT '0 : 正常， 1 : 销户'")
	private String status;

	public CSettlementSpecialBankAccountExtension() {
		super();
	}

	public CSettlementSpecialBankAccountExtension(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted, Boolean sfysy, String status) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.sfysy = sfysy;
		this.status = status;
	}

	public Boolean getSfysy() {
		return sfysy;
	}

	public void setSfysy(Boolean sfysy) {
		this.sfysy = sfysy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}

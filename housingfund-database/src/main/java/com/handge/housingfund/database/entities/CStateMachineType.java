package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by 凡 on 2017/8/5.
 */
@Entity
@Table(name = "c_state_machine_type")
@org.hibernate.annotations.Table(appliesTo = "c_state_machine_type", comment = "业务状态机步骤类型")
public class CStateMachineType extends Common implements Serializable {

	private static final long serialVersionUID = 751586067931349861L;

	@Column(name = "STEP", columnDefinition = "VARCHAR(20) NOT NULL COMMENT '状态机步骤'")
	private String step;

	@Column(name = "YWZT", columnDefinition = "VARCHAR(2) NOT NULL COMMENT '业务状态'")
	private String ywzt;

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.updated_at = new Date();
		this.step = step;
	}

	public String getYwzt() {
		return ywzt;
	}

	public void setYwzt(String ywzt) {
		this.updated_at = new Date();
		this.ywzt = ywzt;
	}

	public CStateMachineType() {
		super();
	}

	public CStateMachineType(String step, String ywzt) {
		this.updated_at = new Date();
		this.step = step;
		this.ywzt = ywzt;
	}
}

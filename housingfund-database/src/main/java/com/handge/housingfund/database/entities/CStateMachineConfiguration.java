package com.handge.housingfund.database.entities;

import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.database.enums.TransitionKind;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "c_state_machine_configuration")
@org.hibernate.annotations.Table(appliesTo = "c_state_machine_configuration", comment = "业务状态机配置表")
public class CStateMachineConfiguration extends Common implements Serializable {
	private static final long serialVersionUID = 7585329812569696855L;
	@Enumerated(EnumType.STRING)
	@Column(name = "TYPE", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务所属类型'")
	private BusinessType type;
	@Column(name = "SUB_TYPE", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务子类型'")
	private String subType;
	@Column(name = "SOURCE", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '源状态'")
	private String source;
	@Column(name = "TARGET", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '目标状态'")
	private String target;
	@Column(name = "EVENT", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '触发事件'")
	private String event;
	@Column(name = "FLAG", columnDefinition = "BIT(1) DEFAULT NULL COMMENT '是否启用'")
	private boolean flag;
	@Column(name = "ROLE", columnDefinition = "TEXT DEFAULT NULL COMMENT '操作人员'")
	private String role;
	@Column(name = "WORKSTATION", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '业务网点'")
	private String workstation;
	@Enumerated(EnumType.STRING)
	@Column(name = "TransitionKind", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '状态转移类型'")
	private TransitionKind transitionKind;

	@Column(name = "effectiveDate", columnDefinition = "DATETIME DEFAULT NOW() COMMENT '生效时间'" )
	private Date effectiveDate  = new Date();

	@Column(name = "IsAudit", columnDefinition = "VARCHAR(20) DEFAULT NULL COMMENT '是否是审核流程'")
	private String isAudit;

	public BusinessType getType() {
		return type;
	}


	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		if (effectiveDate == null) {
			this.effectiveDate = new Date();
		}
		this.effectiveDate = effectiveDate;
	}

	public void setType(BusinessType type) {
		this.updated_at = new Date();
		this.type = type;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.updated_at = new Date();
		this.subType = subType;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.updated_at = new Date();
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.updated_at = new Date();
		this.target = target;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.updated_at = new Date();
		this.event = event;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.updated_at = new Date();
		this.flag = flag;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.updated_at = new Date();
		this.role = role;
	}

	public TransitionKind getTransitionKind() {
		return transitionKind;
	}

	public void setTransitionKind(TransitionKind transitionKind) {
		this.updated_at = new Date();
		this.transitionKind = transitionKind;
	}

	public String getWorkstation() {
		return workstation;
	}

	public void setWorkstation(String workstation) {
		this.updated_at = new Date();
		this.workstation = workstation;
	}

	public String getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(String isAudit) {
		this.updated_at = new Date();
		this.isAudit = isAudit;
	}

	public CStateMachineConfiguration(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
									  BusinessType type, String subType, String source, String target, String event, boolean flag, String role,
			TransitionKind transitionKind,Date effectiveDate) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.type = type;
		this.subType = subType;
		this.source = source;
		this.target = target;
		this.event = event;
		this.flag = flag;
		this.role = role;
		this.transitionKind = transitionKind;
		this.effectiveDate = effectiveDate;
	}

	public CStateMachineConfiguration() {
		super();
	}


	public CStateMachineConfiguration(BusinessType type, String subType, String source, String target, String event, boolean flag, String role,
									  TransitionKind transitionKind,Date effectiveDate,String isAudit ,String workstation){
		this.type = type;
		this.subType = subType;
		this.source = source;
		this.target = target;
		this.event = event;
		this.flag = flag;
		this.role = role;
		this.transitionKind = transitionKind;
		this.effectiveDate = effectiveDate;
		this.isAudit = isAudit;
		this.workstation = workstation;

	}

	@Override
	public String toString() {
		return "CStateMachineConfiguration{" +
				"type=" + type +
				", subType='" + subType + '\'' +
				", source='" + source + '\'' +
				", target='" + target + '\'' +
				", event='" + event + '\'' +
				", flag=" + flag +
				", role='" + role + '\'' +
				", workstation='" + workstation + '\'' +
				", transitionKind=" + transitionKind +
				", effectiveDate=" + effectiveDate +
				", isAudit='" + isAudit + '\'' +
				'}';
	}
}

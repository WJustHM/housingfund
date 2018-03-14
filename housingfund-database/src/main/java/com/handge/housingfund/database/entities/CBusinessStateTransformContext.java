package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "c_business_state_transform_context")
@org.hibernate.annotations.Table(appliesTo = "c_business_state_transform_context", comment = "业务状态转换上下文表")
public class CBusinessStateTransformContext extends Common implements Serializable {

	private static final long serialVersionUID = 7698458380221293705L;
	@Column(name = "taskid", columnDefinition = "VARCHAR(20) NOT NULL COMMENT '业务流水号'")
	private String taskId;
	@Column(name = "context", columnDefinition = "TEXT NOT NULL COMMENT '转移上下文'")
	private String context;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.updated_at = new Date();
		this.taskId = taskId;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.updated_at = new Date();
		this.context = context;
	}

	public CBusinessStateTransformContext(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted,
			String taskId, String context) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = updated_at;
		this.taskId = taskId;
		this.context = context;
	}

	public CBusinessStateTransformContext() {
		super();

	}

}

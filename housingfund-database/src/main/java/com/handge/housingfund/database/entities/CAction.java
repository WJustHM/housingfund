package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Liujuhao on 2017/7/13.
 */
@Entity
@Table(name = "c_action")
@org.hibernate.annotations.Table(appliesTo = "c_action", comment = "权限api表")
public class CAction extends Common implements Serializable {

	private static final long serialVersionUID = -6596987195163732599L;

	@Column(name = "action_url", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '请求路径'")
	private String action_url;

	@Column(name = "action_method", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '请求方法'")
	private String action_method;

	@Column(name = "action_code", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '请求代码'")
	private String action_code;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "c_permission_action", joinColumns = @JoinColumn(name = "action_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private List<CPermission> permissions = new ArrayList<CPermission>();

	public String getAction_url() {
		return action_url;
	}

	public void setAction_url(String action_url) {
		this.updated_at = new Date();
		this.action_url = action_url;
	}

	public String getAction_method() {
		return action_method;
	}

	public void setAction_method(String action_method) {
		this.updated_at = new Date();
		this.action_method = action_method;
	}

	public String getAction_code() {
		return action_code;
	}

	public void setAction_code(String action_code) {
		this.updated_at = new Date();
		this.action_code = action_code;
	}

	public List<CPermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<CPermission> permissions) {
		this.updated_at = new Date();
		this.permissions = permissions;
	}

	public CAction() {
		super();

	}

	public CAction(String id, Date createdAt, boolean deleted, Date deletedAt, Date updatedAt, String actionUrl,
			String actionMethod, String actionCode) {
		this.id = id;
		this.created_at = createdAt;
		this.deleted = deleted;
		this.deleted_at = deletedAt;
		this.updated_at = updatedAt;
		this.action_url = actionUrl;
		this.action_method = actionMethod;
		this.action_code = actionCode;
	}

}

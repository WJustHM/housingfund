package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Liujuhao on 2017/7/12.
 */
@Entity
@Table(name = "c_permission")
@org.hibernate.annotations.Table(appliesTo = "c_permission", comment = "权限表")
public class CPermission extends Common implements Serializable {

	private static final long serialVersionUID = 7588579722767049326L;

	@Column(name = "permission_name", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL COMMENT '权限名称'")
	private String permission_name;

	@Column(name = "permission_note", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '权限备注'")
	private String permission_note;

	@Column(name = "permission_code", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '权限代码'")
	private String permission_code;

	@Column(name = "module_id", nullable = false, columnDefinition = "VARCHAR(32) NOT NULL COMMENT '模块ID'")
	private String module_id;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "c_role_permission", joinColumns = @JoinColumn(name = "permission_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<CRole> roles = new ArrayList<>();

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "c_permission_action", joinColumns = @JoinColumn(name = "permission_id"), inverseJoinColumns = @JoinColumn(name = "action_id"))
	private List<CAction> actions = new ArrayList<>();

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "module_id", insertable = false, updatable = false)
	private CAccountModule cAccountModule;

	public String getPermission_name() {
		return permission_name;
	}

	public void setPermission_name(String permission_name) {
       this.updated_at = new Date();
		this.permission_name = permission_name;
	}

	public String getPermission_note() {
		return permission_note;
	}

	public void setPermission_note(String permission_note) {
       this.updated_at = new Date();
		this.permission_note = permission_note;
	}

	public String getPermission_code() {
		return permission_code;
	}

	public void setPermission_code(String permission_code) {
       this.updated_at = new Date();
		this.permission_code = permission_code;
	}

	public CPermission(){
       super();


	}

	public List<CRole> getRoles() {
		return roles;
	}

	public void setRoles(List<CRole> roles) {
       this.updated_at = new Date();
		this.roles = roles;
	}

	public List<CAction> getActions() {
		return actions;
	}

	public void setActions(List<CAction> actions) {
       this.updated_at = new Date();
		this.actions = actions;
	}

	public String getModule_id() {
		return module_id;
	}

	public void setModule_id(String module_id) {
       this.updated_at = new Date();
		this.module_id = module_id;
	}

	public CAccountModule getcAccountModule() {
		return cAccountModule;
	}

	public void setcAccountModule(CAccountModule cAccountModule) {
       this.updated_at = new Date();
		this.cAccountModule = cAccountModule;
	}

	public CPermission(String id, Date created_at, boolean deleted, Date deleted_at, Date updated_at,
			String permissionName, String permissionNote, String permissionCode, String module_id,
			List<CAction> actions) {
		this.id = id;
		this.created_at = created_at;
		this.deleted = deleted;
		this.deleted_at = deleted_at;
		this.updated_at = updated_at;
		this.permission_name = permissionName;
		this.permission_note = permissionNote;
		this.permission_code = permissionCode;
		this.actions = actions;
		this.module_id = module_id;
		// this.cAccountModule = cAccountModule;
	}

}

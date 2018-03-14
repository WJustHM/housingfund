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
@Table(name = "c_role")
@org.hibernate.annotations.Table(appliesTo = "c_role", comment = "角色表")
public class CRole extends Common implements Serializable {

	private static final long serialVersionUID = 6363413284830175426L;

	@Column(name = "role_name", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL COMMENT '角色名称'")
	private String role_name;

	@Column(name = "role_note", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '角色备注'")
	private String role_note;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "c_auth_role", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "auth_id"))
	private List<CAuth> auths = new ArrayList<>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "c_role_permission", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private List<CPermission> permissions = new ArrayList<>();

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
       this.updated_at = new Date();
		this.role_name = role_name;
	}

	public List<CAuth> getAuths() {
		return auths;
	}

	public void setAuths(List<CAuth> auths) {
       this.updated_at = new Date();
		this.auths = auths;
	}

	public List<CPermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<CPermission> permissions) {
       this.updated_at = new Date();
		this.permissions = permissions;
	}

	public String getRole_note() {
		return role_note;
	}

	public void setRole_note(String role_note) {
       this.updated_at = new Date();
		this.role_note = role_note;
	}

	public CRole(){
       super();


	}

	public CRole(String id, Date created_at, boolean deleted, Date deleted_at, Date updated_at, String roleName,
			String role_note, List<CPermission> permissions) {
		this.id = id;
		this.created_at = created_at;
		this.deleted = deleted;
		this.deleted_at = deleted_at;
		this.updated_at = updated_at;
		this.role_name = roleName;
		this.role_note = role_note;
		this.permissions = permissions;
	}

}

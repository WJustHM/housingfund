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
@Table(name = "c_auth")
@org.hibernate.annotations.Table(appliesTo = "c_auth", comment = "认证表")
public class CAuth extends Common implements Serializable {

	private static final long serialVersionUID = -8431333744790369566L;

	@Column(name = "user_id", nullable = false, columnDefinition = "VARCHAR(32) NOT NULL COMMENT '用户id'")
	private String user_id;

	@Column(name = "type", nullable = false, columnDefinition = "tinyint(4) NOT NULL COMMENT '用户类型（0：最高管理员，1：个人账户，2：员工账户，3：单位账户，4：房开商账号）'")
	private int type;

	@Column(name = "username", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL COMMENT '账号'")
	private String username;

	@Column(name = "password", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '密码'")
	private String password;

	@Column(name = "state", nullable = false, columnDefinition = "BIT(1) NOT NULL DEFAULT 0 COMMENT '状态（1：启用，0：禁用）'")
	private Boolean state;

	@Column(name = "email", columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '邮箱'")
	private String email;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "c_auth_role", joinColumns = @JoinColumn(name = "auth_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<CRole> roles = new ArrayList<>();

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.updated_at = new Date();
		this.user_id = user_id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.updated_at = new Date();
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.updated_at = new Date();
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.updated_at = new Date();
		this.password = password;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.updated_at = new Date();
		this.state = state;
	}

	public List<CRole> getRoles() {
		return roles;
	}

	public void setRoles(List<CRole> roles) {
		this.updated_at = new Date();
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.updated_at = new Date();
		this.email = email;
	}

	public CAuth() {
		super();

	}

	public CAuth(String id, Date created_at, boolean deleted, Date deleted_at, Date updated_at, String user_id,
			int type, String username, String password, Boolean state, String email, List<CRole> roles) {
		this.id = id;
		this.created_at = created_at;
		this.deleted = deleted;
		this.deleted_at = deleted_at;
		this.updated_at = updated_at;
		this.user_id = user_id;
		this.type = type;
		this.username = username;
		this.password = password;
		this.state = state;
		this.roles = roles;
		this.email = email;
	}

}

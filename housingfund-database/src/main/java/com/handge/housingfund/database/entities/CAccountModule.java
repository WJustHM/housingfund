package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by tanyi on 2017/7/17.
 */
@Entity
@Table(name = "c_account_module")
@org.hibernate.annotations.Table(appliesTo = "c_account_module", comment = "组织管理-模块表")
public class CAccountModule extends Common implements Serializable {

	private static final long serialVersionUID = 390328426600977894L;

	@Column(name = "module_name", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL COMMENT '模块名称'")
	private String module_name;

	@Column(name = "classify_id", nullable = false, columnDefinition = "VARCHAR(32) NOT NULL COMMENT '分类ID'")
	private String classify_id;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = CPermission.class, mappedBy = "cAccountModule")
	private List<CPermission> cPermissions;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "classify_id", insertable = false, updatable = false)
	private CAccountClassify cAccountClassify;

	public String getModule_name() {
		return module_name;
	}

	public void setModule_name(String module_name) {
       this.updated_at = new Date();
		this.module_name = module_name;
	}

	public String getClassify_id() {
		return classify_id;
	}

	public void setClassify_id(String classify_id) {
       this.updated_at = new Date();
		this.classify_id = classify_id;
	}

	public List<CPermission> getcPermissions() {
		return cPermissions;
	}

	public void setcPermissions(List<CPermission> cPermissions) {
       this.updated_at = new Date();
		this.cPermissions = cPermissions;
	}

	public CAccountClassify getcAccountClassify() {
		return cAccountClassify;
	}

	public void setcAccountClassify(CAccountClassify cAccountClassify) {
       this.updated_at = new Date();
		this.cAccountClassify = cAccountClassify;
	}

	public CAccountModule(String id, Date createdAt, boolean deleted, Date deletedAt, Date updatedAt,
			String module_name, String classify_id, List<CPermission> cPermissions) {
		this.id = id;
		this.created_at = createdAt;
		this.deleted = deleted;
		this.deleted_at = deletedAt;
		this.updated_at = updatedAt;
		this.module_name = module_name;
		this.classify_id = classify_id;
		this.cPermissions = cPermissions;
		// this.cAccountClassify = cAccountClassify;
	}

	public CAccountModule(){
       super();


	}

}

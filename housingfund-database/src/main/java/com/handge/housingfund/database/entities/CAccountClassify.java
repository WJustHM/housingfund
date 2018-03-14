package com.handge.housingfund.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by tanyi on 2017/7/17.
 */
@Entity
@Table(name = "c_account_classify")
@org.hibernate.annotations.Table(appliesTo = "c_account_classify", comment = "组织管理-分类表")
public class CAccountClassify extends Common implements Serializable {

	private static final long serialVersionUID = 834685499259845444L;

	@Column(name = "classify_name", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL COMMENT '分类名称'")
	private String classify_name;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = CAccountModule.class, mappedBy = "cAccountClassify")
	private List<CAccountModule> cAccountModules;

	public String getClassify_name() {
		return classify_name;
	}

	public void setClassify_name(String classify_name) {
		this.updated_at = new Date();
		this.classify_name = classify_name;
	}

	public List<CAccountModule> getcAccountModules() {
		return cAccountModules;
	}

	public void setcAccountModules(List<CAccountModule> cAccountModules) {
		this.updated_at = new Date();
		this.cAccountModules = cAccountModules;
	}

	public CAccountClassify(String id, Date createdAt, boolean deleted, Date deletedAt, Date updatedAt,
			String classify_name, List<CAccountModule> cAccountModules) {
		this.id = id;
		this.created_at = createdAt;
		this.deleted = deleted;
		this.deleted_at = deletedAt;
		this.updated_at = updatedAt;
		this.classify_name = classify_name;
		this.cAccountModules = cAccountModules;
	}

	public CAccountClassify() {
		super();

	}

}

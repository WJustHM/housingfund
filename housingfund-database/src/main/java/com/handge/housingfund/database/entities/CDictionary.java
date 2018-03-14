package com.handge.housingfund.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "c_dictionary")
@org.hibernate.annotations.Table(appliesTo = "c_dictionary", comment = "字典表")
public class CDictionary extends Common implements Serializable {

	private static final long serialVersionUID = -1874964226508510919L;
	@Column(name = "no", columnDefinition = "NUMERIC(3,0) COMMENT '序号'")
	private BigDecimal no = BigDecimal.ZERO;
	@Column(name = "code", columnDefinition = "VARCHAR(10) NOT NULL COMMENT '编码'")
	private String code;
	@Column(name = "name", columnDefinition = "VARCHAR(20) NOT NULL COMMENT '名称'")
	private String name;
	@Column(name = "type")
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.updated_at = new Date();
		this.type = type;
	}

	public BigDecimal getNo() {
		return no;
	}

	public void setNo(BigDecimal no) {
		this.updated_at = new Date();
		this.no = no;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.updated_at = new Date();
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.updated_at = new Date();
		this.name = name;
	}

	public CDictionary() {
		super();

	}

	public CDictionary(String id, Date created_at, Date updated_at, Date deleted_at, boolean deleted, BigDecimal no,
			String code, String name, String type) {
		this.id = id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.deleted_at = deleted_at;
		this.deleted = deleted;
		this.no = no;
		this.code = code;
		this.name = name;
		this.type = type;
	}

}
